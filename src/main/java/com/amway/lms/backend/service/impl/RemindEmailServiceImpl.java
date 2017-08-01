package com.amway.lms.backend.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.amway.lms.backend.common.EmailHepler;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.configuration.MailConfig;
import com.amway.lms.backend.entity.Result;
import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.entity.Survey;
import com.amway.lms.backend.entity.SurveySent;
import com.amway.lms.backend.entity.SurveySentEmail;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.repository.ResultRepository;
import com.amway.lms.backend.repository.SessionRepository;
import com.amway.lms.backend.repository.SurveyRepository;
import com.amway.lms.backend.repository.SurveySentEmailRepository;
import com.amway.lms.backend.repository.SurveySentRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.RemindEmailService;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.Action;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Clazz;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Duration;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Sequence;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.model.property.XProperty;

@Transactional
@Service
@EnableScheduling
public class RemindEmailServiceImpl implements RemindEmailService{
    
    private static final Logger logger = LoggerFactory.getLogger(RemindEmailServiceImpl.class);
    
    @Autowired
    private JavaMailSender javaMailSender;

    private static final String EMAIL_TEMPLATE_CLASSPATH_RES = "classpath:mail/template/default.html";

    private static final String AMWAY_LOGO_IMAGE = "mail/template/images/amway-logo.png";

    private static final String AMWAY_SURVEY_IMAGE = "mail/template/images/amway-survey.png";

    private static final String PNG_MIME = "image/png";
    
    private static final int QUESTION_ID_EMAIL = 22;
    
    private static final int QUESTION_ID_MSNV = 21;
    
    @Value("${number.day.send.email.remind}")
    private int NUMBER_DAY_TO_SENT_EMAIL_REMAIND;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmailHepler emailHepler;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    private SurveySentRepository surveySentRepository;

    @Autowired
    private SurveySentEmailRepository surveySentEmailRepository;
    
    @Autowired
    private ResultRepository resultRepository;
    
    @Autowired
    SessionRepository sessionRepository;
    
    @Override
    public void remindSurveyEmail() {
        logger.info("remindSurveyEmail Is running...");
        List<Survey> surveys = surveyRepository.getSurveyListByIsSent(1);
        
        for(Survey survey : surveys){
            
            int surveyId = survey.getId();
            SurveySent surveySent = surveySentRepository.getFirstSurveySentBySurveyId(surveyId);
            if(surveySent == null) {
                logger.info("Cannot find SurveySent with SurveyId = "+surveyId);
                continue;
            }
            
            if(!validateSurveyToRemind(survey, surveySent)){
                logger.info("Survey cannot sent email remind, id = "+survey.getId());
                continue;
            }

            List<SurveySentEmail> surveySentEmails = surveySentEmailRepository.getSurveySentEmailListBySurveySentId(surveySent.getId());

            List<String> emailsNotSurveyYet = getListEmailNotSurveyYet(survey, surveySentEmails);
            
            for(String e : emailsNotSurveyYet){
                System.out.println("Email not Survey: "+e); 
            }
            
            sentEmailToRemind(surveySent, emailsNotSurveyYet);
            
            surveySent.setIsRemind(1);
            surveySent.setRemindTime(Utils.getCurrentTime());
            surveySentRepository.updateSurveySent(surveySent);
        }
    }
    
    private boolean validateSurveyToRemind(Survey survey, SurveySent surveySent){
        if(surveySent.getIsRemind() != 0){
            logger.info("This survey was sent email remind, surveyId = "+survey.getId());
            return false;
        }
        if(Utils.getCurrentTime().after(survey.getEndingTime())){
            logger.debug("This survey is out of expried date");
            return false;
        }
        if(addDate(surveySent.getSentTime(), NUMBER_DAY_TO_SENT_EMAIL_REMAIND).after(survey.getEndingTime())){
            logger.info("This survey cannot send email remind. Cause the first time sent email + " 
                    +NUMBER_DAY_TO_SENT_EMAIL_REMAIND + " days is larger surveyEndingTime");
            return false;
        }
        if(Utils.getCurrentTime().before(addDate(surveySent.getSentTime(), NUMBER_DAY_TO_SENT_EMAIL_REMAIND))){
            logger.info("This survey cannot send email remind. Cause current time still befor the first time sent email + " 
                    +NUMBER_DAY_TO_SENT_EMAIL_REMAIND + " days.");
            return false;
        }
        return true;
    }
    
    private Timestamp addDate(Timestamp timestamp, int numberDays){
        return new Timestamp(timestamp.getTime() + 10*60*1000L); //for test, delay time is 10 minute
        //return new Timestamp(timestamp.getTime() + numberDays*24*60*60*1000L); //for release
    }
    
    public List<String> getListEmailNotSurveyYet(Survey survey, List<SurveySentEmail> surveySentEmails){
        List<String> emailsNotSurveyYet = new ArrayList<>();
        if(survey.getSurveyTypeId() == 2) return emailsNotSurveyYet;
        
        List<String> emailResults = getListEmailResult(survey, surveySentEmails);
        
        for(SurveySentEmail surveySentEmail : surveySentEmails){
            boolean isSurvey = false;
            for(String emailResult : emailResults){
                if (surveySentEmail.getEmail().equals(emailResult.trim()))
                    isSurvey = true;
            }
            if(!isSurvey){
                emailsNotSurveyYet.add(surveySentEmail.getEmail());
            }
        }
        
        return emailsNotSurveyYet;
    }
    
    public List<String> getListEmailResult(Survey survey, List<SurveySentEmail> surveySentEmails){
        List<String> emailsResult = new ArrayList<>();

        List<Result> results = resultRepository.getResultsBySurveyIdAndQuestionId(survey.getId(), QUESTION_ID_EMAIL);
        for(Result result : results){
            try {
                if(!isExistInSurveySentEmails(surveySentEmails, result)){
                    Result resultMSNV = resultRepository.getResultBySurveyResultIdSurveyIdQuestionId(result.getSurveyResultId(), 
                            survey.getId(), QUESTION_ID_MSNV);
                    if(resultMSNV == null){
                        logger.debug("Cannot find MSNV");
                        continue;
                    }

                    User user = userRepository.getUserByUserID(resultMSNV.getAnswerContent());
                    if(user == null){
                        logger.debug("Cannot find User with userID = "+resultMSNV.getAnswerContent());
                        continue;
                    }
                    emailsResult.add(user.getEmail());
                } else {
                    emailsResult.add(result.getAnswerContent());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return emailsResult;
    }
    
    private boolean isExistInSurveySentEmails(List<SurveySentEmail> surveySentEmails, Result result){
        if(surveySentEmails == null || result == null || result.getAnswerContent() == null) return false;
        for(SurveySentEmail surveySentEmail : surveySentEmails){
            if(result.getAnswerContent().equals(surveySentEmail.getEmail()))
                return true;
        }
        return false;
    }
    
    public void sentEmailToRemind(SurveySent surveySent, List<String> emails){
        try {
            MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
            Context ctx = new Context();

            ctx.setVariable("emailContents", surveySent.getHtmlContent());

            message.setFrom("admin@amway.com");
            message.setSubject("[Remind] "+surveySent.getSubject());
            //List<String> emails = emailInfor.getEmails();
            message.setTo(emails.toArray(new String[emails.size()]));
            String output = htmlTemplateEngine.process(getEmailTemplateByName(EMAIL_TEMPLATE_CLASSPATH_RES), ctx);
            message.setText(output, true /* isHtml */);

            message.addInline("amway-logo", new ClassPathResource(AMWAY_LOGO_IMAGE), PNG_MIME);
            message.addInline("amway-survey", new ClassPathResource(AMWAY_SURVEY_IMAGE), PNG_MIME);
            this.javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("--sentEmailToRemind: Cannot sent email remind");
        }
    }
    
    public String getEmailTemplateByName(String templateName) throws IOException {
        final Resource templateResource = this.applicationContext.getResource(templateName);
        final InputStream inputStream = templateResource.getInputStream();
        return IOUtils.toString(inputStream, MailConfig.EMAIL_TEMPLATE_ENCODING);
    }

    @Override
    public void testEmailCalendar(int sessionId) {
        try {
            Session session = sessionRepository.getSessionById(sessionId);
            
            MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            mimeMessage.addHeaderLine("method=REQUEST");
            mimeMessage.addHeaderLine("charset=UTF-8");
            mimeMessage.addHeaderLine("component=VEVENT");
            
            mimeMessage.setFrom(new InternetAddress("admin@amway.com"));
            mimeMessage.setSubject("Test calendar");
            mimeMessage.setRecipients(Message.RecipientType.TO, getInternetAddress(new String[]{"anh.vxt.it@gmail.com", "tuancom1993@gmail.com", "acton@enclave.vn", "missy@enclave.vn"}));
//            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
//            Context ctx = new Context();
//
//            ctx.setVariable("emailContents", "This is email to test");
//
//            message.setFrom("admin@amway.com");
//            message.setSubject("Test calendar");
//            //List<String> emails = emailInfor.getEmails();
//            message.setTo(new String[]{"anh.vxt.it@gmail.com", "tuancom1993@gmail.com", "acton@enclave.vn", "charles@enclave.vn"});
//            String output = htmlTemplateEngine.process(getEmailTemplateByName(EMAIL_TEMPLATE_CLASSPATH_RES), ctx);
//            message.setText(output, true /* isHtml */);
//
//            message.addInline("amway-logo", new ClassPathResource(AMWAY_LOGO_IMAGE), PNG_MIME);
//            message.addInline("amway-survey", new ClassPathResource(AMWAY_SURVEY_IMAGE), PNG_MIME);

            Calendar calendar = new Calendar();
            TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
            TimeZone timezone = registry.getTimeZone("Asia/Saigon");
            VTimeZone tz = timezone.getVTimeZone();
           
            PropertyList calendarProperties = calendar.getProperties();
            calendarProperties.add(new ProdId("-//Microsoft Corporation//Outlook 12.0 MIMEDIR//EN"));
            calendarProperties.add(Version.VERSION_2_0);
            calendarProperties.add(Method.REQUEST);
            calendarProperties.add(CalScale.GREGORIAN);
            calendarProperties.add(new XProperty("X-MS-OLK-FORCEINSPECTOROPEN", "TRUE"));
            
            VEvent event = new VEvent(new DateTime(session.getStartTime().getTime()), new DateTime(session.getEndTime().getTime()), "Session Time");
            //event.getProperties().add(tz.getTimeZoneId());
            event.getProperties().add(new Location(""+session.getLocationId()));
            event.getProperties().add(new Description("Description"));
            event.getProperties().add(new Organizer("mailto:acton@enclave.vn"));
            event.getProperties().add(Priority.MEDIUM);
            event.getProperties().add(Clazz.PUBLIC);
            event.getProperties().add(new Sequence());
//            event.getProperties().add(new UidGenerator("ssid:"+String.valueOf(session.getId())).generateUid());
            event.getProperties().add(new Uid("ssid:"+session.getId()));
            event.getProperties().add(new XProperty("X-MICROSOFT-CDO-BUSYSTATUS", "TENTATIVE"));
            event.getProperties().add(new XProperty("X-MICROSOFT-CDO-IMPORTANCE", "1"));
            event.getProperties().add(new XProperty("X-MICROSOFT-CDO-INTENDEDSTATUS", "BUSY"));
            event.getProperties().add(new XProperty("X-MICROSOFT-DISALLOW-COUNTER","FALSE"));
            event.getProperties().add(new XProperty("X-MS-OLK-ALLOWEXTERNCHECK", "TRUE"));
            event.getProperties().add(new XProperty("X-MS-OLK-AUTOSTARTCHECK", "FALSE"));
            event.getProperties().add(new XProperty("X-MS-OLK-CONFTYPE", "0"));
            
            VAlarm alarm = new VAlarm();
            alarm.getProperties().add(new Duration(new Dur(0, 0, -15, 0)));
            alarm.getProperties().add(Action.DISPLAY);
            alarm.getProperties().add(new Description("Reminder"));
            
            event.getAlarms().add(alarm);
            
            calendar.getComponents().add(event);
            System.err.println(Utils.generateSuccessResponseString(calendar));
            
         // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
            messageBodyPart.setHeader("Content-ID", "calendar_message");
            
            messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(calendar.toString(), "text/calendar;method=REQUEST")));// very important
            Multipart multipart = new MimeMultipart("alternative");
            multipart.addBodyPart(messageBodyPart);

            // Put parts in message
            mimeMessage.setContent(multipart);
            
            mimeMessage.writeTo(System.err);
            
//            message.addAttachment("SessionTime.ics", new ByteArrayDataSource(calendar.toString(), "text/calendar"));
            
            this.javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("--sentEmailToRemind: Cannot sent email remind");
        }
        
    }

    private InternetAddress[] getInternetAddress(String... emails){
        InternetAddress[] addresses = new InternetAddress[emails.length];
        for(int i = 0; i < emails.length; i++){
            try {
                addresses[i] = new InternetAddress(emails[i]);
            } catch (AddressException e) {
                e.printStackTrace();
            }
        }
        return addresses;
    }
}
