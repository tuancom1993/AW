package com.amway.lms.backend.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Message;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.entity.CoursesOfTrainingPlan;
import com.amway.lms.backend.entity.PreparatoryParticipant;
import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.entity.SessionParticipant;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.repository.CourseOfTrainingPlanRepository;
import com.amway.lms.backend.repository.CourseRepository;
import com.amway.lms.backend.repository.LocationRepository;
import com.amway.lms.backend.repository.PreparatoryParticipantRepository;
import com.amway.lms.backend.repository.RoomRepository;
import com.amway.lms.backend.repository.SessionParticipantRepository;
import com.amway.lms.backend.repository.SessionRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.EmailService;
import com.amway.lms.backend.service.SessionService;

/**
 * @author: Hung (Charles) V. PHAM
 */

@Service
@Transactional
public class SessionServiceImpl implements SessionService {
    private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Autowired
    private SessionRepository sessionDao;

    @Autowired
    private CourseRepository courseDao;

    @Autowired
    private LocationRepository locationDao;

    @Autowired
    private RoomRepository roomDao;

    @Autowired
    private UserRepository userDao;

    @Autowired
    private PreparatoryParticipantRepository preparatoryParticipantDao;

    @Autowired
    private SessionParticipantRepository sessionParticipantDao;

    @Autowired
    private CourseOfTrainingPlanRepository courseOfTrainingPlanDao;

    @Autowired
    private EmailService emailService;

    @Override
    public ResponseEntity<?> getSessionDetail(int sessionId) {
        try {
            Session session = this.sessionDao.getSessionById(sessionId);
            if (session == null)
                throw new ObjectNotFoundException(Message.MSG_SESSION_NOT_FOUND + " sessionId = " + sessionId);
            int locationId = session.getLocationId();
            int roomId = session.getRoomId();
            int courseId = session.getCourseId();
            session.setLocation(this.locationDao.getLocationById(locationId));
            session.setRoom(this.roomDao.getRoomById(roomId));
            session.setCourseName(this.courseDao.getCourseById(courseId).getName());
            return Utils.generateSuccessResponseEntity(session);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getSessionDetail " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addNewSession(Session session) {
        try {
            int courseId = session.getCourseId();
            Course courseToUpdate = this.courseDao.getCourseById(courseId);
            if (courseToUpdate == null)
                throw new AddObjectException(Message.MSG_COURSE_NOT_FOUND + " courseId = " + courseId);
            // add new session
            int sessionIdx = courseToUpdate.getNumberOfSession() + 1;
            User coordinator = userDao.getUserById(session.getCoordinatorId());
            if (coordinator != null) {
                session.setCoordinatorFullName(new StringBuilder(coordinator.getFirstName()).append(" ")
                        .append(coordinator.getLastName()).toString());
            } else {
                session.setCoordinatorFullName("N/A");
            }

            User trainer = null;
            if (session.getIsInternalTrainer() == 1) {
                trainer = userDao.getUserById(session.getTrainerId());
                if (trainer == null)
                    throw new ObjectNotFoundException(Message.MSG_TRAINER_NOT_FOUND);
                String trainerFullName = trainer.getFirstName() + " " + trainer.getLastName();
                session.setTrainerFullName(trainerFullName);
            } else {
                session.setTrainerId(0);
                session.setTrainerFullName(session.getTrainerFullName());
            }
            session.setName("Session " + sessionIdx);

            session.setStartTime(Common.getTimeStampFromString(session.getStartTimeStr(),
                    Common.DATE_TIME_FORMAT_DD_MMM_YYYY_AM_PM));
            session.setEndTime(
                    Common.getTimeStampFromString(session.getEndTimeStr(), Common.DATE_TIME_FORMAT_DD_MMM_YYYY_AM_PM));
            this.sessionDao.addSession(session);

            // Increase number of session
            courseToUpdate.setNumberOfSession(courseToUpdate.getNumberOfSession() + 1);
            this.courseDao.editCourse(courseToUpdate);

            // Check to add participants belong to training plan
            List<CoursesOfTrainingPlan> courseOfTrainingPlanList = this.courseOfTrainingPlanDao
                    .getCourseOfTrainingPlanByCourseId(courseId);
            if (courseOfTrainingPlanList != null && courseOfTrainingPlanList.size() > 0) {
                for (CoursesOfTrainingPlan coursesOfTrainingPlan : courseOfTrainingPlanList) {
                    int trainingPlanId = coursesOfTrainingPlan.getTrainingPlanId();
                    List<PreparatoryParticipant> preparatoryParticipants = this.preparatoryParticipantDao
                            .getPreparatoryParticipantByTrainingPlanId(trainingPlanId);
                    for (PreparatoryParticipant preparatoryParticipant : preparatoryParticipants) {
                        this.sessionParticipantDao.addSessionParticipantFromPlan(session.getId(),
                                preparatoryParticipant.getUserId());
                    }
                }
            }
            if (coordinator != null) {
                // Send email to coordinator
                emailService.sendEmailCoordinator(Common.getEmployee(coordinator), session.getId());
            }

            if (trainer != null) {
                // Send email to trainer
                emailService.sendEmailToTrainer(Common.getEmployee(trainer), session.getId());
            }

            return Utils.generateSuccessResponseEntity(session);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addNewSession " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateSession(Session session) {
        try {
            List<String> emails = new ArrayList<>();
            Session sessionToUpdate = this.sessionDao.getSessionById(session.getId());
            if (sessionToUpdate == null)
                throw new EditObjectException(Message.MSG_SESSION_NOT_FOUND + " sessionId = " + session.getId());
            // Don't allow updateSession if it has started
            /*
             * if (sessionToUpdate.getEndTime().after(Utils.getCurrentTime()))
             * throw new EditObjectException(Message.MSG_SESSION_STARTED +
             * " sessionId = " + session.getId());
             */
            int oldCoordinatorId = sessionToUpdate.getCoordinatorId();
            int oldTrainerId = sessionToUpdate.getTrainerId();
            int oldIsInternalTrainer = sessionToUpdate.getIsInternalTrainer();

            sessionToUpdate.setId(session.getId());
            sessionToUpdate.setDescription(session.getDescription());
            sessionToUpdate.setCourseId(session.getCourseId());
            sessionToUpdate.setLocationId(session.getLocationId());
            sessionToUpdate.setRoomId(session.getRoomId());
            sessionToUpdate.setStartTime(Common.getTimeStampFromString(session.getStartTimeStr(),
                    Common.DATE_TIME_FORMAT_DD_MMM_YYYY_AM_PM));
            sessionToUpdate.setEndTime(
                    Common.getTimeStampFromString(session.getEndTimeStr(), Common.DATE_TIME_FORMAT_DD_MMM_YYYY_AM_PM));
            sessionToUpdate.setCoordinatorId(session.getCoordinatorId());
            if (session.getIsInternalTrainer() == 1) {
                User trainer = this.userDao.getUserById(session.getTrainerId());
                sessionToUpdate.setTrainerId(session.getTrainerId());
                sessionToUpdate.setTrainerFullName(
                        new StringBuilder(trainer.getFirstName()).append(" ").append(trainer.getLastName()).toString());
            } else {
                sessionToUpdate.setTrainerFullName(session.getTrainerFullName());
            }
            sessionToUpdate.setCoordinatorId(session.getCoordinatorId());
            User coordinator = userDao.getUserById(session.getCoordinatorId());
            if (coordinator != null) {
                sessionToUpdate.setCoordinatorFullName(new StringBuilder(coordinator.getFirstName()).append(" ")
                        .append(coordinator.getLastName()).toString());
            } else {
                sessionToUpdate.setCoordinatorFullName("N/A");
            }
            sessionToUpdate.setIsInternalTrainer(session.getIsInternalTrainer());
            this.sessionDao.updateSession(sessionToUpdate);

            // Send email to coordinator
            if (oldCoordinatorId != sessionToUpdate.getCoordinatorId()) {
                /*
                 * this.emailService.sendEmailChangeInfoSession(
                 * Common.getEmployee(this.userDao.getUserById(oldCoordinatorId)
                 * ), sessionToUpdate.getId());
                 */
                addEmailToList(emails, oldCoordinatorId);
            }
            /*
             * this.emailService.sendEmailChangeInfoSession(
             * Common.getEmployee(this.userDao.getUserById(sessionToUpdate.
             * getCoordinatorId())), sessionToUpdate.getId());
             */
            addEmailToList(emails, sessionToUpdate.getCoordinatorId());

            // Send email to trainer
            if (oldIsInternalTrainer == 1 && oldTrainerId != session.getTrainerId()) {
                /*
                 * this.emailService.sendEmailChangeInfoSession(Common.
                 * getEmployee(this.userDao.getUserById(oldTrainerId)),
                 * sessionToUpdate.getId());
                 */
                addEmailToList(emails, oldTrainerId);
            }
            if (sessionToUpdate.getIsInternalTrainer() == 1) {
                /*
                 * this.emailService.sendEmailChangeInfoSession(
                 * Common.getEmployee(this.userDao.getUserById(sessionToUpdate.
                 * getTrainerId())), sessionToUpdate.getId());
                 */
                addEmailToList(emails, sessionToUpdate.getTrainerId());
            }

            // Send email to trainees
            List<SessionParticipant> sessionParticipantList = this.sessionParticipantDao
                    .getSessionParticipantBySessionId(session.getId());
            for (SessionParticipant sessionParticipant : sessionParticipantList) {
                /*
                 * User trainee =
                 * this.userDao.getUserById(sessionParticipant.getUserId());
                 * this.emailService.sendEmailChangeInfoSession(Common.
                 * getEmployee(trainee), session.getId());
                 */
                addEmailToList(emails, sessionParticipant.getUserId());
            }
            emails = clearDuplicate(emails);
            emailService.sendEmailChangeInfoSession((String[]) emails.toArray(new String[emails.size()]), sessionToUpdate.getId());
            return Utils.generateSuccessResponseEntity(session);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addNewSession " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    private void addEmailToList(List<String> emails, int userId) {
        try {
            User user = userDao.getUserById(userId);
            if (user == null)
                throw new Exception("addEmailToList -- Cannot find User by id = " + userId);
            String email = user.getEmail();
            if (email == null || "".equals(email))
                throw new Exception("addEmailToList -- Email is null or empty");
            emails.add(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private List<String> clearDuplicate(List<String> list) {
        Set<String> set = new HashSet<>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    @Override
    public ResponseEntity<?> deleteSession(int sessionId) {
        try {
            Session sessionToDelete = this.sessionDao.getSessionById(sessionId);
            if (sessionToDelete == null)
                throw new DeleteObjectException(Message.MSG_SESSION_NOT_FOUND + " sessionId = " + sessionId);
            Course courseToUpdate = this.courseDao.getCourseById(sessionToDelete.getCourseId());
            if (courseToUpdate == null)
                throw new DeleteObjectException(
                        Message.MSG_COURSE_NOT_FOUND + " courseId = " + sessionToDelete.getCourseId());
            this.sessionDao.deleteSession(sessionToDelete);
            // decrease number of session for course
            courseToUpdate.setNumberOfSession(courseToUpdate.getNumberOfSession() - 1);
            this.courseDao.editCourse(courseToUpdate);
            return Utils.generateSuccessResponseEntity(sessionToDelete);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addNewSession " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public void sendEmailToTrainerForPostSurveyCronJob()
            throws MessagingException, IOException, ObjectNotFoundException {
        // TODO Auto-generated method stub
        logger.info("sendEmailToTrainerForPostSurveyCronJob Is running...");
        Timestamp today = new Timestamp(System.currentTimeMillis());
        List<Session> sessionList = this.sessionDao.getSessionsNotSentEmailToTrainer();
        for (Session session : sessionList) {
            try {
                if (session.getEndTime().before(today)) {
                    // Send Email to trainer for post survey
                    User trainer = this.userDao.getUserById(session.getTrainerId());
                    if (trainer == null) {
                        logger.error("Cannot find Trainer with id = " + session.getTrainerId() + " in sessionId "
                                + session.getId());
                        continue;
                    }
                    this.emailService.sendEmailToTrainerForPostSurvey(Common.getEmployee(trainer), session.getId());
                    session.setIsSentEmail(1);
                    this.sessionDao.updateSession(session);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
