package com.amway.lms.backend.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amway.lms.backend.common.ComparatorUtils;
import com.amway.lms.backend.common.ComparatorUtils.CourseComparator;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Message;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Category;
import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.entity.CourseParticipant;
import com.amway.lms.backend.entity.Location;
import com.amway.lms.backend.entity.Room;
import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.EmailInfor;
import com.amway.lms.backend.model.SessionModel;
import com.amway.lms.backend.repository.CategoryRepository;
import com.amway.lms.backend.repository.CourseOfTrainingPlanRepository;
import com.amway.lms.backend.repository.CourseParticipantRepository;
import com.amway.lms.backend.repository.CourseRepository;
import com.amway.lms.backend.repository.LocationRepository;
import com.amway.lms.backend.repository.RoomRepository;
import com.amway.lms.backend.repository.SessionRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.CourseService;
import com.amway.lms.backend.service.EmailService;

/**
 * @author: Hung (Charles) V. PHAM
 */

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    private SessionRepository sessionDao;

    @Autowired
    private CourseRepository courseDao;

    @Autowired
    private UserRepository userDao;

    @Autowired
    private CourseParticipantRepository courseParticipantDao;

    @Autowired
    private CategoryRepository categoryDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LocationRepository locationDao;

    @Autowired
    private RoomRepository roomDao;

    @Autowired
    private CourseOfTrainingPlanRepository courseOfTrainingPlanDao;

    @Override
    public ResponseEntity<?> getCoursesList(int isActive, Integer trainingPlanId) {
        try {
            List<Course> courseList = new ArrayList<>();
            courseList = this.courseDao.getCoursesList(isActive, trainingPlanId);
            return Utils.generateSuccessResponseEntity(addCategoryToCourseList(courseList));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCoursesList " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> getCoursesListByStatus(int firstItem, int maxItem, int isActive) {
        try {
            List<Course> courseList = new ArrayList<>();
            courseList = this.courseDao.getCoursesListByStatus(firstItem, maxItem, isActive);
            return Utils.generateSuccessResponseEntity(addCategoryToCourseList(courseList));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCoursesListByStatus " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> addCourse(Course course) {
        try {
            this.courseDao.addCourse(course);
            // Add at least one session to course
            // Session session = new Session();
            // session.setName("Session 1");
            // session.setCourseId(course.getId());
            // session.setIsInternalTrainer(1);
            // this.sessionDao.addSession(session);
            return Utils.generateSuccessResponseEntity(course);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addNewCourse " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> updateCoursesStatus(List<Course> courses, int isActive) {
        try {
            // get List of Courses to return
            List<Course> coursesReturn = new ArrayList<Course>();
            for (Course course : courses) {
                Course courseForUpdate = courseDao.getCourseById(course.getId());
                courseForUpdate.setIsActive(isActive);
                this.courseDao.updateCoursesStatus(courseForUpdate);
                coursesReturn.add(courseForUpdate);
            }
            return Utils.generateSuccessResponseEntity(addCategoryToCourseList(coursesReturn));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - updateCoursesStatus " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> getCoursesByNameOrCode(String searchString, int isActive) {
        try {
            List<Course> courses = this.courseDao.getCoursesByNameOrCode(searchString, isActive);
            if (courses == null || courses.size() == 0)
                throw new ObjectNotFoundException(Message.MSG_COURSE_NOT_FOUND);
            return Utils.generateSuccessResponseEntity(addCategoryToCourseList(courses));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCoursesByNameOrCode " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());

        }
    }

    @Override
    public ResponseEntity<?> getCoursesByUser(String searchString) {
        try {
            return new ResponseEntity<>(this.courseDao.getCoursesByUser(searchString), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("EXCEPTION - getCoursesByUser " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getCoursesById(int courseId) {
        try {
            Course course = this.courseDao.getCourseById(courseId);
            if (course == null) {
                throw new ObjectNotFoundException(Message.MSG_COURSE_NOT_FOUND + " courseId = " + courseId);
            }
            course.setCategory(this.categoryDao.getCategoryById(course.getCategoryId()));
            return Utils.generateSuccessResponseEntity(course);
        } catch (Exception e) {
            logger.error("EXCEPTION - getCourse " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getCoursesApprovedList(int firstItem, int maxItem) {
        try {
            List<Course> courseList = this.courseDao.getCoursesApprovedList(firstItem, maxItem);
            if (courseList == null || courseList.size() == 0)
                throw new ObjectNotFoundException(Message.MSG_COURSE_NOT_FOUND);
            for (int i = 0; i < courseList.size(); i++) {
                int categoryId = courseList.get(i).getCategoryId();
                courseList.get(i).setCategory(this.categoryDao.getCategoryById(categoryId));
            }
            return Utils.generateSuccessResponseEntity(courseList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCoursesApprovedList " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    protected List<Course> addCategoryToCourseList(List<Course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            int categoryId = courseList.get(i).getCategoryId();
            logger.debug("getCourseList, categoryId=" + categoryId);
            Category category = this.categoryDao.getCategoryById(categoryId);
            logger.debug("getCourseList, category" + category);
            courseList.get(i).setCategory(category);
        }
        return courseList;
    }

    private List<Course> setCourseListSession(List<Course> courses) {
        for (Course course : courses) {
            // Set Category for course of item List
            int categoryId = course.getCategoryId();
            course.setCategory(this.categoryDao.getCategoryById(categoryId));

            // Set Session for course of item List
            int courseId = course.getId();
            course.setSessions(this.sessionDao.getSessionsByCourseId(courseId));
        }

        return courses;
    }

    @Override
    public ResponseEntity<?> getCoursesInProgressList(int firstItem, int maxItem) {
        try {

            List<Course> courseList = this.courseDao.getCoursesImprogressList(firstItem, maxItem);
            if (courseList == null || courseList.size() == 0)
                throw new ObjectNotFoundException(Message.MSG_COURSE_NOT_FOUND);

            List<Course> courseListSession = setCourseListSession(courseList);
            return Utils.generateSuccessResponseEntity(courseListSession);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCoursesInProgressList " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> sendingEmailToPrticipants(final int courseId, EmailInfor emailInfor) throws IOException {
        try {
            // Update status
            for (Integer userId : emailInfor.getLstUserId()) {
                CourseParticipant courseParticipant = courseParticipantDao.getCourseParticipant(courseId, userId);
                courseParticipant.setConfirmationStatus(1);
                courseParticipantDao.updateCheckIn(courseParticipant);
            }
            // TODO get email template:
            emailService.sendEmail(emailInfor);
        } catch (MessagingException e) {
            return Utils.generateFailureResponseEntity(700, "Invalid email address");
        }
        return Utils.generateSuccessResponseEntity("The Email send");
    }

    @Override
    public ResponseEntity<?> getCoursesToAddTrainingPlan(int trainingPlanId) {
        try {
            List<Course> courseList = this.courseDao.getCoursesToAddTrainingPlan(trainingPlanId);
            if (courseList == null || courseList.size() == 0)
                throw new ObjectNotFoundException(Message.MSG_COURSE_NOT_FOUND);
            Collections.sort(courseList, ComparatorUtils.COURSE_COMPARATOR);
            return Utils.generateSuccessResponseEntity(addCategoryToCourseList(courseList));

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCoursesList " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    public ResponseEntity<?> getCoursesListNotHaveQuiz() throws Exception {

        return Utils.generateSuccessResponseEntity(courseDao.getCourseListNotHaveQuiz());
    }

    @Override
    public ResponseEntity<?> getSessionsByCourseId(int courseId) {
        try {
            List<Session> sessions = new ArrayList<>();
            sessions = this.sessionDao.getSessionsByCourseId(courseId);
            List<SessionModel> sessionModels = new LinkedList<>();
            Timestamp today = new Timestamp(System.currentTimeMillis());
            for (Session session : sessions) {
                Location location = this.locationDao.getLocationById(session.getLocationId());
                Room room = this.roomDao.getRoomById(session.getRoomId());
                SessionModel sessionModel = new SessionModel();
                // BeanUtils.copyProperties(session, sessionModel);
                sessionModel.setLocation(location);
                sessionModel.setRoom(room);
                sessionModel.setCourseId(courseId);
                sessionModel.setId(session.getId());
                sessionModel.setSessionName(session.getName());
                sessionModel.setDescription(session.getDescription());
                sessionModel.setEndTimeStr(session.getEndTimeStr());
                sessionModel.setStartTimeStr(session.getStartTimeStr());
                sessionModel.setLocationId(session.getLocationId());
                sessionModel.setRoomId(session.getRoomId());
                if (session.getIsInternalTrainer() == 1) {
                    User trainer = this.userDao.getUserById(session.getTrainerId());
                    sessionModel.setTrainerId(session.getTrainerId());
                    sessionModel.setTrainerFullName(new StringBuilder(trainer.getFirstName()).append(" ")
                            .append(trainer.getLastName()).toString());
                } else {
                    sessionModel.setTrainerFullName(session.getTrainerFullName());
                }
                sessionModel.setIsInternalTrainer(session.getIsInternalTrainer());
                sessionModel.setCoordinatorId(session.getCoordinatorId());
                sessionModel.setCoordinatorFullName(session.getCoordinatorFullName());
                sessionModel.setStartTime(session.getStartTime());
                sessionModel.setEndTime(session.getEndTime());
                if (session.getStartTime().before(today)) {
                    sessionModel.setAddParticipant(false);
                } else {
                    sessionModel.setAddParticipant(true);
                }
                if (session.getEndTime().after(today)) {
                    sessionModel.setEndSession(false);
                } else {
                    sessionModel.setEndSession(true);
                }
                sessionModels.add(sessionModel);
            }
            return Utils.generateSuccessResponseEntity(sessionModels);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getSessionsByCourseId " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> updateCoursesAJPV(Course course) {
        try {
            Course courseForUpdate = courseDao.getCourseById(course.getId());
            if (courseForUpdate == null)
                throw new EditObjectException(Message.MSG_COURSE_NOT_FOUND + " courseId = " + course.getId());
            courseForUpdate.setParticipationAJPV(course.getParticipationAJPV());
            courseForUpdate.setCompletionAJPV(course.getCompletionAJPV());
            courseForUpdate.setPassQuizAJPV(course.getPassQuizAJPV());
            this.courseDao.editCourse(courseForUpdate);
            return Utils.generateSuccessResponseEntity(courseForUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - updateCoursesAJPV " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getCoursesOfTrainingPlan(int trainingPlanId) {
        try {
            List<Course> courseList = this.courseDao.getCoursesOfTrainingPlan(trainingPlanId);
            if (courseList == null || courseList.size() == 0)
                throw new ObjectNotFoundException(Message.MSG_COURSE_NOT_FOUND);
            for (int i = 0; i < courseList.size(); i++) {
                int courseId = courseList.get(i).getId();
                int isCourseRequired = this.courseOfTrainingPlanDao.getCourseOfTrainingPlan(trainingPlanId, courseId)
                        .getIsCourseRequired();
                courseList.get(i).setIsCourseRequired(isCourseRequired);
            }
            Collections.sort(courseList, ComparatorUtils.COURSE_COMPARATOR);
            return Utils.generateSuccessResponseEntity(addCategoryToCourseList(courseList));

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCoursesList " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    public List<Course> getCoursesApprovedListExport(int firstItem, int maxItem) {
        List<Course> courseList = this.courseDao.getCoursesApprovedList(firstItem, maxItem);
        if (courseList == null || courseList.size() == 0) {
            return null;
        }
        for (Course course : courseList) {
            course.setSessions(this.sessionDao.getSessionsByCourseIdAndStatus(course.getId(), 3));
        }
        return courseList;
    }

    @Override
    public ResponseEntity<?> getCoursesStartByToday(String courseName) {
        try {
            List<Course> courses = this.courseDao.getCoursesStartByToday(courseName);
            if (courses == null || courses.size() == 0)
                throw new ObjectNotFoundException(Message.MSG_COURSE_NOT_FOUND);
            return Utils.generateSuccessResponseEntity(getSessionsFromCourses(courses));

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCoursesList " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    private List<Session> getSessionsFromCourses(List<Course> courses) {
        List<Session> sessionsReturn = new ArrayList<Session>();
        for (Course course : courses) {
            // Set Session for course of item List
            int courseId = course.getId();
            List<Session> sessions = this.sessionDao.getSessionsStartToday(courseId);
            for (Session session : sessions) {
                session.setCourseName(course.getName());
                sessionsReturn.add(session);
            }
        }

        return sessionsReturn;
    }

    @Override
    public ResponseEntity<?> getCoursesListForPostTrainingSurvey() throws Exception {
        try {
            logger.info("getCoursesListForPostTrainingSurvey()");
            List<Course> courses = courseDao.getCourseListForPostTrainingSurvey();
            return Utils.generateSuccessResponseEntity(courses);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getCoursesListForPostTrainingSurvey " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    
}
