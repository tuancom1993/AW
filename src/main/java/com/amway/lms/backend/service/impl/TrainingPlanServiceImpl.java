package com.amway.lms.backend.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amway.lms.backend.common.AmwayEnum.ColorWithStatus;
import com.amway.lms.backend.common.AmwayEnum.SessionStatus;
import com.amway.lms.backend.configuration.CustomUserDetail;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Message;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.entity.Department;
import com.amway.lms.backend.entity.PreparatoryParticipant;
import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.entity.SessionParticipant;
import com.amway.lms.backend.entity.TrainingPlan;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.CourseOfTrainingPlanDashBoard;
import com.amway.lms.backend.model.CourseSessionStatus;
import com.amway.lms.backend.repository.CourseOfTrainingPlanRepository;
import com.amway.lms.backend.repository.DepartmentRepository;
import com.amway.lms.backend.repository.PreparatoryParticipantRepository;
import com.amway.lms.backend.repository.SessionParticipantRepository;
import com.amway.lms.backend.repository.SessionRepository;
import com.amway.lms.backend.repository.TrainingPlanRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.TrainingPlanService;

@Service
@Transactional
public class TrainingPlanServiceImpl implements TrainingPlanService {
    private static final Logger logger = LoggerFactory
            .getLogger(TrainingPlanServiceImpl.class);
    @Autowired
    private TrainingPlanRepository trainingPlanDao;

    @Autowired
    private SessionRepository sessionDao;

    @Autowired
    private SessionParticipantRepository sessionParticipantDao;

    @Autowired
    private CourseOfTrainingPlanRepository courseOfTrainingPlanDao;

    @Autowired
    private UserRepository userDao;

    @Autowired
    private DepartmentRepository departmentDao;

    @Autowired
    private PreparatoryParticipantRepository preparatoryParticipantDao;

    @Override
    public ResponseEntity<?> getCourseListByPlanAndStatus(int trainingPlanId,
            int sessionStatus) {
        try {
            List<Course> courses = this.trainingPlanDao
                    .getCourseListByPlan(trainingPlanId);
            if (courses == null || courses.size() == 0)
                throw new ObjectNotFoundException();
            for (Course course : courses) {
                int courseId = course.getId();
                List<Session> sessions = this.sessionDao
                        .getSessionsByCourseIdAndStatus(courseId, sessionStatus);
                for (Session session : sessions) {
                    session.setStatus(getSessionStatus(session.getStartTime(),
                            session.getEndTime()));
                }
                course.setSessions(sessions);
            }
            return new ResponseEntity<>(convertCourseToDashBoard(courses),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCourseListByPlanAndStatus "
                    + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> getCourseListByPlan(int trainingPlanId) {
        try {
            List<Course> courses = this.trainingPlanDao
                    .getCourseListByPlan(trainingPlanId);
            if (courses == null || courses.size() == 0)
                throw new ObjectNotFoundException();
            for (Course course : courses) {
                List<Session> sessions = this.sessionDao
                        .getSessionsByCourseId(course.getId());
                for (Session session : sessions) {
                    session.setStatus(getSessionStatus(session.getStartTime(),
                            session.getEndTime()));
                }
                course.setSessions(sessions);
            }
            return new ResponseEntity<>(convertCourseToDashBoard(courses),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCoursesList " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> addTrainingPlan(TrainingPlan trainingPlan) {
        try {
            // Add trainingPlan to DB
            this.trainingPlanDao.addTrainingPlan(trainingPlan);
            // After add to DB, get back id
            return Utils.generateSuccessResponseEntity(trainingPlan);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addTrainingPlan " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> updateTrainingPlan(TrainingPlan trainingPlan) {
        try {
            TrainingPlan trainingPlanUpdate = this.trainingPlanDao
                    .getTrainingPlanById(trainingPlan.getId());
            if (trainingPlanUpdate == null)
                throw new EditObjectException();
            trainingPlanUpdate.setPlanName(trainingPlan.getPlanName());
            trainingPlanUpdate.setIsMeasureByAJPV(trainingPlan
                    .getIsMeasureByAJPV());
            this.trainingPlanDao.updateTrainingPlan(trainingPlanUpdate);
            return Utils.generateSuccessResponseEntity(trainingPlanUpdate);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - updateTrainingPlan " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> getTrainingPlans() {
        try {
            List<TrainingPlan> trainingPlans = this.trainingPlanDao
                    .getTrainingPlans();
            if (trainingPlans == null || trainingPlans.size() == 0)
                throw new ObjectNotFoundException();
            return Utils.generateSuccessResponseEntity(trainingPlans);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getTrainingPlans " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> searchTrainingPlans(String planName) {
        try {
            List<TrainingPlan> trainingPlans = this.trainingPlanDao
                    .searchTrainingPlans(planName);
            if (trainingPlans == null || trainingPlans.size() == 0)
                throw new ObjectNotFoundException();
            return Utils.generateSuccessResponseEntity(trainingPlans);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getTrainingPlans " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> delTrainingPlans(List<TrainingPlan> trainingPlans) {
        try {
            for (int i = 0; i < trainingPlans.size(); i++) {
                int trainingPlanId = trainingPlans.get(i).getId();
                TrainingPlan trainingPlan = this.trainingPlanDao
                        .getTrainingPlanById(trainingPlanId);
                if (trainingPlan == null)
                    throw new DeleteObjectException();
                this.trainingPlanDao.delTrainingPlan(trainingPlan);
                this.courseOfTrainingPlanDao
                        .delCourseOfTrainingPlanByPlainingId(trainingPlanId);
            }
            return Utils.generateSuccessResponseEntity(trainingPlans);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getTrainingPlans " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> getTrainingPlanById(int trainingPlanId) {
        try {
            TrainingPlan trainingPlan = this.trainingPlanDao
                    .getTrainingPlanById(trainingPlanId);
            if (trainingPlan == null)
                throw new ObjectNotFoundException();
            return Utils.generateSuccessResponseEntity(trainingPlan);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getTrainingPlans " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> getUsersOfTrainingPlan(int trainingPlanId,
            int existed, Integer departmentId) {
        try {
            User userLogin = null;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetail) {
                CustomUserDetail customUserDetails = (CustomUserDetail) principal;
                userLogin = customUserDetails.getUser();
            }
            TrainingPlan trainingPlan = this.trainingPlanDao
                    .getTrainingPlanById(trainingPlanId);
            if (trainingPlan == null)
                throw new ObjectNotFoundException();
            List<User> users = this.userDao.getUsersOfTrainingPlan(
                    trainingPlanId, existed, departmentId, userLogin);
            if (users == null || users.size() == 0)
                throw new ObjectNotFoundException();

            for (int i = 0; i < users.size(); i++) {
                Department department = this.departmentDao
                        .getDepartmentById(users.get(i).getDepartmentId());
                users.get(i).setDepartment(department);
            }

            return Utils.generateSuccessResponseEntity(users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getTrainingPlans " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> addUsersToTrainingPlan(int trainingPlanId,
            List<User> users) {
        try {
            TrainingPlan trainingPlan = this.trainingPlanDao
                    .getTrainingPlanById(trainingPlanId);
            if (trainingPlan == null)
                throw new ObjectNotFoundException(
                        Message.MSG_TRAINING_PLAN_NOT_FOUND
                                + " TrainingPlanId = " + trainingPlanId);
            List<Course> courses = this.trainingPlanDao
                    .getCourseListByPlan(trainingPlanId);
            for (User user : users) {
                PreparatoryParticipant preparatoryParticipant = new PreparatoryParticipant();
                preparatoryParticipant.setUserId(user.getId());
                preparatoryParticipant.setTrainingPlanId(trainingPlanId);
                if (courses != null && courses.size() > 0) {
                    for (Course course : courses) {
                        // Add user/participant to Preparatory Participant
                        preparatoryParticipant.setCourseId(course.getId());
                        this.preparatoryParticipantDao
                                .addPreparatoryParticipant(preparatoryParticipant);
                        List<Session> sessions = this.sessionDao
                                .getSessionsByCourseId(course.getId());
                        for (Session session : sessions) {
                            SessionParticipant sessionParticipant = this.sessionParticipantDao
                                    .getSessionParticipant(session.getId(),
                                            user.getId());
                            // Also add user/participant to sessions belong to
                            // courses belong to training plan
                            if (sessionParticipant == null) {
                                this.sessionParticipantDao
                                        .addSessionParticipantFromPlan(
                                                session.getId(), user.getId());
                            }
                        }
                    }
                } else {
                    this.preparatoryParticipantDao
                            .addPreparatoryParticipant(preparatoryParticipant);
                }
            }
            return Utils.generateSuccessResponseEntity(users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addUsersToTrainingPlan" + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_ADD_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> delUsersFromTrainingPlan(int trainingPlanId,
            List<User> users) {
        try {
            for (User user : users) {
                List<PreparatoryParticipant> preparatoryParticipants = this.preparatoryParticipantDao
                        .getPreparatoryParticipant(trainingPlanId, user.getId());
                if (preparatoryParticipants == null
                        || preparatoryParticipants.size() == 0)
                    throw new DeleteObjectException(
                            Message.MSG_TRAINING_PLAN_NOT_FOUND
                                    + " TrainingPlanId = " + trainingPlanId);
                this.preparatoryParticipantDao
                        .delPreparatoryParticipant(preparatoryParticipants);
                
                List<Course> courses = this.trainingPlanDao
                        .getCourseListByPlan(trainingPlanId);
                if (courses != null && courses.size() > 0){
                    for (Course course : courses) {
                        List<Session> sessions = this.sessionDao
                                .getSessionsByCourseId(course.getId());
                        for (Session session : sessions) {
                            this.sessionParticipantDao.delSessionParticipant(session.getId(), user.getId());
                        }
                    }
                }
            }
            return Utils.generateSuccessResponseEntity(users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - delUsersFromTrainingPlan "
                    + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    e.getMessage());
        }
    }
    
    @Override
    public ResponseEntity<?> getCoursesStatusList(int trainingPlanId, int sessionStatus) {
        try {
            List<Course> coursesList = this.trainingPlanDao.getCourseListByPlan(trainingPlanId);
            if (coursesList == null || coursesList.size() == 0){
                return Utils
                        .generateSuccessResponseEntity(countCourseStatus(coursesList));
            }
            if (sessionStatus == 0){
                for (Course course : coursesList) {
                    List<Session> sessions = this.sessionDao
                            .getSessionsByCourseId(course.getId());
                    for (Session session : sessions) {
                        session.setStatus(getSessionStatus(session.getStartTime(),
                                session.getEndTime()));
                    }
                    course.setSessions(sessions);
                }
            } else {
                for (Course course : coursesList) {
                    List<Session> sessions = this.sessionDao
                            .getSessionsByCourseIdAndStatus(course.getId(), sessionStatus);
                    for (Session session : sessions) {
                        session.setStatus(getSessionStatus(session.getStartTime(),
                                session.getEndTime()));
                    }
                    course.setSessions(sessions);
                }
            }
            return Utils
                    .generateSuccessResponseEntity(countCourseStatus(coursesList));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getStatusList " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }
    
    private CourseSessionStatus countCourseStatus(List<Course> coursesList){
        int notStarted = 0;
        int inProgress = 0;
        int completed = 0;
        int inactiveSession = 0;
        if (coursesList != null && coursesList.size() != 0){
            for (Course course : coursesList){
                if (course.getIsActive() == 0){
                    inactiveSession = inactiveSession + course.getSessions().size();
                } else {
                    List<Session> sessions = course.getSessions();
                    for (Session session : sessions){
                        if (session.getStatus().equals(SessionStatus.NOT_STARTED.getStrValue())){
                            notStarted = notStarted + 1;
                        } else if (session.getStatus().equals(SessionStatus.IN_PROGRESS.getStrValue())){
                            inProgress = inProgress + 1;
                        } else if (session.getStatus().equals(SessionStatus.COMPLETED.getStrValue())){
                            completed = completed + 1;
                        }
                    }
                }
            }
        }        
        CourseSessionStatus courseSessionStatus = new CourseSessionStatus();
        courseSessionStatus.setNotStarted(notStarted);
        courseSessionStatus.setInProgress(inProgress);
        courseSessionStatus.setCompleted(completed);
        courseSessionStatus.setInactiveSession(inactiveSession);
        return courseSessionStatus;
    }

    private List<CourseOfTrainingPlanDashBoard> convertCourseToDashBoard(
            List<Course> courses) {
        List<CourseOfTrainingPlanDashBoard> listReturn = new ArrayList<>();
        for (Course course : courses) {
            List<Session> sessions = course.getSessions();
            for (Session session : sessions) {
                CourseOfTrainingPlanDashBoard courseOfTrainingPlanDashBoard = new CourseOfTrainingPlanDashBoard();
                courseOfTrainingPlanDashBoard.setId(session.getId());
                courseOfTrainingPlanDashBoard.setStart(session.getStartTime());
                courseOfTrainingPlanDashBoard.setEnd(session.getEndTime());
                courseOfTrainingPlanDashBoard
                        .setSessionStatus(getSessionStatus(
                                session.getStartTime(), session.getEndTime()));
                String title = course.getName() + " (" + session.getName()
                        + ")";
                courseOfTrainingPlanDashBoard.setTitle(title);
                String colorStatus = convertColor(getSessionStatus(
                        session.getStartTime(), session.getEndTime()));
                courseOfTrainingPlanDashBoard.setBackgroundColor(colorStatus);
                courseOfTrainingPlanDashBoard.setBorderColor(colorStatus);
                courseOfTrainingPlanDashBoard.setCourseStatus(course
                        .getIsActive() == 1 ? "Active" : "Inactive");
                if (courseOfTrainingPlanDashBoard.getCourseStatus().equals(
                        "Inactive")) {
                    courseOfTrainingPlanDashBoard
                            .setBackgroundColor(ColorWithStatus.IN_ACTIVE
                                    .getStrValue());
                    courseOfTrainingPlanDashBoard
                            .setBorderColor(ColorWithStatus.IN_ACTIVE
                                    .getStrValue());
                }
                listReturn.add(courseOfTrainingPlanDashBoard);
            }

        }
        return listReturn;
    }

    private String convertColor(String sessionStatus) {
        if (SessionStatus.NOT_STARTED.getStrValue().equals(sessionStatus)) {
            return ColorWithStatus.NOT_STARTED.getStrValue();
        } else if (SessionStatus.IN_PROGRESS.getStrValue()
                .equals(sessionStatus)) {
            return ColorWithStatus.INPROGRESS.getStrValue();
        } else if (SessionStatus.COMPLETED.getStrValue().equals(sessionStatus)) {
            return ColorWithStatus.COMPLETED.getStrValue();
        }
        return ColorWithStatus.DEFAULT.getStrValue();
    }

    private String getSessionStatus(Timestamp startTime, Timestamp endTime) {
        if (startTime == null && endTime == null)
            return null;
        Timestamp current = new Timestamp(System.currentTimeMillis());
        if (startTime != null && startTime.after(current)) {
            return SessionStatus.NOT_STARTED.getStrValue();
        } else if (endTime != null && endTime.before(current)) {
            return SessionStatus.COMPLETED.getStrValue();
        }
        return SessionStatus.IN_PROGRESS.getStrValue();
    }

}
