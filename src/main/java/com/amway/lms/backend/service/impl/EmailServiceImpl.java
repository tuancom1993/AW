package com.amway.lms.backend.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.amway.lms.backend.common.AmwayEnum;
import com.amway.lms.backend.common.AmwayEnum.ManagerActionStatus;
import com.amway.lms.backend.common.AmwayEnum.Roles;
import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.EmailHepler;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.configuration.MailConfig;
import com.amway.lms.backend.entity.Category;
import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.entity.Location;
import com.amway.lms.backend.entity.Room;
import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.entity.SessionParticipant;
import com.amway.lms.backend.entity.Survey;
import com.amway.lms.backend.entity.SurveySent;
import com.amway.lms.backend.entity.SurveySentEmail;
import com.amway.lms.backend.entity.Test;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.EmailSendingException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.AcceptCourse;
import com.amway.lms.backend.model.CourseInformation;
import com.amway.lms.backend.model.EmailInfor;
import com.amway.lms.backend.model.Employee;
import com.amway.lms.backend.model.GeneralInformation;
import com.amway.lms.backend.model.LineManager;
import com.amway.lms.backend.model.LocationAndTime;
import com.amway.lms.backend.repository.CategoryRepository;
import com.amway.lms.backend.repository.CourseRepository;
import com.amway.lms.backend.repository.LocationRepository;
import com.amway.lms.backend.repository.RoomRepository;
import com.amway.lms.backend.repository.SessionParticipantRepository;
import com.amway.lms.backend.repository.SessionRepository;
import com.amway.lms.backend.repository.SurveyRepository;
import com.amway.lms.backend.repository.SurveySentEmailRepository;
import com.amway.lms.backend.repository.SurveySentRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.EmailService;
import com.amway.lms.backend.service.PostTrainingSurveyParticipantService;
import com.amway.lms.backend.service.PostTrainingSurveyTrainerService;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Action;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Clazz;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Duration;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Sequence;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.model.property.XProperty;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    private static final String EMAIL_TEMPLATE_CLASSPATH_RES = "classpath:mail/template/default.html";

    private static final String EMAIL_TEMPLATE_SEND_EMAIL_RES = "classpath:mail/template/accpetDenyEmail.html";

    private static final String EMAIL_TEMPLATE_SEND_EMAIL_COORDINATOR = "classpath:mail/template/emailToCoordinator.html";

    private static final String EMAIL_TEMPLATE_SEND_EMAIL_CHANGE_SESSION = "classpath:mail/template/informChangeSession.html";

    private static final String EMAIL_TEMPLATE_SEND_EMAIL_TRAINEE_PLAN = "classpath:mail/template/emailToTraineePlan.html";

    private static final String EMAIL_TEMPLATE_SEND_EMAIL_TRAINER = "classpath:mail/template/emailToTrainer.html";

    private static final String EMAIL_TEMPLATE_SEND_EMAIL_TAKE_QUIZ = "classpath:mail/template/emailTakeQuiz.html";

    private static final String EMAIL_TEMPLATE_SEND_EMAIL_POST_SURVEY_TRAINEE = "classpath:mail/template/postSurveyTrainee.html";

    private static final String EMAIL_TEMPLATE_SEND_EMAIL_POST_SURVEY_TRAINER = "classpath:mail/template/postSurveyTrainer.html";

    private static final String EMAIL_TEMPLATE_SEND_EMAIL_TEST = "classpath:mail/template/emailTakeTest.html";

    private static final String AMWAY_LOGO_IMAGE = "mail/template/images/amway-logo.png";

    private static final String AMWAY_SURVEY_IMAGE = "mail/template/images/amway-survey.png";

    private static final String PNG_MIME = "image/png";

    private static final String FILE_NAME_CALENDAR = "invite.ics";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    EmailHepler emailHepler;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    private SessionParticipantRepository sessionParticipantRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SurveySentRepository surveySentRepository;

    @Autowired
    private SurveySentEmailRepository surveySentEmailRepository;

    @Autowired
    private PostTrainingSurveyParticipantService postTrainingSurveyParticipantService;

    @Autowired
    private PostTrainingSurveyTrainerService postTrainingSurveyTrainerService;

    @Override
    public ResponseEntity<?> sendEmailForSurvey(EmailInfor emailInfor) throws IOException {
        Survey survey = surveyRepository.getSurveyById(emailInfor.getSurveyId());
        if (survey == null)
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_SURVEY_NOT_AVAILABLE_EXCEPTION,
                    ErrorCode.MSG_SURVEY_NOT_AVAILABLE_EXCEPTION);
        try {
            MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
            Context ctx = new Context();

            ctx.setVariable("emailContents", emailInfor.getHtmlContent());

            message.setFrom("admin@amway.com");
            message.setSubject(emailInfor.getSubject());
            List<String> emails = emailInfor.getEmails();
            message.setTo(emails.toArray(new String[emails.size()]));
            String output = htmlTemplateEngine.process(getEmailTemplateByName(EMAIL_TEMPLATE_CLASSPATH_RES), ctx);
            message.setText(output, true /* isHtml */);

            message.addInline("amway-logo", new ClassPathResource(AMWAY_LOGO_IMAGE), PNG_MIME);
            message.addInline("amway-survey", new ClassPathResource(AMWAY_SURVEY_IMAGE), PNG_MIME);
            this.javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            return Utils.generateFailureResponseEntity(700, "Invalid email address");
        }

        survey.setIsSent(1);
        surveyRepository.editSurvey(survey);

        // add SurveySent
        SurveySent surveySent = new SurveySent();
        surveySent.setHtmlContent(emailInfor.getHtmlContent());
        surveySent.setLinkSurvey(emailInfor.getLinkSurvey());
        surveySent.setSubject(emailInfor.getSubject());
        surveySent.setSentTime(Utils.getCurrentTime());
        surveySent.setSurveyId(emailInfor.getSurveyId());
        surveySent.setIsRemind(0);
        surveySentRepository.addSurveySent(surveySent);

        // add survey sent email
        for (String email : emailInfor.getEmails()) {
            SurveySentEmail surveySentEmail = new SurveySentEmail();
            surveySentEmail.setEmail(email);
            surveySentEmail.setSurveySentId(surveySent.getId());
            surveySentEmailRepository.addSurveySentEmail(surveySentEmail);
        }
        return Utils.generateSuccessResponseEntity("The Servey already send");

    }

    public String getEmailTemplate() throws IOException {
        final Resource templateResource = this.applicationContext.getResource(EMAIL_TEMPLATE_CLASSPATH_RES);
        final InputStream inputStream = templateResource.getInputStream();
        return IOUtils.toString(inputStream, MailConfig.EMAIL_TEMPLATE_ENCODING);
    }

    public String getEmailTemplateByName(String templateName) throws IOException {
        final Resource templateResource = this.applicationContext.getResource(templateName);
        final InputStream inputStream = templateResource.getInputStream();
        return IOUtils.toString(inputStream, MailConfig.EMAIL_TEMPLATE_ENCODING);
    }

    @Override
    public ResponseEntity<?> getEmailTemplate(String emailTemplate) throws IOException {
        return Utils.generateSuccessResponseEntity(getEmailTemplate());
    }

    @Override
    public void sendEmail(EmailInfor emailInfor) throws IOException, MessagingException {
        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        final Context ctx = new Context();
        final String output = htmlTemplateEngine.process(emailInfor.getHtmlContent(), ctx);
        List<String> emails = emailInfor.getEmails();
        message.setTo(emails.toArray(new String[emails.size()]));
        message.setFrom("admin@amway.com");
        message.setSubject(emailInfor.getSubject());
        message.setText(output, true /* isHtml */);
        this.javaMailSender.send(mimeMessage);

    }

    private CourseInformation getCocourseInformation(int sessionId) throws ObjectNotFoundException {
        CourseInformation courseInformation = new CourseInformation();

        GeneralInformation generalInformation = new GeneralInformation();

        Session session = sessionRepository.getSessionById(sessionId);
        if (session == null) {
            throw new ObjectNotFoundException(ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);

        }
        Course course = courseRepository.getCourseById(session.getCourseId());
        if (course == null) {
            throw new ObjectNotFoundException(ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);

        }
        courseInformation.setCourseId(course.getId());

        Category category = categoryRepository.getCategoryById(course.getCategoryId());
        Location location = locationRepository.getLocationById(session.getLocationId());
        Room room = roomRepository.findRoombyId(session.getRoomId());
        generalInformation.setCourseName(course.getName());
        generalInformation.setBriefDescription(course.getDescription());
        generalInformation.setCategory(category != null ? category.getName() : "");
        generalInformation.setLecture(session.getTrainerFullName());
        generalInformation.setAmount(course.getNumberOfSession());

        courseInformation.setGeneralInformation(generalInformation);
        LocationAndTime locationAndTime = new LocationAndTime();

        locationAndTime.setLocation(location != null ? location.getName() : "");
        locationAndTime.setRoom(room != null ? room.getName() : "");
        locationAndTime.setStartTime(Utils.convertDateToString(session.getStartTime()));
        locationAndTime.setEndTime(Utils.convertDateToString(session.getEndTime()));
        courseInformation.setLocationAndTime(locationAndTime);

        return courseInformation;
    }

    @Override
    public ResponseEntity<?> acceptDenyByEmail(List<Employee> employees, Integer sessionId)
            throws EmailSendingException {
        try {
            // Add participant into submitted list
            for (Employee employee : employees) {
                SessionParticipant sessionParticipant = sessionParticipantRepository
                        .getSessionParticipant(employee.getSessionId(), employee.getEmployeeId());
                if (sessionParticipant == null) {
                    sessionParticipantRepository.addSessionParticipantSentMail(sessionId, employee.getEmployeeId());
                    // this.sendEmailToTrainee(employee, sessionId);
                } else {
                    sessionParticipant.setConfirmationStatus(1);
                    sessionParticipantRepository.updateSessionParticipant(sessionParticipant);
                    // this.sendEmailToTrainee(employee, sessionId);
                }
            }
            ArrayList<LineManager> lineManagers = new ArrayList<>(getManagers(employees, sessionId));
            logger.debug("-----acceptDenyByEmail - lineManagers: " + Utils.objectToJsonString(lineManagers));

            for (LineManager lineManager : lineManagers) {
                MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
                Context ctx = getContext(sessionId);

                message.setFrom("renton.enclave@gmail.com");
                message.setSubject(emailHepler.getSubjectToManager());
                message.setTo(lineManager.getEmailAdrress());
                List<Employee> trainees = lineManager.getTrannees();

                ctx.setVariable("trainees", trainees);

                String output = htmlTemplateEngine.process(getEmailTemplateByName(EMAIL_TEMPLATE_SEND_EMAIL_RES), ctx);

                message.setText(output, true /* isHtml */);

                message.addInline("amway-logo", new ClassPathResource(AMWAY_LOGO_IMAGE), PNG_MIME);
                message.addInline("amway-survey", new ClassPathResource(AMWAY_SURVEY_IMAGE), PNG_MIME);
                this.javaMailSender.send(mimeMessage);
            }
            return Utils.generateSuccessResponseEntity("The Email already send");
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmailSendingException("Email exception");
        }
    }

    private List<LineManager> getManagers(List<Employee> employees, Integer sessionId)
            throws Exception {
        List<LineManager> managers = new LinkedList<>();
        List<Integer> lstEmployeeId = new LinkedList<>();
        Set<Integer> lstLineMnagerId = new HashSet<>();
        // Get listId ManagerBy userId
        for (Employee employee : employees) {

            User userEntity = userRepository.getUserById(employee.getEmployeeId());
            if (userEntity == null) {
                continue;
            }

            if (userEntity.getRoleId() == Roles.HOD.getIntValue()) {
                logger.info("This user is HOD, the email will sent direct to HOD, id =" + userEntity.getId());
                this.sendEmailToTrainee(employee, sessionId);
                continue;
            } else if (userEntity.getRoleId() == Roles.APPROVAL_MANAGER.getIntValue()) {
                User hod = userRepository.getHodOfUserByUserId(userEntity.getId());
                if (hod == null) {
                    logger.info("User id = " + userEntity.getId() + " is AM but don't have HOD");
                    continue;
                }
                lstLineMnagerId.add(hod.getId());
                lstEmployeeId.add(employee.getEmployeeId());
            } else {
                User approvalManager = userRepository.getUserById(userEntity.getApprovalManagerId());
                if (approvalManager == null) {
                    logger.info("User id = " + userEntity.getId() + " is Trainee but don't have Approval Manager");
                    continue;
                }
                lstLineMnagerId.add(approvalManager.getId());
                lstEmployeeId.add(employee.getEmployeeId());
            }
        }

        for (Integer managerId : lstLineMnagerId) {
            LineManager lineManager = new LineManager();
            User lineManagerEntity = userRepository.getUserById(managerId);
            lineManager.setEmailAdrress(lineManagerEntity.getEmail());
            lineManager.setId(managerId);
            List<User> userEntities = null;

            if (lineManagerEntity.getRoleId() == Roles.HOD.getIntValue()) {
                userEntities = userRepository.getListApprovalManagerByHODId(managerId);
                logger.debug("----getListApprovalManagerByHODId: " + Utils.objectToJsonString(userEntities));
            } else
                userEntities = userRepository.getListUserByApprovalMangerId(managerId);

            int id = 1;
            for (User user : userEntities) {
                if (lstEmployeeId.contains(user.getId())) {
                    Employee employee = new Employee();
                    String fullName = new StringBuilder(user.getFirstName()).append(" ").append(user.getLastName())
                            .toString();
                    employee.setFullName(fullName);
                    employee.setEmployeeId(id);
                    // convert to json before encrtypt
                    String jsonString = jsonStringAcceptDeny(sessionId, user.getId());
                    // Encrypt
                    String acceptDenyLinkEncrypt = Utils.encrypt(jsonString);
                    String linkAccept = new StringBuffer(emailHepler.getLinkAccept()).append(acceptDenyLinkEncrypt)
                            .toString();
                    String linkDeny = new StringBuffer(emailHepler.getLinkDeny()).append(acceptDenyLinkEncrypt)
                            .toString();
                    employee.setLinkAccept(linkAccept);
                    employee.setLinkDeny(linkDeny);
                    lineManager.getTrannees().add(employee);
                    id++;
                }
            }
            managers.add(lineManager);
        }
        return managers;
    }

    @Override
    @Async
    public void sendEmailToTrainee(Employee employee, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        Context ctx = getContext(sessionId);

        message.setFrom("admin@amway.com");
        message.setSubject(emailHepler.getSubjectTotrainee());

        String output;
        String jsonString = jsonStringAcceptDeny(sessionId, employee.getEmployeeId());
        // Encrypt
        String acceptDenyLinkEncrypt = Utils.encrypt(jsonString);
        String linkAccept = new StringBuffer(emailHepler.getLinkTrainneeAccept()).append(acceptDenyLinkEncrypt)
                .toString();
        String linkDeny = new StringBuffer(emailHepler.getLinkTraineeDeny()).append(acceptDenyLinkEncrypt).toString();
        ctx.setVariable("linkAccept", linkAccept);
        ctx.setVariable("linkDeny", linkDeny);
        output = htmlTemplateEngine.process(getEmailTemplateByName(EMAIL_TEMPLATE_SEND_EMAIL_TRAINEE_PLAN), ctx);
        message.setTo(employee.getEmailAddress());
        message.setText(output, true /* isHtml */);
        message.addInline("amway-logo", new ClassPathResource(AMWAY_LOGO_IMAGE), PNG_MIME);
        message.addInline("amway-survey", new ClassPathResource(AMWAY_SURVEY_IMAGE), PNG_MIME);

        message.addAttachment(FILE_NAME_CALENDAR, new ByteArrayDataSource(
                getEmailCalendar(sessionId, mimeMessage).toString(), "text/calendar;method=REQUEST"));

        this.javaMailSender.send(mimeMessage);
    }

    @Override
    @Async
    public void sendEmailCoordinator(Employee employee, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException {
        logger.info("---------sendEmailCoordinator--------------"+sessionId);

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        Context ctx = getContext(sessionId);
        message.setFrom("admin@amway.com");
        message.setSubject(emailHepler.getSubjectTotrainee());

        // Save into sessionParticipantRepository
        // sessionParticipantRepository.addSessionParticipantFromSession(sessionId,
        // employee.getEmployeeId());

        String output = htmlTemplateEngine.process(getEmailTemplateByName(EMAIL_TEMPLATE_SEND_EMAIL_COORDINATOR), ctx);
        logger.debug("-------------Coordinator: " + employee.getEmailAddress());
        message.setTo(employee.getEmailAddress());
        message.setText(output, true /* isHtml */);
        message.addInline("amway-logo", new ClassPathResource(AMWAY_LOGO_IMAGE), PNG_MIME);
        message.addInline("amway-survey", new ClassPathResource(AMWAY_SURVEY_IMAGE), PNG_MIME);
        this.javaMailSender.send(mimeMessage);

    }

    @Override
    public void sendEmailChangeInfoSession(Employee employee, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException {

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        Context ctx = getContext(sessionId);
        message.setFrom("admin@amway.com");
        message.setSubject(emailHepler.getSubjectChangeSessionInfor());
        // Encrypt
        String output = htmlTemplateEngine.process(getEmailTemplateByName(EMAIL_TEMPLATE_SEND_EMAIL_CHANGE_SESSION),
                ctx);
        message.setTo(employee.getEmailAddress());
        message.setText(output, true /* isHtml */);
        message.addInline("amway-logo", new ClassPathResource(AMWAY_LOGO_IMAGE), PNG_MIME);
        message.addInline("amway-survey", new ClassPathResource(AMWAY_SURVEY_IMAGE), PNG_MIME);
        this.javaMailSender.send(mimeMessage);

    }

    @Override
    @Async
    public void sendEmailChangeInfoSession(String[] emails, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        Context ctx = getContext(sessionId);
        message.setFrom("admin@amway.com");
        message.setSubject(emailHepler.getSubjectChangeSessionInfor());
        // Encrypt
        String output = htmlTemplateEngine.process(getEmailTemplateByName(EMAIL_TEMPLATE_SEND_EMAIL_CHANGE_SESSION),
                ctx);
        message.setTo(emails);
        message.setText(output, true /* isHtml */);
        message.addInline("amway-logo", new ClassPathResource(AMWAY_LOGO_IMAGE), PNG_MIME);
        message.addInline("amway-survey", new ClassPathResource(AMWAY_SURVEY_IMAGE), PNG_MIME);
        this.javaMailSender.send(mimeMessage);
    }

    private String jsonStringAcceptDeny(int sessionId, int userId) {
        AcceptCourse acceptCourse = new AcceptCourse();
        acceptCourse.setSessionId(sessionId);
        acceptCourse.setUserId(userId);
        String jsonString = "";
        try {
            jsonString = Utils.ObjectToJsonString(acceptCourse);
            return jsonString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;

    }

    @Override
    @Async
    public ResponseEntity<?> accepetCourseByEmail(String encrtyptStr) throws Exception {
        try {
            String jsonStringDecrypt = Utils.decrypt(encrtyptStr);
            AcceptCourse acceptCourse = Utils.JsonStringToObject(jsonStringDecrypt);
            SessionParticipant sessionParticipant = sessionParticipantRepository
                    .getSessionParticipant(acceptCourse.getSessionId(), acceptCourse.getUserId());
            if (sessionParticipant == null) {
                sessionParticipantRepository.addSessionParticipantFromSession(acceptCourse.getSessionId(),
                        acceptCourse.getUserId());
            }
            this.sendEmailToTrainee(this.getEmployee(acceptCourse.getUserId()), acceptCourse.getSessionId());
            SessionParticipant sessionParticipantUpdate = sessionParticipantRepository
                    .getSessionParticipant(acceptCourse.getSessionId(), acceptCourse.getUserId());
            sessionParticipantUpdate.setManagerActionStatus(ManagerActionStatus.ACCEPTED.getValue());
            sessionParticipantRepository.updateSessionParticipant(sessionParticipantUpdate);
            return Utils.generateSuccessResponseEntity("Accept Course ");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Email Exception");
        }
    }

    @Override
    public ResponseEntity<?> denyCourseByEmail(String encrtyptStr) throws Exception {
        String jsonStringDecrypt = Utils.decrypt(encrtyptStr);
        AcceptCourse acceptCourse = Utils.JsonStringToObject(jsonStringDecrypt);
        SessionParticipant sessionParticipant = sessionParticipantRepository
                .getSessionParticipant(acceptCourse.getSessionId(), acceptCourse.getUserId());
        if (sessionParticipant == null) {
            sessionParticipantRepository.addSessionParticipantFromSession(acceptCourse.getSessionId(),
                    acceptCourse.getUserId());
        }
        SessionParticipant sessionParticipantUpdate = sessionParticipantRepository
                .getSessionParticipant(acceptCourse.getSessionId(), acceptCourse.getUserId());
        sessionParticipantUpdate.setManagerActionStatus(ManagerActionStatus.DENIED.getValue());
        sessionParticipantRepository.updateSessionParticipant(sessionParticipantUpdate);
        return Utils.generateSuccessResponseEntity("Denied Course ");
    }

    @Override
    public ResponseEntity<?> traineeAcceptCourse(String encrtyptStr) throws Exception {
        String jsonStringDecrypt = Utils.decrypt(encrtyptStr);

        System.err.println("jsonStringDecrypt: " + jsonStringDecrypt);

        AcceptCourse acceptCourse = Utils.JsonStringToObject(jsonStringDecrypt);
        SessionParticipant sessionParticipant = sessionParticipantRepository
                .getSessionParticipant(acceptCourse.getSessionId(), acceptCourse.getUserId());
        // sessionParticipant.setConfirmationStatus(CourseStatus.ACCEPTED.getValue());
        sessionParticipant.setCompletionStatus(AmwayEnum.CompletionStatus.ACCEPTED.getValue());
        sessionParticipantRepository.updateSessionParticipant(sessionParticipant);

        System.out
                .println("SessionParticipant after apcept: " + Utils.generateSuccessResponseString(sessionParticipant));

        return Utils.generateSuccessResponseEntity("Accept Course ");
    }

    @Override
    public ResponseEntity<?> traineeDenyCourse(String encrtyptStr) throws Exception {
        String jsonStringDecrypt = Utils.decrypt(encrtyptStr);
        AcceptCourse acceptCourse = Utils.JsonStringToObject(jsonStringDecrypt);

        SessionParticipant sessionParticipant = sessionParticipantRepository
                .getSessionParticipant(acceptCourse.getSessionId(), acceptCourse.getUserId());
        // sessionParticipant.setConfirmationStatus(CourseStatus.DENIED.getValue());
        sessionParticipant.setCompletionStatus(AmwayEnum.CompletionStatus.DENIED.getValue());
        sessionParticipantRepository.updateSessionParticipant(sessionParticipant);
        return Utils.generateSuccessResponseEntity("Denied Course ");
    }

    private Employee getEmployee(int userId) {
        User user = userRepository.getUserById(userId);
        Employee employee = new Employee();
        employee.setEmailAddress(user.getEmail());
        employee.setEmployeeId(userId);
        return employee;

    }

    private Context getContext(int sessionId) throws ObjectNotFoundException {
        Context ctx = new Context();
        CourseInformation courseInformation = getCocourseInformation(sessionId);
        Session session = sessionRepository.getSessionById(sessionId);
        ctx.setVariable("courseName", courseInformation.getGeneralInformation().getCourseName());
        ctx.setVariable("courseNameAndSession",
                courseInformation.getGeneralInformation().getCourseName() + "(" + session.getName() + ")");
        ctx.setVariable("category", courseInformation.getGeneralInformation().getCategory());
        ctx.setVariable("trainer", courseInformation.getGeneralInformation().getLecture());
        //
        ctx.setVariable("location", courseInformation.getLocationAndTime().getLocation());
        ctx.setVariable("room", courseInformation.getLocationAndTime().getRoom());
        ctx.setVariable("date", Utils.convertDateToStringTrainingAction(session.getStartTime()));
        ctx.setVariable("startTime",
                Common.getStringDate(session.getStartTime(), Common.DATE_TIME_FORMAT_DD_MMM_YYYY_AM_PM));
        ctx.setVariable("endTime",
                Common.getStringDate(session.getEndTime(), Common.DATE_TIME_FORMAT_DD_MMM_YYYY_AM_PM));
        return ctx;

    }

    public Calendar getEmailCalendar(int sessionId, MimeMessage mimeMessage) {
        try {
            Calendar calendar = new Calendar();

            Session session = sessionRepository.getSessionById(sessionId);
            CourseInformation courseInformation = getCocourseInformation(sessionId);

            if (session == null)
                return calendar;

            mimeMessage.addHeaderLine("method=REQUEST");
            mimeMessage.addHeaderLine("charset=UTF-8");
            mimeMessage.addHeaderLine("component=VEVENT");

            // TimeZoneRegistry registry =
            // TimeZoneRegistryFactory.getInstance().createRegistry();
            // TimeZone timezone = registry.getTimeZone("Asia/Saigon");
            // VTimeZone tz = timezone.getVTimeZone();

            PropertyList calendarProperties = calendar.getProperties();
            calendarProperties.add(new ProdId("-//Microsoft Corporation//Outlook 12.0 MIMEDIR//EN"));
            calendarProperties.add(Version.VERSION_2_0);
            calendarProperties.add(Method.REQUEST);
            calendarProperties.add(CalScale.GREGORIAN);
            calendarProperties.add(new XProperty("X-MS-OLK-FORCEINSPECTOROPEN", "TRUE"));

            VEvent event = new VEvent(new DateTime(session.getStartTime().getTime()),
                    new DateTime(session.getEndTime().getTime()), "Session Time");
            // event.getProperties().add(tz.getTimeZoneId());
            event.getProperties()
                    .add(new net.fortuna.ical4j.model.property.Location(
                            courseInformation.getLocationAndTime().getLocation() + " - Room: "
                                    + courseInformation.getLocationAndTime().getRoom()));
            // event.getProperties().add(new Description("Description"));
            event.getProperties().add(new Organizer("mailto:admin@amway.com"));
            event.getProperties().add(Priority.MEDIUM);
            event.getProperties().add(Clazz.PUBLIC);
            event.getProperties().add(new Sequence());
            // event.getProperties().add(new
            // UidGenerator("ssid:"+String.valueOf(session.getId())).generateUid());
            event.getProperties().add(new Uid("ssid:" + session.getId()));
            event.getProperties().add(new XProperty("X-MICROSOFT-CDO-BUSYSTATUS", "TENTATIVE"));
            event.getProperties().add(new XProperty("X-MICROSOFT-CDO-IMPORTANCE", "1"));
            event.getProperties().add(new XProperty("X-MICROSOFT-CDO-INTENDEDSTATUS", "BUSY"));
            event.getProperties().add(new XProperty("X-MICROSOFT-DISALLOW-COUNTER", "FALSE"));
            event.getProperties().add(new XProperty("X-MS-OLK-ALLOWEXTERNCHECK", "TRUE"));
            event.getProperties().add(new XProperty("X-MS-OLK-AUTOSTARTCHECK", "FALSE"));
            event.getProperties().add(new XProperty("X-MS-OLK-CONFTYPE", "0"));

            VAlarm alarm = new VAlarm();
            alarm.getProperties().add(new Duration(new Dur(0, 0, -15, 0)));
            alarm.getProperties().add(Action.DISPLAY);
            alarm.getProperties().add(new Description("Reminder"));

            event.getAlarms().add(alarm);
            calendar.getComponents().add(event);

            return calendar;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("--sentEmailToRemind: Cannot get calendar");
            return null;
        }

    }

    @Override
    @Async
    public void sendEmailToTrainer(Employee employee, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException {
        logger.info("---------sendEmailToTrainer--------------");
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        Context ctx = getContext(sessionId);

        message.setFrom("admin@amway.com");
        message.setSubject(emailHepler.getSubjectTotrainee());
        String output;
        output = htmlTemplateEngine.process(getEmailTemplateByName(EMAIL_TEMPLATE_SEND_EMAIL_TRAINER), ctx);
        message.setTo(employee.getEmailAddress());
        message.setText(output, true /* isHtml */);
        message.addInline("amway-logo", new ClassPathResource(AMWAY_LOGO_IMAGE), PNG_MIME);
        message.addInline("amway-survey", new ClassPathResource(AMWAY_SURVEY_IMAGE), PNG_MIME);

        message.addAttachment(FILE_NAME_CALENDAR, new ByteArrayDataSource(
                getEmailCalendar(sessionId, mimeMessage).toString(), "text/calendar;method=REQUEST"));

        this.javaMailSender.send(mimeMessage);

    }

    @Override
    @Async
    public void sendEmailToTraineeForQuiz(Employee employee, Integer sessionParticipantId)
            throws MessagingException, IOException, ObjectNotFoundException {
        logger.info("sendEmailToTraineeForQuiz(): start");
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");

        SessionParticipant participant = sessionParticipantRepository.getSessionParticipantById(sessionParticipantId);

        Context ctx = getContext(participant.getSessionId());
        message.setFrom("admin@amway.com");
        message.setSubject("Quiz Invitation");

        String linkTakeQuiz = new StringBuffer(emailHepler.getLinkTakeQuiz()).append(sessionParticipantId).toString();
        // logger.debug("Link take quiz: "+linkTakeQuiz);
        ctx.setVariable("linkTakeQuiz", linkTakeQuiz);

        String output = htmlTemplateEngine.process(getEmailTemplateByName(EMAIL_TEMPLATE_SEND_EMAIL_TAKE_QUIZ), ctx);
        // logger.debug("OUT PUT: "+output);
        message.setTo(employee.getEmailAddress());
        message.setText(output, true /* isHtml */);
        message.addInline("amway-logo", new ClassPathResource(AMWAY_LOGO_IMAGE), PNG_MIME);
        message.addInline("amway-survey", new ClassPathResource(AMWAY_SURVEY_IMAGE), PNG_MIME);
        this.javaMailSender.send(mimeMessage);
        logger.info("sendEmailToTraineeForQuiz(): end");
    }

    @Override
    @Async
    public void sendEmailToTrainerForPostSurvey(Employee trainer, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException {
        logger.info("sendEmailToTrainerForPostSurvey(): start");
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");

        Context ctx = getContext(sessionId);
        message.setFrom("admin@amway.com");
        message.setSubject("Post Survey Trainer");

        String linkTakeSurveyTrainer = new StringBuffer().append(emailHepler.getLinkPostSurveyTrainer())
                .append(postTrainingSurveyTrainerService.encodePostSurveyTrainer(sessionId, trainer.getEmployeeId()))
                .toString();

        ctx.setVariable("linkTakeSurveyTrainer", linkTakeSurveyTrainer);

        String output = htmlTemplateEngine
                .process(getEmailTemplateByName(EMAIL_TEMPLATE_SEND_EMAIL_POST_SURVEY_TRAINER), ctx);

        message.setTo(trainer.getEmailAddress());
        message.setText(output, true /* isHtml */);
        message.addInline("amway-logo", new ClassPathResource(AMWAY_LOGO_IMAGE), PNG_MIME);
        message.addInline("amway-survey", new ClassPathResource(AMWAY_SURVEY_IMAGE), PNG_MIME);
        this.javaMailSender.send(mimeMessage);
        logger.info("sendEmailToTrainerForPostSurvey(): end");

    }

    @Override
    @Async
    public void sendEmailToTraineeForPostSurvey(Employee trainee, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException {
        logger.info("sendEmailToTraineeForPostSurvey(): start");
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");

        Context ctx = getContext(sessionId);
        message.setFrom("admin@amway.com");
        message.setSubject("Post Survey Trainee");

        String linkTakeSurveyTrainee = new StringBuffer().append(emailHepler.getLinkPostSurveyTrainee()).append(
                postTrainingSurveyParticipantService.encodePostSurveyParticipant(sessionId, trainee.getEmployeeId()))
                .toString();
        ctx.setVariable("linkTakeSurveyTrainee", linkTakeSurveyTrainee);

        String output = htmlTemplateEngine
                .process(getEmailTemplateByName(EMAIL_TEMPLATE_SEND_EMAIL_POST_SURVEY_TRAINEE), ctx);
        message.setTo(trainee.getEmailAddress());
        message.setText(output, true /* isHtml */);
        message.addInline("amway-logo", new ClassPathResource(AMWAY_LOGO_IMAGE), PNG_MIME);
        message.addInline("amway-survey", new ClassPathResource(AMWAY_SURVEY_IMAGE), PNG_MIME);
        this.javaMailSender.send(mimeMessage);
        logger.info("sendEmailToTraineeForPostSurvey(): end");

    }

    @Override
    public void sendEmailForTest(String[] to, Test test, String urlEncode) throws Exception {
        // TODO Auto-generated method stub
        logger.info("sendEmailTest(): start");
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");

        Context ctx = getContextForEmailTest(test, urlEncode);
        message.setFrom("admin@amway.com");
        message.setSubject("Test Invitation");

        String output = htmlTemplateEngine.process(getEmailTemplateByName(EMAIL_TEMPLATE_SEND_EMAIL_TEST), ctx);

        message.setTo(to);
        message.setText(output, true /* isHtml */);
        message.addInline("amway-logo", new ClassPathResource(AMWAY_LOGO_IMAGE), PNG_MIME);
        message.addInline("amway-survey", new ClassPathResource(AMWAY_SURVEY_IMAGE), PNG_MIME);
        this.javaMailSender.send(mimeMessage);
    }

    private Context getContextForEmailTest(Test test, String urlEncode) {
        Context ctx = new Context();
        ctx.setVariable("testTitle", test.getTitle());
        ctx.setVariable("endDate", Common.getStringDate(test.getEndingTime(), Common.DATE_FORMAT_DD_MMM_YYYY));
        ctx.setVariable("linkTakeTest",
                new StringBuffer().append(emailHepler.getLinkTakeTest()).append(urlEncode).toString());
        return ctx;
    }

}
