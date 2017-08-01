package com.amway.lms.backend.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.service.CronJobService;
import com.amway.lms.backend.service.RemindEmailService;
import com.amway.lms.backend.service.SessionParticipantService;
import com.amway.lms.backend.service.SessionService;
import com.amway.lms.backend.service.ldap.LdapUserService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;

@Service
@Transactional
@EnableScheduling
public class CronJobServiceImpl implements CronJobService {
    private static final Logger logger = LoggerFactory.getLogger(CronJobServiceImpl.class);

    @Autowired
    LdapUserService ldapUserService;

    @Autowired
    RemindEmailService remindEmailService;

    @Autowired
    SessionParticipantService sessionParticipantService;

    @Autowired
    SessionService sessionService;

    @Autowired
    ServletContext servletContext;

    @Override
    // @Scheduled(cron = "0 0 0 * * ?") // Daily at midnight
    @Scheduled(cron = "0 0/10 * * * ?") // Every 10 min
    public void executeCronJobSyncLDAPUser() {
        ldapUserService.syncUserData();
    }

    @Override
    @Scheduled(cron = "0 0/6 * * * ?") // Every 6 minutes
    // @Scheduled(fixedDelay = 20000)
    public void executeCronJobRemindEmailSurvey() {
        remindEmailService.remindSurveyEmail();
    }

    @Override
    // @Scheduled(cron = "0 30 2 * * *") // This cronjob will run 2:30 AM
    @Scheduled(cron = "0 0/15 * * * ?")
    public void executeCronJobAcceptForTrainee() {
        sessionParticipantService.setAcceptForTraineeCronJob();
    }

    @Override
    // @Scheduled(cron = "0 30 2 * * *") // This cronjob will run 2:30 AM
    @Scheduled(cron = "0 0/15 * * * ?")
    public void executeConJobSendEmailToTrainerForPostSurvey() {
        try {
            sessionService.sendEmailToTrainerForPostSurveyCronJob();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    // @Scheduled(cron = "0 0 15 * * ?")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void executeCronJobBackupDB() {
        StringBuffer output = new StringBuffer();
        Process p;
        try {
            String backupFileShell = new ClassPathResource("backup_database.sh").getURL().getFile().toString();
            logger.info("------****" + backupFileShell);
            String command = "sh " + backupFileShell;
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            logger.info("Command print out");
            logger.info(output.toString());

            uploadBackupFileToGoogleDrive();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FilenameFilter getFileNameFilter() {
        return new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                try {
                    if (!"sql".equals(Common.getExtension(name)))
                        return false;

                    String dbName = name.substring(0, name.length() - 24);
                    return "amway_dev".equals(dbName);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
    }

    /** Application name. */
    private static final String APPLICATION_NAME = "Drive API Java Amway";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
            ".credentials/drive-java");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials at
     * ~/.credentials/drive-java-quickstart
     */
    private static final java.util.Collection<String> SCOPES = DriveScopes.all();

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * 
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        
        InputStream in = CronJobServiceImpl.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * 
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    private void uploadBackupFileToGoogleDrive() {
        try {
            File folderContainBackupBD = new File("/amway-backup");
            File[] backupDBFiles = folderContainBackupBD.listFiles(getFileNameFilter());
            List<File> files = Arrays.asList(backupDBFiles);
            Collections.sort(files, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.compare(f1.lastModified(), f2.lastModified());
                }
            });
            for (int i = 0; i < files.size(); i++) {
                System.err.println("File in root after filter: " + (i + 1) + ": " + files.get(i).getName());
            }

            File backupFile = files.get(files.size() - 1);
            
            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
            fileMetadata.setName(backupFile.getName());
            fileMetadata.setParents(Collections.singletonList("0B-YVLMldK7WgN19EWVU3Z004Vk0"));
            java.io.File filePath = backupFile;
            FileContent mediaContent = new FileContent("application/sql", filePath);
            com.google.api.services.drive.model.File file = getDriveService().files().create(fileMetadata, mediaContent)
                    .setFields("id").execute();
            System.out.println("File ID: " + file.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
