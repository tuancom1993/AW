package com.amway.lms.backend.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amway.lms.backend.common.AmwayEnum;
import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Message;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.entity.Department;
import com.amway.lms.backend.entity.PostTrainingSurveyParticipant;
import com.amway.lms.backend.entity.Quiz;
import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.entity.SessionParticipant;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.QuizNotAvailableException;
import com.amway.lms.backend.model.Employee;
import com.amway.lms.backend.model.ParticipantCompletionStatus;
import com.amway.lms.backend.repository.CourseRepository;
import com.amway.lms.backend.repository.DepartmentRepository;
import com.amway.lms.backend.repository.PostTrainingSurveyParticipantRepository;
import com.amway.lms.backend.repository.QuizRepository;
import com.amway.lms.backend.repository.SessionParticipantRepository;
import com.amway.lms.backend.repository.SessionRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.EmailService;
import com.amway.lms.backend.service.SessionParticipantService;

/**
 * @author: Hung (Charles) V. PHAM
 */

@Service
@Transactional
public class SessionParticipantServiceImpl implements SessionParticipantService {
    private static final Logger logger = LoggerFactory.getLogger(SessionParticipantServiceImpl.class);

    @Autowired
    UserRepository userDao;

    @Autowired
    SessionParticipantRepository sessionParticipantDao;

    @Autowired
    DepartmentRepository departmentDao;

    @Autowired
    SessionRepository sessionDao;

    @Autowired
    CourseRepository courseDao;

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    PostTrainingSurveyParticipantRepository postTrainingSurveyParticipantRepository;

    @Override
    public ResponseEntity<?> addSessionPaticipant(int sessionId, List<User> users) {
        try {
            // get List of Users to return
            List<User> usersReturn = new ArrayList<User>();
            for (int i = 0; i < users.size(); i++) {
                int userId = users.get(i).getId();
                User user = this.userDao.getUserById(userId);
                if (user == null)
                    throw new ObjectNotFoundException(Message.MSG_USER_NOT_FOUND + " userId = " + userId);
                this.sessionParticipantDao.addSessionParticipantFromSession(sessionId, userId);
                usersReturn.add(user);
            }
            return Utils.generateSuccessResponseEntity(usersReturn);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addSessionPaticipant " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> importParticipant(MultipartFile participantFile, int sessionId)
            throws IOException, Exception {
        try {

            if (participantFile == null)
                throw new ObjectNotFoundException(
                        Message.MSG_SESSION_PARTICIPANT_NOT_FOUND + " sessionId = " + sessionId);
            logger.debug("========================Participant file name: " + participantFile.getOriginalFilename()
                    + " | Size: " + (double) participantFile.getSize() / 1000 + "KB");
            String extension = Common.getExtension(participantFile.getOriginalFilename());
            Workbook workbook = getWorkbook(participantFile.getInputStream(), extension);
            logger.debug("========================Sheet name: " + workbook.getSheetAt(0).getSheetName());
            // Get First sheet from the workbook
            Sheet firstSheet = workbook.getSheetAt(0);
            // Iterate start from the first sheet of the uploaded excel file
            Iterator<Row> rowIterator = firstSheet.iterator();
            SessionParticipant sessionParticipant = new SessionParticipant();
            User user = new User();
            int count = 0;
            while (rowIterator.hasNext()) {
                if (count != 0) {
                    Row currentRow = rowIterator.next();
                    Iterator<Cell> cellIterator = currentRow.iterator();
                    while (cellIterator.hasNext()) {
                        Cell currentCell = cellIterator.next();
                        int columnIndex = currentCell.getColumnIndex();
                        switch (columnIndex) {
                        case 0:
                            user = userDao.getUserByUserID((String) getCellValue(currentCell));
                            if (user != null) {
                                // Check exist userId in session participant
                                // before insert
                                if (!sessionParticipantDao.isExistUserInSessionParticipant(sessionId, user.getId())) {
                                    sessionParticipantDao.addSessionParticipantFromPlan(sessionId, user.getId());
                                } else {
                                    // Check exist trainer in session
                                    // participant then remove
                                    List<User> userList = userDao.getUserList();
                                    for (User u : userList) {
                                        if (sessionParticipantDao.isExistTrainerInSessionParticipant(sessionId,
                                                u.getId())) {
                                            sessionParticipantDao.delSessionParticipant(sessionId, u.getId());
                                        }
                                    }
                                    continue;
                                }
                            }
                            break;
                        case 1:
                            // For maintain in future: read file with more
                            // column
                            break;
                        }
                    }
                }
                count++;
                workbook.close();
                participantFile.getInputStream().close();
            }
            return Utils.generateSuccessResponseEntity(sessionParticipant);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_PARTICIPANT_CANNOT_IMPORT_EXCEPTION,
                    ErrorCode.MSG_PARTICIPANT_CANNOT_IMPORT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> delSessionPaticipant(int sessionId, List<User> users) {
        try {
            // get List of Users to return
            List<User> usersReturn = new ArrayList<User>();
            for (int i = 0; i < users.size(); i++) {
                int userId = users.get(i).getId();
                User user = this.userDao.getUserById(userId);
                // If user is not found, throw exception
                if (user == null)
                    throw new ObjectNotFoundException(Message.MSG_USER_NOT_FOUND + " userId = " + userId);

                // If SessionParticipant is not found, throw exception
                SessionParticipant sp = this.sessionParticipantDao.getSessionParticipant(sessionId, userId);
                if (sp == null)
                    throw new ObjectNotFoundException(Message.MSG_SESSION_PARTICIPANT_NOT_FOUND);
                this.sessionParticipantDao.delSessionParticipant(sessionId, users.get(i).getId());
                usersReturn.add(user);
            }
            return Utils.generateSuccessResponseEntity(usersReturn);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION SessionParticipantServiceImpl - delCourseParticipant " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_DELETE_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getUsersBySessionId(int sessionId, Integer confirmationStatus, Integer departmentId) {
        try {

            List<User> users = this.sessionParticipantDao.getUsersBySessionId(sessionId, confirmationStatus,
                    departmentId);

            List<Employee> employees = new LinkedList<>();
            if (users == null || users.size() == 0)
                throw new ObjectNotFoundException(Message.MSG_USER_NOT_FOUND);
            for (User user : users) {
                Employee employee = new Employee();
                if (user.getDepartmentId() == 0) {
                    employee.setDepartment("N/A");
                } else {
                    departmentId = user.getDepartmentId();
                    Department department = this.departmentDao.getDepartmentById(departmentId);
                    employee.setDepartment(department.getName());
                }
                BeanUtils.copyProperties(user, employee);
                employee.setSessionId(sessionId);
                employee.setEmployeeId(user.getId());
                employee.setEmailAddress(user.getEmail());
                employee.setApprovalManagerId(user.getApprovalManagerId());
                employee.setFullName(
                        new StringBuilder(user.getFirstName()).append(" ").append(user.getLastName()).toString());
                employees.add(employee);
            }
            return Utils.generateSuccessResponseEntity(employees);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION SessionParticipantServiceImpl - getUsersBySessionId" + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getUsersAcceptedBySessionId(int sessionId) {
        try {

            List<User> users = this.sessionParticipantDao.getUsersBySessionId(sessionId, null, null);

            List<Employee> employees = new LinkedList<>();
            if (users == null || users.size() == 0)
                throw new ObjectNotFoundException(Message.MSG_USER_NOT_FOUND);
            for (User user : users) {
                Department department = this.departmentDao.getDepartmentById(user.getDepartmentId());
                Employee employee = new Employee();
                BeanUtils.copyProperties(user, employee);
                employee.setSessionId(sessionId);
                employee.setEmployeeId(user.getId());
                employee.setStaffCode(user.getStaffCode());
                employee.setEmailAddress(user.getEmail());
                employee.setDepartment(department.getName());
                employee.setJobTitle(user.getJobTitle());
                employee.setFullName(
                        new StringBuilder(user.getFirstName()).append(" ").append(user.getLastName()).toString());
                employee.setPostSurveyStatus(getPostTrainingSurveyStatus(sessionId, user.getId()));
                employees.add(employee);
            }

            return Utils.generateSuccessResponseEntity(employees);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION SessionParticipantServiceImpl - getUsersBySessionId" + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getUsersCheckInOutBySessionId(int sessionId, String name, int completionStatus) {
        try {

            List<User> users = this.userDao.getUsersCheckInOutBySessionId(sessionId, name, completionStatus);
            if (users == null || users.size() == 0)
                throw new ObjectNotFoundException(Message.MSG_USER_NOT_FOUND);
            for (User user : users) {
                SessionParticipant sessionParticipant = this.sessionParticipantDao.getSessionParticipant(sessionId,
                        user.getId());
                user.setCheckinAt(sessionParticipant.getCheckinAt());
                user.setCheckoutAt(sessionParticipant.getCheckoutAt());
            }
            return Utils.generateSuccessResponseEntity(users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION SessionParticipantServiceImpl - getUsersByCourseId" + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> userCheckIn(SessionParticipant sessionParticipant) {
        try {
            SessionParticipant updateObject = this.sessionParticipantDao
                    .getSessionParticipant(sessionParticipant.getSessionId(), sessionParticipant.getUserId());

            if (updateObject == null)
                throw new EditObjectException(Message.MSG_SESSION_PARTICIPANT_NOT_FOUND);

            // completion status = 5, 6 => can not checkin
            if ((updateObject.getCompletionStatus() == AmwayEnum.CompletionStatus.ENDED_SESSION.getValue())
                    || (updateObject.getCompletionStatus() == AmwayEnum.CompletionStatus.COMPLETED.getValue())) {
                throw new EditObjectException(Message.MSG_SESSION_PARTICIPANT_CHECK_IN_ERROR_ENDED_OR_COMPLETED);
            }

            if (sessionParticipant.getCheckinAt() == null) { // checkin time ==
                                                             // null => reset
                                                             // checkin
                if (updateObject.getCompletionStatus() == AmwayEnum.CompletionStatus.PARTICIPATED.getValue()) {
                    updateObject.setCheckinAt(sessionParticipant.getCheckinAt());
                    updateObject.setCompletionStatus(AmwayEnum.CompletionStatus.ACCEPTED.getValue());
                    this.sessionParticipantDao.updateSessionParticipant(updateObject);
                    updateAjpvForUser(sessionParticipant.getSessionId(), sessionParticipant.getUserId(), true);
                    return Utils.generateSuccessResponseEntity(updateObject);
                }
                throw new EditObjectException(Message.MSG_SESSION_PARTICIPANT_CHECK_IN_RESET_ERROR);
            } else { // checkin time !=null => process checkin
                if (updateObject.getCompletionStatus() == AmwayEnum.CompletionStatus.ACCEPTED.getValue()) {
                    updateObject.setCheckinAt(sessionParticipant.getCheckinAt());
                    updateObject.setCompletionStatus(AmwayEnum.CompletionStatus.PARTICIPATED.getValue());
                    this.sessionParticipantDao.updateSessionParticipant(updateObject);
                    updateAjpvForUser(sessionParticipant.getSessionId(), sessionParticipant.getUserId(), true);
                    return Utils.generateSuccessResponseEntity(updateObject);
                }
                throw new EditObjectException(Message.MSG_SESSION_PARTICIPANT_CHECK_IN_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION SessionParticipantServiceImpl - userCheckIn" + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    // Update AJPV for this user
    private ResponseEntity<?> updateAjpvForUser(int sessionId, int userId, boolean isCheckIn) {
        try {
            int courseId = this.sessionDao.getSessionById(sessionId).getCourseId();
            Course course = this.courseDao.getCourseById(courseId);
            if (course == null)
                throw new ObjectNotFoundException(Message.MSG_COURSE_NOT_FOUND + " courseId = " + courseId);

            User updateUser = this.userDao.getUserById(userId);
            if (updateUser == null)
                throw new EditObjectException(Message.MSG_USER_NOT_FOUND + " userId = " + userId);
            if (isCheckIn) {
                updateUser.setAjpv(updateUser.getAjpv() + course.getParticipationAJPV());
            } else {
                updateUser.setAjpv(updateUser.getAjpv() + course.getCompletionAJPV());
            }
            this.userDao.editUser(updateUser);
            return Utils.generateSuccessResponseEntity(updateUser);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION SessionParticipantServiceImpl - updateAjpvForUser" + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> userCheckOut(SessionParticipant sessionParticipant) {
        try {
            SessionParticipant updateObject = this.sessionParticipantDao
                    .getSessionParticipant(sessionParticipant.getSessionId(), sessionParticipant.getUserId());
            if (updateObject == null)
                throw new EditObjectException(Message.MSG_SESSION_PARTICIPANT_NOT_FOUND);

            Session session = this.sessionDao.getSessionById(sessionParticipant.getSessionId());
            if (sessionParticipant.getCheckoutAt() == null) { // checkout time
                                                              // == null =>
                                                              // reset checkout
                if ((updateObject.getCompletionStatus() == AmwayEnum.CompletionStatus.ENDED_SESSION.getValue())
                        || (updateObject.getCompletionStatus() == AmwayEnum.CompletionStatus.COMPLETED.getValue())) {
                    updateObject.setCheckoutAt(sessionParticipant.getCheckoutAt());
                    updateObject.setCompletionStatus(AmwayEnum.CompletionStatus.PARTICIPATED.getValue());
                    this.sessionParticipantDao.updateSessionParticipant(updateObject);
                    return Utils.generateSuccessResponseEntity(updateObject);
                }
                throw new EditObjectException(Message.MSG_SESSION_PARTICIPANT_CHECK_OUT_RESET_ERROR);
            } else { // checkout time !=null => process checkout
                if (updateObject.getCompletionStatus() == AmwayEnum.CompletionStatus.PARTICIPATED.getValue()) {
                    updateObject.setCheckoutAt(sessionParticipant.getCheckoutAt());
                    Course course = this.courseDao.getCourseById(session.getCourseId());
                    if (course.getIsOptional() == 1) {
                        // If course is optional, completion status is completed
                        updateObject.setCompletionStatus(AmwayEnum.CompletionStatus.COMPLETED.getValue());
                        updateAjpvForUser(sessionParticipant.getSessionId(), sessionParticipant.getUserId(), false);
                    } else {
                        // If course is mandatory, completion status is ended
                        // session
                        updateObject.setCompletionStatus(AmwayEnum.CompletionStatus.ENDED_SESSION.getValue());
                    }
                    // Send Email to trainee for quiz
                    if (validateSendTakeQuizEmail(updateObject))
                        this.emailService.sendEmailToTraineeForQuiz(
                                Common.getEmployee(this.userDao.getUserById(updateObject.getUserId())),
                                updateObject.getId());

                    // Send Email to trainee for post survey
                    this.emailService.sendEmailToTraineeForPostSurvey(
                            Common.getEmployee(this.userDao.getUserById(updateObject.getUserId())), session.getId());
                    this.sessionParticipantDao.updateSessionParticipant(updateObject);
                    return Utils.generateSuccessResponseEntity(updateObject);
                }
                throw new EditObjectException(Message.MSG_SESSION_PARTICIPANT_CHECK_OUT_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION SessionParticipantServiceImpl - userCheckIn" + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getUsersCheckinCheckout(int sessionId, String name) {
        try {
            List<User> users = this.userDao.getUsersCheckinCheckout(sessionId, name);
            if (users == null || users.size() == 0)
                throw new ObjectNotFoundException(Message.MSG_USER_NOT_FOUND);
            for (User user : users) {
                SessionParticipant sessionParticipant = this.sessionParticipantDao.getSessionParticipant(sessionId,
                        user.getId());
                user.setCheckinAt(sessionParticipant.getCheckinAt());
                user.setCheckoutAt(sessionParticipant.getCheckoutAt());
                user.setCompletionStatus(sessionParticipant.getCompletionStatus());
            }
            return Utils.generateSuccessResponseEntity(users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION SessionParticipantServiceImpl - getUsersByCourseId" + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public void setAcceptForTraineeCronJob() {
        logger.info("setAcceptForTraineeCronJob Is running...");
        Timestamp today = new Timestamp(System.currentTimeMillis());
        long threeDays = 3 * 24 * 60 * 60 * 1000;
        List<SessionParticipant> sessionParticipantList = this.sessionParticipantDao.getSessionParticipantByStatus();
        for (SessionParticipant sessionParticipant : sessionParticipantList) {
            try {
                int sessionId = sessionParticipant.getSessionId();
                Session session = this.sessionDao.getSessionById(sessionId);
                int courseId = session.getCourseId();
                Course course = this.courseDao.getCourseById(courseId);
                if (course.getIsDefaultApproval() == 1) {
                    if (today.getTime() > sessionParticipant.getConfirmationDate().getTime() + threeDays) {
                        sessionParticipant.setCompletionStatus(AmwayEnum.CompletionStatus.ACCEPTED.getValue());
                        this.sessionParticipantDao.updateSessionParticipant(sessionParticipant);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean validateSendTakeQuizEmail(SessionParticipant sessionParticipant) {
        try {
            Session session = sessionDao.getSessionById(sessionParticipant.getSessionId());
            if (session == null)
                throw new ObjectNotFoundException(
                        "Cannot find Session by sessionId = " + sessionParticipant.getSessionId());

            Course course = courseDao.getCourseById(session.getCourseId());
            if (course == null)
                throw new ObjectNotFoundException("Cannot find Course by sessionId = " + session.getId());

            Quiz quiz = quizRepository.getQuizByCourseId(course.getId());
            if (quiz == null)
                throw new QuizNotAvailableException("Cannot find Quiz by CourseId = " + course.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            return cell.getStringCellValue();

        case Cell.CELL_TYPE_BOOLEAN:
            return cell.getBooleanCellValue();

        case Cell.CELL_TYPE_NUMERIC:
            return cell.getNumericCellValue();
        }
        return null;
    }

    private Workbook getWorkbook(InputStream inputStream, String extension) throws IOException {
        Workbook workbook = null;

        if ("xlsx".equals(extension)) {
            workbook = new XSSFWorkbook(inputStream);
        } else if ("xls".equals(extension)) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException(Message.MSG_SESSION_PARTICIPANT_NOT_EXCEL_FORMAT_FILE);
        }

        return workbook;
    }

    private String getPostTrainingSurveyStatus(int sessionId, int userId) {
        try {
            List<PostTrainingSurveyParticipant> surveys = postTrainingSurveyParticipantRepository
                    .getPostTrainingSurveyParticipantBySessionIdAndUserId(sessionId, userId);
            if (surveys != null && !surveys.isEmpty())
                return AmwayEnum.PostSurveyStatus.SUBMITTED.getStrValue();
            else
                return AmwayEnum.PostSurveyStatus.NOT_SUBMMITTED_YET.getStrValue();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public ResponseEntity<?> getStatusList(int userId, int trainingPlanId, int year) {
        try {
            List<SessionParticipant> sessionParticipantLst = null;
            if (trainingPlanId == -1 && year == -1) {
                sessionParticipantLst = this.sessionParticipantDao.getApprovedSessionParticipantByUserId(userId);
            } else if (trainingPlanId != -1 && year != -1) {
                sessionParticipantLst = this.sessionParticipantDao.getApprovedListByUserTrainingPlanYear(userId,
                        trainingPlanId, year);
            } else if (trainingPlanId != -1 && year == -1) {
                sessionParticipantLst = this.sessionParticipantDao.getApprovedListByUserTrainingPlan(userId,
                        trainingPlanId);
            } else if (trainingPlanId == -1 && year != -1) {
                sessionParticipantLst = this.sessionParticipantDao.getApprovedListByUserYear(userId, year);
            }
            return Utils.generateSuccessResponseEntity(countComplettionStatus(sessionParticipantLst));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION SessionParticipantServiceImpl - getStatusList" + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_SQL_EXCEPTION, e.getMessage());
        }
    }

    private ParticipantCompletionStatus countComplettionStatus(List<SessionParticipant> sessionParticipantLst) {
        int waitingForResponding = 0;
        int accepted = 0;
        int denied = 0;
        int participated = 0;
        int endedSession = 0;
        int completed = 0;
        if (sessionParticipantLst != null && sessionParticipantLst.size() > 0) {
            for (SessionParticipant sessionParticipant : sessionParticipantLst) {
                if (sessionParticipant.getCompletionStatus() == AmwayEnum.CompletionStatus.WAITING_FOR_APPROVAL
                        .getValue()) {
                    waitingForResponding = waitingForResponding + 1;
                } else if (sessionParticipant.getCompletionStatus() == AmwayEnum.CompletionStatus.ACCEPTED.getValue()) {
                    accepted = accepted + 1;
                } else if (sessionParticipant.getCompletionStatus() == AmwayEnum.CompletionStatus.DENIED.getValue()) {
                    denied = denied + 1;
                } else if (sessionParticipant.getCompletionStatus() == AmwayEnum.CompletionStatus.PARTICIPATED
                        .getValue()) {
                    participated = participated + 1;
                } else if (sessionParticipant.getCompletionStatus() == AmwayEnum.CompletionStatus.ENDED_SESSION
                        .getValue()) {
                    endedSession = endedSession + 1;
                } else if (sessionParticipant.getCompletionStatus() == AmwayEnum.CompletionStatus.COMPLETED
                        .getValue()) {
                    completed = completed + 1;
                }
            }
        }
        ParticipantCompletionStatus participantCompletionStatus = new ParticipantCompletionStatus();
        participantCompletionStatus.setWaitingForResponding(waitingForResponding);
        participantCompletionStatus.setAccepted(accepted);
        participantCompletionStatus.setDenied(denied);
        participantCompletionStatus.setParticipated(participated);
        participantCompletionStatus.setEndedSession(endedSession);
        participantCompletionStatus.setCompleted(completed);
        return participantCompletionStatus;
    }
}