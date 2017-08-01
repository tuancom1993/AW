package com.amway.lms.backend.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.amway.lms.backend.common.AmwayEnum;
import com.amway.lms.backend.common.AmwayEnum.ColorWithStatus;
import com.amway.lms.backend.common.AmwayEnum.CompletionStatus;
import com.amway.lms.backend.common.AmwayEnum.CourseStatus;
import com.amway.lms.backend.common.AmwayEnum.ManagerActionStatus;
import com.amway.lms.backend.common.AmwayEnum.QuizResultStatus;
import com.amway.lms.backend.common.AmwayEnum.QuizStatus;
import com.amway.lms.backend.common.AmwayEnum.TrainingActionStatus;
import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.ComparatorUtils;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Message;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Category;
import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.entity.CoursesOfTrainingPlan;
import com.amway.lms.backend.entity.Department;
import com.amway.lms.backend.entity.Location;
import com.amway.lms.backend.entity.PreparatoryParticipant;
import com.amway.lms.backend.entity.Quiz;
import com.amway.lms.backend.entity.Role;
import com.amway.lms.backend.entity.Room;
import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.entity.SessionParticipant;
import com.amway.lms.backend.entity.TrainingAction;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.ActionPlan;
import com.amway.lms.backend.model.CourseInformation;
import com.amway.lms.backend.model.CourseSessionExport;
import com.amway.lms.backend.model.Employee;
import com.amway.lms.backend.model.EventCourse;
import com.amway.lms.backend.model.EventCourseTableView;
import com.amway.lms.backend.model.GeneralInformation;
import com.amway.lms.backend.model.LocationAndTime;
import com.amway.lms.backend.model.PostTrainingAction;
import com.amway.lms.backend.model.PresentationSkill;
import com.amway.lms.backend.model.ProcessStatus;
import com.amway.lms.backend.model.QuizResult;
import com.amway.lms.backend.model.UserDetail;
import com.amway.lms.backend.repository.CategoryRepository;
import com.amway.lms.backend.repository.CourseOfTrainingPlanRepository;
import com.amway.lms.backend.repository.CourseRepository;
import com.amway.lms.backend.repository.DepartmentRepository;
import com.amway.lms.backend.repository.LocationRepository;
import com.amway.lms.backend.repository.PreparatoryParticipantRepository;
import com.amway.lms.backend.repository.QuizRepository;
import com.amway.lms.backend.repository.RoleRepository;
import com.amway.lms.backend.repository.RoomRepository;
import com.amway.lms.backend.repository.SessionParticipantRepository;
import com.amway.lms.backend.repository.SessionRepository;
import com.amway.lms.backend.repository.TrainingActionRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.EmailService;
import com.amway.lms.backend.service.LearningDashboardService;

@Service
@Transactional
public class LearningDashboardServiceImpl implements LearningDashboardService {
    private static final Logger logger = LoggerFactory.getLogger(LearningDashboardServiceImpl.class);

    // @Autowired
    // private CourseParticipantRepository courseParticipantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TrainingActionRepository trainingActionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SessionParticipantRepository sessionParticipantRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CourseOfTrainingPlanRepository courseOfTrainingPlanRepository;

    @Autowired
    private PreparatoryParticipantRepository preparatoryParticipantRepository;

    @Autowired
    private QuizRepository quizRepository;

    // @Autowired
    // private SupperviseTraineeRepository supperviseTraineeRepository;

    /**
     * - Show all participated courses And post training action is either empty
     * or rejected by direct supervisor
     * 
     * @throws ObjectNotFoundException
     */

    @Override
    public ResponseEntity<?> getAllCourse(List<Integer> userIds, int trainingPlanId, int year) {
        try {
            List<EventCourse> eventCourses = new LinkedList<>();
            for (Integer userId : userIds) {
                List<SessionParticipant> lstSessionParticipant = new ArrayList<>();
                if (trainingPlanId == -1 && year == -1) {
                    lstSessionParticipant = sessionParticipantRepository.getSessionParticipantByUserId(userId);
                } else if (trainingPlanId != -1 && year == -1) {
                    // lstSessionParticipant =
                    // getSessionParticipantsByPlanAndUser(
                    // userId, trainingPlanId);
                    lstSessionParticipant = this.sessionParticipantRepository.getListByUserTrainingPlan(userId,
                            trainingPlanId);
                } else if (trainingPlanId == -1 && year != -1) {
                    lstSessionParticipant = this.sessionParticipantRepository.getListByUserYear(userId, year);
                } else if (trainingPlanId != -1 && year != -1) {
                    lstSessionParticipant = this.sessionParticipantRepository.getListByUserTrainingPlanYear(userId,
                            trainingPlanId, year);
                }
                for (SessionParticipant sessionParticipant : lstSessionParticipant) {
                    EventCourse eventCourse = new EventCourse();
                    com.amway.lms.backend.model.Course courseModel = getCourse(sessionParticipant);
                    if (courseModel != null) {
                        eventCourse = getEventCourse(courseModel, sessionParticipant);
                        if (ManagerActionStatus.ACCEPTED.getValue() == sessionParticipant.getManagerActionStatus()) {
                            eventCourses.add(eventCourse);
                        }
                    }
                }
            }
            return Utils.generateSuccessResponseEntity(eventCourses);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCoursesList " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }

    }

    @Override
    public ResponseEntity<?> getCourseListByCompletionStatus(int userId, int completionStatus) {
        try {
            List<EventCourse> eventCourses = new LinkedList<>();
            List<SessionParticipant> lstSessionParticipant = this.sessionParticipantRepository
                    .getListByUserAndCompletionStatus(userId, completionStatus);
            if (lstSessionParticipant == null || lstSessionParticipant.isEmpty())
                throw new ObjectNotFoundException();

            for (SessionParticipant sessionParticipant : lstSessionParticipant) {
                EventCourse eventCourse = new EventCourse();
                com.amway.lms.backend.model.Course courseModel = getCourse(sessionParticipant);
                if (courseModel != null) {
                    eventCourse = getEventCourse(courseModel, sessionParticipant);
                    if (ManagerActionStatus.ACCEPTED.getValue() == sessionParticipant.getManagerActionStatus()) {
                        eventCourses.add(eventCourse);
                    }
                }
            }
            return Utils.generateSuccessResponseEntity(eventCourses);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCourseListByCompletionStatus " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    /**
     * Reuse code from getAllCourse(List<Integer> userIds) to export pdf Any
     * change above function, need update this also
     */
    @Override
    public List<CourseSessionExport> getAllCourseSessionExport(Integer userId) throws ObjectNotFoundException {
        List<CourseSessionExport> courseSessions = new LinkedList<>();
        List<SessionParticipant> lstSessionParticipant = sessionParticipantRepository
                .getSessionParticipantByUserId(userId);
        if (CollectionUtils.isEmpty(lstSessionParticipant)) {
            Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
        for (SessionParticipant sessionParticipant : lstSessionParticipant) {
            CourseSessionExport courseSession = new CourseSessionExport();
            com.amway.lms.backend.model.Course courseModel = getCourse(sessionParticipant);

            courseSession.setCourseName(courseModel.getCourseName());
            courseSession.setStatus(courseModel.getCourseStatus());
            courseSession.setSessionName(courseModel.getSessionName());
            courseSession.setStartTime(courseModel.getStartTime());
            courseSession.setEndTime(courseModel.getEndTime());

            courseSessions.add(courseSession);
        }
        return courseSessions;
    }

    @Override
    public ResponseEntity<?> getParticipatedCourses(int userId, Integer trainingPlanId) throws ObjectNotFoundException {
        List<EventCourseTableView> eventCourses = new LinkedList<>();
        List<SessionParticipant> lstSessionParticipant;
        if (trainingPlanId == null || trainingPlanId == -1) {
            lstSessionParticipant = sessionParticipantRepository.getSessionParticipantByUserId(userId);
        } else {
            // lstSessionParticipant =
            // getSessionParticipantsByPlanAndUser(userId,
            // trainingPlanId);
            lstSessionParticipant = this.sessionParticipantRepository.getListByUserTrainingPlan(userId, trainingPlanId);
        }

        if (CollectionUtils.isEmpty(lstSessionParticipant)) {
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
        for (SessionParticipant sessionParticipant : lstSessionParticipant) {
            com.amway.lms.backend.model.Course courseModel = getCourse(sessionParticipant);
            // courseModel.getIsOptional() == 0 mean is required
            if (sessionParticipant != null && courseModel.getIsOptional() == 0
                    && CompletionStatus.ENDED_SESSION.getValue() == sessionParticipant.getCompletionStatus()
                    || CompletionStatus.COMPLETED.getValue() == sessionParticipant.getCompletionStatus()) {
                EventCourseTableView eventCourse = new EventCourseTableView();
                eventCourse.setCourseName(courseModel.getCourseName());
                eventCourse.setCourseStatus(courseModel.getCourseStatus());
                eventCourse.setSessionName(courseModel.getSessionName());
                eventCourse.setSessionParticipantId(courseModel.getSessionParticipantId());
                eventCourse.setTrainingActionStatus(courseModel.getTrainingActionStatus());
                eventCourse.setSessionId(courseModel.getSessionId());
                eventCourses.add(eventCourse);

                // EventCourse eventCourse = new EventCourse();
                // eventCourse.setBackgroundColor(convertColor(courseModel
                // .getCourseStatus()));
                // eventCourse.setBorderColor(convertColor(courseModel
                // .getCourseStatus()));
                // eventCourse.setCourseId(courseModel.getCourseId());
                // eventCourse.setSessionParticipantId(sessionParticipant.getId());
                // eventCourse.setCourseStatus(courseModel.getCompletionStatus());
                // String title = new StringBuilder()
                // .append(courseModel.getCourseName()).append("(")
                // .append(courseModel.getSessionName()).append(")")
                // .toString();
                // eventCourse.setTrainingActionStatus(courseModel
                // .getTrainingActionStatus());
                // eventCourse.setTitle(title);
                // eventCourse.setStart(courseModel.getTime());
                // eventCourse.setQuizStatus("Passed");
                // eventCourses.add(eventCourse);
            }
        }
        if (CollectionUtils.isEmpty(eventCourses)) {
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
        Collections.sort(eventCourses, ComparatorUtils.EVENT_COURSE_TABLE_VIEW_COMPARATOR);

        return Utils.generateSuccessResponseEntity(eventCourses);
    }

    @Override
    public ResponseEntity<?> getCourseInformation(int sessionParticipantId) throws ObjectNotFoundException {
        CourseInformation courseInformation = new CourseInformation();

        GeneralInformation generalInformation = new GeneralInformation();

        SessionParticipant sessionParticipant = sessionParticipantRepository
                .getSessionParticipantById(sessionParticipantId);
        Session session = sessionRepository.getSessionById(sessionParticipant.getSessionId());
        if (session == null) {
            throw new ObjectNotFoundException(ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);

        }
        Course course = courseRepository.getCourseById(session.getCourseId());
        if (course == null) {
            throw new ObjectNotFoundException(ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);

        }
        courseInformation.setCourseId(course.getId());
        courseInformation.setSessionParticipantId(sessionParticipantId);
        courseInformation.setTraineeId(sessionParticipant.getUserId());

        Category category = categoryRepository.getCategoryById(course.getCategoryId());
        Location location = locationRepository.getLocationById(session.getLocationId());
        Room room = roomRepository.findRoombyId(session.getRoomId());

        generalInformation.setCourseName(course.getName());
        generalInformation.setCourseObjective(course.getObjective());
        generalInformation.setSessionName(session.getName());
        generalInformation.setBriefDescription(course.getDescription());
        generalInformation.setCategory(category != null ? category.getName() : "");
        generalInformation.setStatus(converStatus(sessionParticipant.getCompletionStatus()));
        generalInformation.setLecture(session.getTrainerFullName());
        generalInformation.setAmount(course.getNumberOfSession());

        generalInformation.setCourseStatus(course.getIsActive() == 1 ? "Active" : "Inactive");

        courseInformation.setGeneralInformation(generalInformation);
        LocationAndTime locationAndTime = new LocationAndTime();

        locationAndTime.setLocation(location != null ? location.getName() : "");
        locationAndTime.setRoom(room != null ? room.getName() : "");
        // locationAndTime.setStartTime(Utils.convertDateToString(session
        // .getStartTime()));
        // locationAndTime.setEndTime(Utils.convertDateToString(session
        // .getEndTime()));
        // Acton
        locationAndTime
                .setStartTime(Common.getStringDate(session.getStartTime(), Common.DATE_TIME_FORMAT_DD_MMM_YYYY_AM_PM));
        locationAndTime
                .setEndTime(Common.getStringDate(session.getEndTime(), Common.DATE_TIME_FORMAT_DD_MMM_YYYY_AM_PM));
        courseInformation.setLocationAndTime(locationAndTime);

        courseInformation.setQuizResult(getQuizResult(sessionParticipant));
        return Utils.generateSuccessResponseEntity(courseInformation);
    }

    @Override
    public ResponseEntity<?> updateStatusOfCourse(int sessionParticipantId, int status) {
        SessionParticipant sessionParticipant = sessionParticipantRepository
                .getSessionParticipantById(sessionParticipantId);
        if (sessionParticipant != null) {
            sessionParticipant.setCompletionStatus(status);
            sessionParticipantRepository.updateSessionParticipant(sessionParticipant);
        }
        return Utils.generateSuccessResponseEntity("updateStatusOfCourse");
    }

    @Override
    public ResponseEntity<?> getInfoOfTraineeOfTrainningAction(int sessionParticipantId)
            throws ObjectNotFoundException {

        SessionParticipant sessionParticipant = sessionParticipantRepository
                .getSessionParticipantById(sessionParticipantId);
        Session session = sessionRepository.getSessionById(sessionParticipant.getSessionId());
        if (session == null) {
            throw new ObjectNotFoundException(ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);

        }
        Course course = courseRepository.getCourseById(session.getCourseId());
        if (course == null) {
            throw new ObjectNotFoundException(ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);

        }

        // Set Data PostTrainingAction
        PostTrainingAction postTrainingAction = new PostTrainingAction();
        postTrainingAction.setSessionParticipantId(sessionParticipantId);
        postTrainingAction.setCourseId(course.getId());
        postTrainingAction.setCourseName(course.getName());
        postTrainingAction.setCourseTye(course.getIsOffline() == 1 ? "Online" : "Offline");
        postTrainingAction.setStatus("");

        // Set Data QuizResult
        QuizResult quizResult = getQuizResult(sessionParticipant);
        postTrainingAction.setQuizResult(quizResult);

        return Utils.generateSuccessResponseEntity(postTrainingAction);
    }

    @Override
    public ResponseEntity<?> submitTrainingAction(ActionPlan actionPlan) {

        TrainingAction trainingAction = trainingActionRepository
                .findTrainingActionBySessionParticipantId(actionPlan.getSessionParticipantId());
        if (trainingAction != null) {
            trainingAction.setSessionParticipantId(actionPlan.getSessionParticipantId());
            trainingAction.setTodoPlan(actionPlan.getTodoPlan());
            trainingAction.setStatus(AmwayEnum.TrainingActionStatus.SUBMITTED.getValue());
            try {
                trainingAction.setStartTime(
                        Common.getTimeStampFromString(actionPlan.getStartTime(), Common.DATE_FORMAT_DD_MMM_YYYY));
                trainingAction.setEndTime(
                        Common.getTimeStampFromString(actionPlan.getEndTime(), Common.DATE_FORMAT_DD_MMM_YYYY));
                trainingActionRepository.updateTrainingAction(trainingAction);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Utils.generateSuccessResponseEntity(actionPlan);
        } else {
            trainingAction = new TrainingAction();
            trainingAction.setSessionParticipantId(actionPlan.getSessionParticipantId());
            trainingAction.setTodoPlan(actionPlan.getTodoPlan());
            try {
                trainingAction.setStartTime(
                        Common.getTimeStampFromString(actionPlan.getStartTime(), Common.DATE_FORMAT_DD_MMM_YYYY));
                trainingAction.setEndTime(
                        Common.getTimeStampFromString(actionPlan.getEndTime(), Common.DATE_FORMAT_DD_MMM_YYYY));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            trainingActionRepository.saveTrainingAction(trainingAction);
            return Utils.generateSuccessResponseEntity(actionPlan);
        }
    }

    @Override
    public ResponseEntity<?> getListCourseOfTraineeOfOptionCourse(int userId, Integer trainingPlanId)
            throws ObjectNotFoundException {
        List<com.amway.lms.backend.model.Course> lstCourseModel = new LinkedList<>();
        List<SessionParticipant> lstSessionParticipant;
        if (trainingPlanId == null || trainingPlanId == -1) {
            lstSessionParticipant = sessionParticipantRepository.getSessionParticipantByUserId(userId);
        } else {
            // lstSessionParticipant =
            // getSessionParticipantsByPlanAndUser(userId,
            // trainingPlanId);
            lstSessionParticipant = this.sessionParticipantRepository.getListByUserTrainingPlan(userId, trainingPlanId);
        }

        for (SessionParticipant sessionParticipant : lstSessionParticipant) {
            Session session = sessionRepository.getSessionById(sessionParticipant.getSessionId());
            if (session == null) {
                throw new ObjectNotFoundException(ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);

            }
            Course course = courseRepository.getCourseById(session.getCourseId());
            if (course == null) {
                throw new ObjectNotFoundException(ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);

            }
            if (ManagerActionStatus.ACCEPTED.getValue() == sessionParticipant.getManagerActionStatus()) {
                lstCourseModel.add(getCourse(sessionParticipant));
            }
        }
        if (CollectionUtils.isEmpty(lstCourseModel)) {
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
        Collections.sort(lstCourseModel, ComparatorUtils.COURSE_MODEL_COMPARATOR);
        return Utils.generateSuccessResponseEntity(lstCourseModel);
    }

    @Override
    public ResponseEntity<?> getPresentationSkill(int courseId) {
        Course course = courseRepository.getCourseById(courseId);
        if (course == null) {
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
        PresentationSkill presentationSkill = new PresentationSkill();
        presentationSkill.setPostTrainingComment(course.getPostTrainingComment());
        presentationSkill.setPreTrainingComment(course.getPreTrainingComment());
        return Utils.generateSuccessResponseEntity(presentationSkill);
    }

    @Override
    public ResponseEntity<?> getPresentationSkillBySession(int sessionId, int userId) {
        SessionParticipant sessionParticipant = sessionParticipantRepository.getSessionParticipant(sessionId, userId);
        if (sessionParticipant == null) {
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
        PresentationSkill presentationSkill = new PresentationSkill();
        presentationSkill.setPreTrainingComment(sessionParticipant.getPreTrainingComment());
        return Utils.generateSuccessResponseEntity(presentationSkill);
    }

    @Override
    public ResponseEntity<?> addPretrainingComment(int courseId, String preTrainingComment) {

        Course course = courseRepository.getCourseById(courseId);
        course.setPreTrainingComment(preTrainingComment);
        courseRepository.editCourse(course);
        return Utils.generateSuccessResponseEntity("Add pretrainingComment success");
    }

    @Override
    public ResponseEntity<?> searchByStaffCode(int supperviseId, String staffCode) {

        UserDetail userDetail = new UserDetail();
        // Get user by staffCode
        List<Integer> userIds = getListUserIdOfSuppervise(supperviseId);
        for (Integer userId : userIds) {
            User user = userRepository.searchByUserIdandStaffCode(userId, staffCode);
            if (user == null) {
                continue;
            }
            List<com.amway.lms.backend.model.Course> lstCourse = new ArrayList<>();

            userDetail.setEmployee(getEmployee(user));

            // Get Course and session

            List<SessionParticipant> lstSessionParticipant = sessionParticipantRepository
                    .getSessionParticipantByUserId(userId);
            for (SessionParticipant sessionParticipant : lstSessionParticipant) {
                try {
                    lstCourse.add(getCourse(sessionParticipant));
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }
            userDetail.setCourse(lstCourse);

            if (userDetail.getEmployee() == null) {
                return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                        ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
            }
        }
        Collections.sort(userDetail.getCourse(), new Comparator<com.amway.lms.backend.model.Course>() {
            @Override
            public int compare(com.amway.lms.backend.model.Course o1, com.amway.lms.backend.model.Course o2) {
                return o2.getSessionId() - o1.getSessionId();
            }
        });
        return Utils.generateSuccessResponseEntity(userDetail);
    }

    @Override
    public ResponseEntity<?> searchByHodStaffCode(int hodId, String staffCode) {

        UserDetail userDetail = new UserDetail();
        // Get user by staffCode
        List<Integer> userIds = getListUserIdOfHod(hodId);
        for (Integer userId : userIds) {
            User user = userRepository.searchByUserIdandStaffCode(userId, staffCode);
            if (user == null) {
                continue;
            }
            List<com.amway.lms.backend.model.Course> lstCourse = new ArrayList<>();

            userDetail.setEmployee(getEmployee(user));

            // Get Course and session

            List<SessionParticipant> lstSessionParticipant = sessionParticipantRepository
                    .getSessionParticipantByUserId(userId);
            for (SessionParticipant sessionParticipant : lstSessionParticipant) {
                try {
                    lstCourse.add(getCourse(sessionParticipant));
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                    System.out.println();
                }
                userDetail.setCourse(lstCourse);

            }

            if (userDetail.getEmployee() == null) {
                return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                        ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
            }
        }
        return Utils.generateSuccessResponseEntity(userDetail);
    }

    @Override
    public ResponseEntity<?> reviewActionTrainingActionPlan(int sessionParticipantId) {
        TrainingAction trainingAction = trainingActionRepository
                .findTrainingActionBySessionParticipantId(sessionParticipantId);
        if (trainingAction == null) {
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }

        SessionParticipant sessionParticipant = sessionParticipantRepository
                .getSessionParticipantById(sessionParticipantId);
        if (sessionParticipant == null) {
            logger.error("Cannot find participant by id: " + sessionParticipantId);
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }

        ActionPlan actionPlan = new ActionPlan();
        actionPlan.setSessionParticipantId(trainingAction.getSessionParticipantId());
        actionPlan.setTodoPlan(trainingAction.getTodoPlan());
        actionPlan.setStartTime(Common.getStringDate(trainingAction.getStartTime(), Common.DATE_FORMAT_DD_MMM_YYYY));
        actionPlan.setEndTime(Common.getStringDate(trainingAction.getEndTime(), Common.DATE_FORMAT_DD_MMM_YYYY));
        actionPlan.setStatus(trainingAction.getStatus());
        return Utils.generateSuccessResponseEntity(actionPlan);
    }

    @Override
    public ResponseEntity<?> reviewActionTrainingQuiz(int sessionParticipantId) {

        SessionParticipant sessionParticipant = sessionParticipantRepository
                .getSessionParticipantById(sessionParticipantId);
        if (sessionParticipant == null) {
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
        return Utils.generateSuccessResponseEntity(getQuizResult(sessionParticipant));
    }

    @Override
    public ResponseEntity<?> getAllCourseBySuper(List<Integer> userIds) throws ObjectNotFoundException {
        List<EventCourse> eventCourses = new LinkedList<>();
        for (Integer userId : userIds) {
            List<SessionParticipant> lstSessionParticipant = sessionParticipantRepository
                    .getSessionParticipantByUserId(userId);
            if (CollectionUtils.isEmpty(lstSessionParticipant)) {
                return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                        ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
            }
            for (SessionParticipant sessionParticipant : lstSessionParticipant) {
                EventCourse eventCourse = new EventCourse();
                com.amway.lms.backend.model.Course courseModel = getCourse(sessionParticipant);
                eventCourse.setBackgroundColor(convertColor(courseModel.getCourseStatus()));
                String title = new StringBuilder().append(courseModel.getCourseName()).append("(")
                        .append(courseModel.getSessionName()).append(")").toString();
                eventCourse.setTitle(title);
                eventCourse.setBorderColor(convertColor(courseModel.getCourseStatus()));
                eventCourse.setCourseId(courseModel.getCourseId());
                eventCourse.setSessionParticipantId(sessionParticipant.getId());
                eventCourse.setStart(courseModel.getTime());
                // status of course_participants
                eventCourse.setCourseStatus(courseModel.getSessionStatus());
                eventCourses.add(eventCourse);
                break;
            }
        }
        return new ResponseEntity<>(eventCourses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getListCourseCoursesApprovedByHR(List<Integer> userIds) {
        List<com.amway.lms.backend.model.Course> lstCourseModel = new LinkedList<>();
        for (Integer userId : userIds) {
            List<SessionParticipant> lstSessionParticipant = sessionParticipantRepository
                    .getSessionParticipantByUserId(userId);
            for (SessionParticipant sessionParticipant : lstSessionParticipant) {
                try {
                    lstCourseModel.add(getCourse(sessionParticipant));
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        Collections.sort(lstCourseModel, ComparatorUtils.COURSE_MODEL_COMPARATOR);
        return Utils.generateSuccessResponseEntity(lstCourseModel);
    }

    @Override
    public void updateComment(int sessionParticipantId, String comment) {
        SessionParticipant sessionParticipant = sessionParticipantRepository
                .getSessionParticipantById(sessionParticipantId);
        sessionParticipant.setComment(comment);
        sessionParticipantRepository.updateSessionParticipant(sessionParticipant);
    }

    @Override
    public List<Integer> getListUserIdOfSuppervise(int supperviseId) {
        return userRepository.getUserByApprovalMangerId(supperviseId);
    }

    @Override
    public ResponseEntity<?> getAllEmployees(int supperviseId) {
        List<Integer> userIds = getListUserIdOfSuppervise(supperviseId);
        List<Employee> lstEmployee = new LinkedList<>();
        for (Integer userId : userIds) {
            User user = userRepository.getUserById(userId);
            if (user == null) {
                continue;
            }
            Employee employee = getEmployee(user);
            lstEmployee.add(employee);
        }
        return Utils.generateSuccessResponseEntity(lstEmployee);
    }

    @Override
    public List<Integer> getListUserIdOfHod(int hodId) {
        return userRepository.getUserByHodId(hodId);
    }

    @Override
    public ResponseEntity<?> getAllEmployeesByHod(int hodId) {
        List<Integer> userIds = getListUserIdOfHod(hodId);
        List<Employee> lstEmployee = new LinkedList<>();
        for (Integer userId : userIds) {
            User user = userRepository.getUserById(userId);
            if (user == null) {
                continue;
            }
            Employee employee = getEmployee(user);
            lstEmployee.add(employee);
        }
        return Utils.generateSuccessResponseEntity(lstEmployee);
    }

    @Override
    public List<Integer> getListUserIdOfHodByDepartment(int hodId, int departmentId) {
        return userRepository.getUserByHodIdAndDepartment(hodId, departmentId);
    }

    @Override
    public ResponseEntity<?> getEmployeesByDepartment(int hodId, int departmentId) {
        List<Integer> userIds = new ArrayList<>();
        if (departmentId == -1) {
            userIds = getListUserIdOfHod(hodId);
        } else {
            userIds = getListUserIdOfHodByDepartment(hodId, departmentId);
        }
        List<Employee> lstEmployee = new LinkedList<>();
        for (Integer userId : userIds) {
            if(hodId == userId){
                continue;
            }

            User user = userRepository.getUserById(userId);
            if (user == null) {
                continue;
            }
            Employee employee = getEmployee(user);
            lstEmployee.add(employee);
        }
        return Utils.generateSuccessResponseEntity(lstEmployee);
    }

    @Override
    public List<Integer> getListUserIdOfAmByDepartment(int approvalManagerId, int departmentId) {
        return userRepository.getListUserIdOfAmByDepartment(approvalManagerId, departmentId);
    }

    @Override
    public ResponseEntity<?> getEmployeesByApprovalManagerId(int approvalManagerId, int departmentId) {
        List<Integer> userIds = new ArrayList<>();
        if (departmentId == -1) {
            userIds = userRepository.getTraineeByApprovalManager(approvalManagerId);
        } else {
            userIds = getListUserIdOfAmByDepartment(approvalManagerId, departmentId);
        }
        List<Employee> lstEmployee = new LinkedList<>();
        for (Integer userId : userIds) {
            User user = userRepository.getUserById(userId);
            if (user == null) {
                continue;
            }
            Employee employee = getEmployee(user);
            lstEmployee.add(employee);
        }
        return Utils.generateSuccessResponseEntity(lstEmployee);
    }

    @Override
    public ResponseEntity<?> getLearningPlanUSer(int userId) throws ObjectNotFoundException {
        UserDetail userDetail = new UserDetail();
        // Get user by staffCode
        User user = userRepository.getUserById(userId);
        if (user == null) {
        }
        List<com.amway.lms.backend.model.Course> lstCourse = new ArrayList<>();

        userDetail.setEmployee(getEmployee(user));
        // Get Course and session
        List<SessionParticipant> lstSessionParticipant = sessionParticipantRepository
                .getSessionParticipantByUserId(userId);
        for (SessionParticipant sessionParticipant : lstSessionParticipant) {
            lstCourse.add(getCourse(sessionParticipant));
        }
        userDetail.setCourse(lstCourse);
        if (user == null || CollectionUtils.isEmpty(lstSessionParticipant)) {
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
        return Utils.generateSuccessResponseEntity(userDetail);

    }

    @Override
    public ResponseEntity<?> getSuggestCourse(int userId, int trainingPlanId) {
        List<com.amway.lms.backend.model.Course> courseSuggest = new LinkedList<>();
        List<Session> sessions = sessionRepository.getListCourseSuggest(userId);
        for (Session session : sessions) {
            PreparatoryParticipant preparatoryParticipants = preparatoryParticipantRepository
                    .getPreparatoryParticipantByCourseIdAndUserId(session.getCourseId(), userId, session.getId());
            if (preparatoryParticipants != null) {
                continue;
            }
            if (trainingPlanId == 0) {
                Course courseEntity = courseRepository.getCourseById(session.getCourseId());
                com.amway.lms.backend.model.Course courseModel = new com.amway.lms.backend.model.Course();
                courseModel.setCourseId(courseEntity != null ? courseEntity.getId() : session.getCourseId());
                courseModel.setCourseName(courseEntity != null ? courseEntity.getName() : session.getDescription());
                courseModel.setTraineeId(userId);
                courseModel.setStartTime(Utils.convertDateToString(session.getStartTime()));
                courseModel.setEndTime(Utils.convertDateToString(session.getEndTime()));
                courseModel.setSessionId(session.getId());
                courseModel.setSessionName(session.getName());
                courseSuggest.add(courseModel);
            } else {
                CoursesOfTrainingPlan coursesOfTrainingPlan = courseOfTrainingPlanRepository
                        .getCourseOfTrainingPlan(trainingPlanId, session.getCourseId());
                if (coursesOfTrainingPlan == null) {
                    continue;
                }
                Course courseEntity = courseRepository.getCourseById(session.getCourseId());
                com.amway.lms.backend.model.Course courseModel = new com.amway.lms.backend.model.Course();
                courseModel.setCourseId(courseEntity != null ? courseEntity.getId() : session.getCourseId());
                courseModel.setCourseName(courseEntity != null ? courseEntity.getName() : session.getDescription());
                courseModel.setTraineeId(userId);
                courseModel.setStartTime(Utils.convertDateToString(session.getStartTime()));
                courseModel.setEndTime(Utils.convertDateToString(session.getEndTime()));
                courseModel.setSessionId(session.getId());
                courseModel.setSessionName(session.getName());
                courseSuggest.add(courseModel);
            }

        }
        return Utils.generateSuccessResponseEntity(courseSuggest);

    }

    @Override
    public ResponseEntity<?> addSuggestCourse(List<com.amway.lms.backend.model.Course> courses) {
        for (com.amway.lms.backend.model.Course course : courses) {
            PreparatoryParticipant preparatoryParticipant = new PreparatoryParticipant();
            preparatoryParticipant.setCourseId(course.getCourseId());
            preparatoryParticipant.setUserId(course.getTraineeId());
            preparatoryParticipant.setTrainingPlanId(course.getTrainingPlanId());
            preparatoryParticipant.setIsRequestedByManager(1);
            User user = userRepository.getUserById(course.getTraineeId());
            User manager = userRepository.getUserById(user.getApprovalManagerId());
            preparatoryParticipant.setSessionId(course.getSessionId());
            preparatoryParticipant.setRequestedManagerName(manager.getFirstName() + " " + manager.getLastName());
            preparatoryParticipantRepository.addPreparatoryParticipant(preparatoryParticipant);
        }
        return Utils.generateSuccessResponseEntity("addSuggestCourse");
    }

    private com.amway.lms.backend.model.Course getCourse(SessionParticipant sessionParticipant)
            throws ObjectNotFoundException {

        com.amway.lms.backend.model.Course courseModel = null;
        Session session = sessionRepository.getSessionById(sessionParticipant.getSessionId());
        if (session != null) {
            Course course = courseRepository.getCourseById(session.getCourseId());
            if (course != null) {
                courseModel = new com.amway.lms.backend.model.Course();
                courseModel.setSessionParticipantId(sessionParticipant.getId());
                courseModel.setCourseId(course.getId());
                courseModel.setTraineeId(sessionParticipant.getUserId());
                if (sessionParticipant != null) {
                    courseModel.setCompletionStatus(converCompletionStatus(sessionParticipant.getCompletionStatus()));
                }
                User trainee = userRepository.getUserById(sessionParticipant.getUserId());
                courseModel.setCourseName(course.getName());
                courseModel.setTraineeName(trainee.getUserID());
                courseModel.setStartTime(
                        Common.getStringDate(session.getStartTime(), Common.DATE_TIME_FORMAT_DD_MMM_YYYY_AM_PM));
                courseModel.setEndTime(
                        Common.getStringDate(session.getEndTime(), Common.DATE_TIME_FORMAT_DD_MMM_YYYY_AM_PM));
                courseModel.setCourseStatus(converStatus(sessionParticipant.getCompletionStatus()));
                courseModel.setTime(session.getStartTime());
                courseModel.setQuizStatus(sessionParticipant.getQuizStatus() != 0 ? "Passed" : "Avaiable");
                TrainingAction trainingAction = this.trainingActionRepository
                        .findTrainingActionBySessionParticipantId(sessionParticipant.getId());
                courseModel.setTrainingActionStatus(trainingAction == null ? -1 : trainingAction.getStatus());
                courseModel.setSessionName(session.getName());
                courseModel.setIsOffline(course.getIsOffline());
                courseModel.setIsOptional(course.getIsOptional());
                courseModel
                        .setManagerActionStatus(converManagerActionStatus(sessionParticipant.getManagerActionStatus()));
                courseModel.setSessionStatus(session.getStatus());
                courseModel.setSessionId(session.getId());
                courseModel.setAjpvpoint(course.getPassQuizAJPV());
                courseModel.setManagerActionStatusValue(sessionParticipant.getManagerActionStatus());
                // courseModel.setApproval(convertApproval(String.valueOf(courseParticipant.getConfirmationStatus())));
            }
        }
        return courseModel;
    }

    /**
     * Set data model for Quiz
     * 
     * @param courseParticipant
     * @return
     */
    private QuizResult getQuizResult(SessionParticipant sessionParticipant) {
        Quiz quiz = quizRepository.getQuizBySessionParticipantId(sessionParticipant.getId());
        if (quiz == null)
            return null;

        QuizResult quizResult = new QuizResult();
        final int numberOfTest = sessionParticipant.getNumberOfTesting();
        quizResult.setNumberOfTesting(sessionParticipant.getNumberOfTesting() + "/" + quiz.getMaxTestTimes());
        quizResult.setCorrectAnswer(sessionParticipant.getQuizCorrectAnswers() + "/" + quiz.getNumberQuestions());
        quizResult.setQuizStatus(String.format(convertQuizStatus(sessionParticipant.getQuizStatus()), numberOfTest));
        quizResult.setScore(sessionParticipant.getQuizScore());
        quizResult.setQuizResult(convertQuizResultStatus(sessionParticipant.getQuizResultStatus()));
        quizResult.setIsMaxTest(sessionParticipant.getNumberOfTesting() < quiz.getMaxTestTimes() ? 0 : 1);
        return quizResult;
    }

    private final String convertQuizStatus(Integer value) {
        if (value == null) {
            return null;
        }
        if (value.equals(QuizStatus.NOT_TAKEN_YET.getValue())) {
            return QuizStatus.NOT_TAKEN_YET.getStrValue();
        } else if (value.equals(QuizStatus.AVAILABLE.getValue())) {
            return QuizStatus.AVAILABLE.getStrValue();
        }
        return QuizStatus.TESTED.getStrValue();

    }

    private final String convertQuizResultStatus(Integer value) {

        if (value == null) {
            return null;
        }
        if (value.equals(QuizResultStatus.NOT_TAKEN_YET.getValue())) {
            return QuizStatus.NOT_TAKEN_YET.getStrValue();
        } else if (value.equals(QuizResultStatus.FAILED.getValue())) {
            return QuizResultStatus.FAILED.getStrValue();
        }
        return QuizResultStatus.PASSED.getStrValue();

    }

    private Employee getEmployee(User user) {
        Department department = departmentRepository.getDepartmentById(user.getDepartmentId());
        Role role = roleRepository.getRoleById(user.getRoleId());
        Employee employee = new Employee();
        employee.setEmployeeId(user.getId());
        employee.setFirstName(user.getFirstName());
        employee.setLastName(user.getLastName());
        employee.setEmailAddress(user.getEmail());
        employee.setStatus(user.getActive());
        employee.setApprovalManagerId(user.getApprovalManagerId());
        employee.setFullName(user.getFirstName() + " " + user.getLastName());
        employee.setStaffCode(user.getStaffCode());
        employee.setJobTitle(user.getJobTitle());
        employee.setRoleId(user.getRoleId());
        employee.setRoleName(role.getDisplayName());
        employee.setDepartment(department != null ? department.getName() : "");
        employee.setAJPVPoints(new StringBuffer().append(user.getAjpv()).toString());
        return employee;
    }

    private String converStatus(Integer value) {
        if (value == null) {
            return null;
        }
        if (value == CompletionStatus.ACCEPTED.getValue()) {
            return CompletionStatus.ACCEPTED.getStrValue();
        } else if (value == (CompletionStatus.DENIED.getValue())) {
            return CompletionStatus.DENIED.getStrValue();
        } else if (value == (CompletionStatus.PARTICIPATED.getValue())) {
            return CompletionStatus.PARTICIPATED.getStrValue();
        }
        if (value == CompletionStatus.COMPLETED.getValue()) {
            return CompletionStatus.COMPLETED.getStrValue();
        }
        if (value == CompletionStatus.ENDED_SESSION.getValue()) {
            return CompletionStatus.ENDED_SESSION.getStrValue();
        }

        return CourseStatus.WAITING_FOR_APPROVAL.getStrValue();
    }

    private String converCompletionStatus(Integer value) {
        if (value == null) {
            return null;
        }
        if (value == CompletionStatus.ACCEPTED.getValue()) {
            return CompletionStatus.ACCEPTED.getStrValue();
        }
        if (value == CompletionStatus.DENIED.getValue()) {
            return CompletionStatus.DENIED.getStrValue();
        }
        if (value == CompletionStatus.PARTICIPATED.getValue()) {
            return CompletionStatus.PARTICIPATED.getStrValue();
        }
        if (value == CompletionStatus.ENDED_SESSION.getValue()) {
            return CompletionStatus.ENDED_SESSION.getStrValue();
        }
        if (value == CompletionStatus.COMPLETED.getValue()) {
            return CompletionStatus.COMPLETED.getStrValue();
        }
        return CompletionStatus.WAITING_FOR_APPROVAL.getStrValue();
    }

    private String converManagerActionStatus(Integer value) {
        if (value == null) {
            return null;
        }
        if (value == ManagerActionStatus.ACCEPTED.getValue()) {
            return ManagerActionStatus.ACCEPTED.getStrValue();
        }
        if (value == ManagerActionStatus.DENIED.getValue()) {
            return ManagerActionStatus.DENIED.getStrValue();
        }
        return ManagerActionStatus.WAITING_FOR_APPROVAL.getStrValue();
    }

    private String convertColor(String status) {
        if (status.equals(CompletionStatus.WAITING_FOR_APPROVAL.getStrValue())) {
            return ColorWithStatus.WAITING_FOR_APPROVAL.getStrValue();
        } else if (status.equals(CompletionStatus.ACCEPTED.getStrValue())) {
            return ColorWithStatus.ACCEPTED.getStrValue();
        } else if (status.equals(CompletionStatus.DENIED.getStrValue())) {
            return ColorWithStatus.DENIED.getStrValue();
        } else if (status.equals(CompletionStatus.PARTICIPATED.getStrValue())) {
            return ColorWithStatus.PARTICIPATED.getStrValue();
        } else if (status.equals(CompletionStatus.ENDED_SESSION.getStrValue())) {
            return ColorWithStatus.ENDED_SESSION.getStrValue();
        } else if (status.equals(CompletionStatus.COMPLETED.getStrValue())) {
            return ColorWithStatus.COMPLETED.getStrValue();
        }
        return ColorWithStatus.DEFAULT.getStrValue();
    }

    @Override
    public ResponseEntity<?> getLearningPlanProcess(int userId, Integer trainingPlanId) throws ObjectNotFoundException {
        ProcessStatus process = new ProcessStatus();
        List<SessionParticipant> lstSessionParticipant;
        if (trainingPlanId == null || trainingPlanId == -1) {
            lstSessionParticipant = sessionParticipantRepository.getSessionParticipantByUserId(userId);
        } else {
            // lstSessionParticipant =
            // getSessionParticipantsByPlanAndUser(userId,
            // trainingPlanId);
            lstSessionParticipant = this.sessionParticipantRepository.getListByUserTrainingPlan(userId, trainingPlanId);
        }
        process.setNumCourseNeedFinish(lstSessionParticipant.size());
        int numCourseNeedFinish = 0;
        for (SessionParticipant sessionParticipant : lstSessionParticipant) {
            if (sessionParticipant.getCompletionStatus() == 0) {
                numCourseNeedFinish = numCourseNeedFinish + 1;
            }
        }
        process.setNumCourseNeedFinish(numCourseNeedFinish);
        return Utils.generateSuccessResponseEntity(process);
    }

    @Override
    public ResponseEntity<?> acceptCourse(int sessionParticipantId) {
        try {
            TrainingAction trainingAction = this.trainingActionRepository
                    .findTrainingActionBySessionParticipantId(sessionParticipantId);
            SessionParticipant sessionParticipant = sessionParticipantRepository
                    .getSessionParticipantById(sessionParticipantId);
            if (trainingAction == null) {
                logger.error(Message.MSG_TRAINING_ACTION_NOT_FOUND + " sessionParticipantId = " + sessionParticipantId);
                throw new EditObjectException(Message.MSG_TRAINING_ACTION_NOT_FOUND);
            }
            if (sessionParticipant == null) {
                logger.error(Message.MSG_TRAINING_ACTION_NOT_FOUND + " sessionParticipantId = " + sessionParticipantId);
                throw new EditObjectException(Message.MSG_TRAINING_ACTION_NOT_FOUND);
            }
            this.sessionParticipantRepository.updateSessionParticipant(sessionParticipant);
            trainingAction.setStatus(TrainingActionStatus.ACCEPTED.getValue());
            this.trainingActionRepository.updateTrainingAction(trainingAction);
            return Utils.generateSuccessResponseEntity(trainingAction);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - acceptCourse " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION, e.getMessage());

        }
    }

    @Override
    public ResponseEntity<?> denyCourse(int sessionParticipantId) {
        try {
            TrainingAction trainingAction = this.trainingActionRepository
                    .findTrainingActionBySessionParticipantId(sessionParticipantId);
            if (trainingAction == null) {
                logger.error(Message.MSG_TRAINING_ACTION_NOT_FOUND + " sessionParticipantId = " + sessionParticipantId);
                throw new EditObjectException(Message.MSG_TRAINING_ACTION_NOT_FOUND);
            }
            trainingAction.setStatus(TrainingActionStatus.DENIED.getValue());
            this.trainingActionRepository.updateTrainingAction(trainingAction);
            return Utils.generateSuccessResponseEntity(trainingAction);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - acceptCourse " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION, e.getMessage());

        }
    }

    @Override
    public ResponseEntity<?> acceptOrDenyCourseByManager(int sessionId, int userId, int courseId, int status,
            String comment) throws MessagingException, IOException, ObjectNotFoundException {
        SessionParticipant sessionParticipant = sessionParticipantRepository.getSessionParticipant(sessionId, userId);
        if (sessionParticipant == null) {
            sessionParticipantRepository.addSessionParticipantFromSession(sessionId, userId);
        }

        Course course = courseRepository.getCourseById(courseId);
        course.setPreTrainingComment(comment);
        courseRepository.updateCourse(course);
        SessionParticipant sessionParticipantUpdate = sessionParticipantRepository.getSessionParticipant(sessionId,
                userId);
        if (status == ManagerActionStatus.ACCEPTED.getValue()) {
            Employee employee = new Employee();
            User trianee = userRepository.getUserById(sessionParticipantUpdate.getUserId());
            employee.setEmailAddress(trianee.getEmail());
            employee.setEmployeeId(trianee.getId());
            emailService.sendEmailToTrainee(employee, sessionId);
        }
        sessionParticipantUpdate.setManagerActionStatus(status);
        sessionParticipantUpdate.setPreTrainingComment(comment);
        sessionParticipantRepository.updateSessionParticipant(sessionParticipantUpdate);
        return Utils.generateSuccessResponseEntity("updateStatusOfCourse");
    }

    @Override
    public ResponseEntity<?> getCourseByTrainingPlan(int userId, int trainingPlanId) throws ObjectNotFoundException {
        List<com.amway.lms.backend.model.Course> courses = new ArrayList<>();
        List<SessionParticipant> lstSessionParticipant = sessionParticipantRepository
                .getSessionParticipantByUserId(userId);
        for (SessionParticipant sessionParticipant : lstSessionParticipant) {
            try {
                if (trainingPlanId == 0) {
                    courses.add(getCourse(sessionParticipant));
                } else {
                    courses.add(getCourseForTrainingPlan(sessionParticipant, trainingPlanId));
                }
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
                System.out.println();
            }

        }
        return Utils.generateSuccessResponseEntity(courses);

    }

    private com.amway.lms.backend.model.Course getCourseForTrainingPlan(SessionParticipant sessionParticipant,
            int trainingPlanId) throws ObjectNotFoundException {

        Session session = sessionRepository.getSessionById(sessionParticipant.getSessionId());
        if (session == null) {
            throw new ObjectNotFoundException(ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);

        }
        CoursesOfTrainingPlan coursesOfTrainingPlan = courseOfTrainingPlanRepository
                .getCourseOfTrainingPlan(trainingPlanId, session.getCourseId());
        if (coursesOfTrainingPlan == null) {
            throw new ObjectNotFoundException(ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }

        Course course = courseRepository.getCourseById(session.getCourseId());
        if (course == null) {
            throw new ObjectNotFoundException(ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);

        }
        com.amway.lms.backend.model.Course courseModel = new com.amway.lms.backend.model.Course();
        courseModel.setSessionParticipantId(sessionParticipant.getId());
        courseModel.setCourseId(course.getId());
        courseModel.setTraineeId(sessionParticipant.getUserId());
        if (sessionParticipant != null) {
            courseModel.setCompletionStatus(converCompletionStatus(sessionParticipant.getCompletionStatus()));
        }
        User trainee = userRepository.getUserById(sessionParticipant.getUserId());
        courseModel.setCourseName(course.getName());
        courseModel.setTraineeName(trainee.getUserID());
        courseModel.setStartTime(Utils.convertDateToString(session.getStartTime()));
        courseModel.setEndTime(Utils.convertDateToString(session.getEndTime()));
        courseModel.setCourseStatus(converStatus(sessionParticipant.getCompletionStatus()));
        courseModel.setTime(session.getStartTime());
        courseModel.setQuizStatus(sessionParticipant.getQuizStatus() != 0 ? "Passed" : "Avaiable");
        TrainingAction trainingAction = this.trainingActionRepository
                .findTrainingActionBySessionParticipantId(sessionParticipant.getId());

        courseModel.setTrainingActionStatus(trainingAction == null ? -1 : trainingAction.getStatus());
        courseModel.setSessionName(session.getName());
        courseModel.setIsOffline(course.getIsOffline());
        courseModel.setManagerActionStatus(converManagerActionStatus(sessionParticipant.getManagerActionStatus()));
        courseModel.setSessionStatus(session.getStatus());
        courseModel.setSessionId(session.getId());
        courseModel.setAjpvpoint(course.getPassQuizAJPV());
        courseModel.setManagerActionStatusValue(sessionParticipant.getManagerActionStatus());
        // courseModel.setApproval(convertApproval(String.valueOf(courseParticipant.getConfirmationStatus())));
        return courseModel;
    }

    // private List<SessionParticipant> getSessionParticipantsByPlanAndUser(
    // int userId, int trainingPlanId) throws ObjectNotFoundException {
    // List<SessionParticipant> sessionParticipants = new
    // ArrayList<SessionParticipant>();
    // List<PreparatoryParticipant> preparatoryParticipants =
    // this.preparatoryParticipantRepository
    // .getPreparatoryParticipant(trainingPlanId, userId);
    // for (PreparatoryParticipant preparatoryParticipant :
    // preparatoryParticipants) {
    // List<Session> sessions = this.sessionRepository
    // .getSessionsByCourseId(preparatoryParticipant.getCourseId());
    // for (Session session : sessions) {
    // SessionParticipant sessionParticipant = this.sessionParticipantRepository
    // .getSessionParticipant(session.getId(), userId);
    // if (sessionParticipant != null) {
    // sessionParticipants.add(sessionParticipant);
    // }
    // }
    // }
    //
    // return sessionParticipants;
    // }

    @Override
    public String getCommentBySessionParticipantId(int sessionParticipantId) throws ObjectNotFoundException {
        SessionParticipant sessionParticipant = sessionParticipantRepository
                .getSessionParticipantById(sessionParticipantId);
        return Utils.generateSuccessResponseString(sessionParticipant.getComment());
    }

    @Override
    public ResponseEntity<?> getForm(int sessionParticipantId) {
        try {
            TrainingAction trainingAction = this.trainingActionRepository
                    .findTrainingActionBySessionParticipantId(sessionParticipantId);
            if (trainingAction == null)
                throw new ObjectNotFoundException(Message.MSG_TRAINING_ACTION_NOT_FOUND
                        + " where sessionParticipantId = " + sessionParticipantId);

            Timestamp currentDay = Utils.getCurrentTime();
            if (trainingAction.getStatus() == AmwayEnum.TrainingActionStatus.SUBMITTED.getValue()) {
                return Utils.generateSuccessResponseEntity(1);
            } else if (trainingAction.getStatus() == AmwayEnum.TrainingActionStatus.ACCEPTED.getValue()
                    && trainingAction.getResult() == 0
                    && !trainingAction.getEndTime().after(Common.setTimeToZero(currentDay))) {
                return Utils.generateSuccessResponseEntity(2);
            } else {
                return Utils.generateSuccessResponseEntity(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getForm " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> evaluate(TrainingAction trainingAction) {
        try {
            TrainingAction taUpdate = this.trainingActionRepository
                    .findTrainingActionBySessionParticipantId(trainingAction.getSessionParticipantId());
            if (taUpdate == null)
                throw new ObjectNotFoundException(Message.MSG_TRAINING_ACTION_NOT_FOUND
                        + " where sessionParticipantId = " + trainingAction.getSessionParticipantId());

            SessionParticipant spUpdate = this.sessionParticipantRepository
                    .getSessionParticipantById(taUpdate.getSessionParticipantId());
            if (spUpdate == null)
                throw new ObjectNotFoundException(Message.MSG_SESSION_PARTICIPANT_NOT_FOUND + " where id = "
                        + taUpdate.getSessionParticipantId());

            taUpdate.setResult(trainingAction.getResult());
            taUpdate.setComment(trainingAction.getComment());
            // Set Completed status for sessionParticipant
            spUpdate.setCompletionStatus(AmwayEnum.CompletionStatus.COMPLETED.getValue());
            this.trainingActionRepository.updateTrainingAction(taUpdate);
            this.sessionParticipantRepository.updateSessionParticipant(spUpdate);
            updateAJPV(spUpdate);
            return Utils.generateSuccessResponseEntity(taUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - evaluate " + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    private void updateAJPV(SessionParticipant sessionParticipant) {
        logger.info("---updateAJPV() when TrainingAction Completed");

        if (sessionParticipant == null) {
            logger.info("Session Participant is null");
            return;
        }

        Session session = sessionRepository.getSessionById(sessionParticipant.getSessionId());
        if (session == null) {
            logger.info("Session is null");
            return;
        }

        Course course = courseRepository.getCourseById(session.getCourseId());
        if (course == null) {
            logger.info("Course is null");
            return;
        }

        if (course.getCompletionAJPV() == 0) {
            logger.info("AJPV equal 0. CourseId = " + course.getId());
            return;
        }

        User user = userRepository.getUserById(sessionParticipant.getUserId());
        if (user == null) {
            logger.info("User is null");
            return;
        }

        user.setAjpv(user.getAjpv() + course.getCompletionAJPV());
        userRepository.editUser(user);
    }

    private EventCourse getEventCourse(com.amway.lms.backend.model.Course courseModel,
            SessionParticipant sessionParticipant) {
        EventCourse eventCourse = new EventCourse();
        eventCourse.setBackgroundColor(convertColor(courseModel.getCourseStatus()));
        String title = new StringBuilder().append(courseModel.getCourseName()).append("(")
                .append(courseModel.getSessionName()).append(")").toString();
        eventCourse.setTitle(title);
        eventCourse.setBorderColor(convertColor(courseModel.getCourseStatus()));
        eventCourse.setCourseId(courseModel.getCourseId());
        eventCourse.setSessionParticipantId(sessionParticipant.getId());
        eventCourse.setStart(courseModel.getTime());
        eventCourse.setEnd(courseModel.getTime());
        // status of course_participants
        eventCourse.setCourseStatus(courseModel.getCompletionStatus());
        eventCourse.setTrainingActionStatus(courseModel.getTrainingActionStatus());
        eventCourse.setIsOffline(courseModel.getIsOffline());
        return eventCourse;
    }

    @Override
    public List<Integer> getListApprovalManagerIdOfHod(int hodId) {
        return userRepository.getListApprovalManagerIdByHodId(hodId);
    }

}