package com.amway.lms.backend.rest;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.common.AmwayEnum.TraineeActionStatus;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.TrainingNeed;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.ActionPlan;
import com.amway.lms.backend.model.Course;
import com.amway.lms.backend.service.LearningDashboardService;
import com.amway.lms.backend.service.TraineeNeedService;

@RestController
@RequestMapping("/api/v1/self")
public class LearningSelfDashboardController {
    private static final Logger logger = LoggerFactory
            .getLogger(LearningSelfDashboardController.class);

    @Autowired
    private LearningDashboardService learningDashboardService;

    @Autowired
    private TraineeNeedService traineeNeedService;

    /**
     * API for learningDashboardRoaldMap Option
     * 
     * @param userId
     * @return
     * @throws ObjectNotFoundException
     */
    @RequestMapping(value = "/dashboardRoaldMap/courses/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCourse(
            @PathVariable("userId") int userId,
            @RequestParam(name = "trainingPlanId", required = true) int trainingPlanId,
            @RequestParam(name = "year", required = true) int year)
            throws ObjectNotFoundException {
        logger.debug("getAllCourse");
        return learningDashboardService.getAllCourse(Arrays.asList(userId),
                trainingPlanId, year);
    }
    
    @RequestMapping(value = "/dashboardRoaldMap/courses/{userId}/status/{completionStatus}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCourse(
            @PathVariable("userId") int userId,
            @PathVariable("completionStatus") int completionStatus)
            throws ObjectNotFoundException {
        logger.debug("getAllCourse");
        return learningDashboardService.getCourseListByCompletionStatus(userId, completionStatus);
    }

    @RequestMapping(value = "/dashboardRoaldMap/courseParticipant/{sessionParticipantId}", method = RequestMethod.GET)
    public ResponseEntity<?> getLearningDashboardRoaldMap(
            @PathVariable("sessionParticipantId") int sessionParticipantId)
            throws ObjectNotFoundException {
        logger.debug("getCourseInformation");
        return learningDashboardService
                .getCourseInformation(sessionParticipantId);
    }

    /**
     * Create training need
     * 
     */

    @RequestMapping(value = "/dashboardRoaldMap/traineeNeed", method = RequestMethod.POST)
    public ResponseEntity<?> createNewTraineeNeed(
            @RequestBody final TrainingNeed trainingNeed) throws SQLException,
            Exception {
        logger.info("createNewTraineeNeed: " + trainingNeed);
        return traineeNeedService.createNewTraineeNeed(trainingNeed);
    }

    /**
     * API for Post - Trainning Action
     * 
     * @param userId
     * @return
     * @throws ObjectNotFoundException
     */
    @RequestMapping(value = "/dashboardTrainningAction/participatedCourses/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getListCourseOfTraineeOfTrainningAction(
            @PathVariable("userId") int userId,
            @RequestParam(name = "trainingPlanId", required = false) Integer trainingPlandId)
            throws ObjectNotFoundException {
        logger.debug("getListCourseOfTraineeOfTrainningAction");
        return learningDashboardService.getParticipatedCourses(userId,
                trainingPlandId);
    }

    // View ActionPlan And Quiz
    @RequestMapping(value = "/dashboardTrainningAction/trainingAction/{sessionParticipantId}/", method = RequestMethod.GET)
    public ResponseEntity<?> getInfoOfTraineeOfTrainningAction(
            @PathVariable("sessionParticipantId") int sessionParticipantId,
            @PathVariable("courseId") int courseId)
            throws ObjectNotFoundException {
        logger.debug("getInfoOfTraineeOfTrainningAction");
        return learningDashboardService
                .getInfoOfTraineeOfTrainningAction(sessionParticipantId);
    }

    @RequestMapping(value = "/dashboardTrainningAction/trainingAction", method = RequestMethod.POST)
    public ResponseEntity<?> submitTrainingAction(
            @RequestBody ActionPlan actionPlan) {
        logger.debug("submitTrainingAction");
        return learningDashboardService.submitTrainingAction(actionPlan);
    }

    /**
     * API for trainee Option
     * 
     * @param userId
     * @return
     * @throws ObjectNotFoundException
     */
    @RequestMapping(value = "/dashboardTraineeOfOptionCourse/tentativeCourses/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getListCourseOfTraineeOfOptionCourse(
            @PathVariable("userId") int userId,
            @RequestParam(name = "trainingPlanId", required = false) Integer trainingPlanId)
            throws ObjectNotFoundException {
        logger.debug("getListCourseOfTraineeOfOptionCourse");
        return learningDashboardService.getListCourseOfTraineeOfOptionCourse(
                userId, trainingPlanId);
    }

    @RequestMapping(value = "/dashboardTraineeOfOptionCourse/acceptCourses", method = RequestMethod.PUT)
    public ResponseEntity<?> acceptCourse(@RequestBody List<Course> course) {
        for (Course courseUpdate : course) {
            learningDashboardService.updateComment(
                    courseUpdate.getSessionParticipantId(),
                    courseUpdate.getComment());
            learningDashboardService.updateStatusOfCourse(
                    courseUpdate.getSessionParticipantId(),
                    TraineeActionStatus.ACCEPTED.getValue());
        }
        return Utils.generateSuccessResponseEntity("AcceptCourse Successful");
    }

    @RequestMapping(value = "/dashboardTraineeOfOptionCourse/denyCourses", method = RequestMethod.PUT)
    public ResponseEntity<?> denyCourses(@RequestBody List<Course> course) {
        for (Course courseUpdate : course) {
            learningDashboardService.updateComment(
                    courseUpdate.getSessionParticipantId(),
                    courseUpdate.getComment());
            learningDashboardService.updateStatusOfCourse(
                    courseUpdate.getSessionParticipantId(),
                    TraineeActionStatus.DENIED.getValue());
        }
        return Utils.generateSuccessResponseEntity("DenyCourses Successful");
    }
    
    @RequestMapping(value = "/dashboardTraineeOfOptionCourse/tentativeCourses/sessionParticipant/{sessionParticipantid}", method = RequestMethod.GET)
    public String getCommentBySessionParticipantId(
            @PathVariable("sessionParticipantid") int sessionParticipantid)
            throws ObjectNotFoundException {
        logger.debug("getCommentBySessionParticipantId");
        return learningDashboardService.getCommentBySessionParticipantId(sessionParticipantid);
    }
    /** 
	 * 
	 */

    @RequestMapping(value = "/dashboardRoaldMap/learningPlan/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getLearningPlan(
            @PathVariable("userId") int userId,
            @RequestParam(name = "trainingPlanId", required = false) Integer trainingPlandId)
            throws ObjectNotFoundException {
        logger.debug("getAllCourse");
        return learningDashboardService.getLearningPlanProcess(userId,
                trainingPlandId);

    }

    @RequestMapping(value = "/trainingAction/trainningActionDetail/actionPlan/{sessionParticipantId}", method = RequestMethod.GET)
    public ResponseEntity<?> reviewActionTrainingActionPlan(
            @PathVariable("sessionParticipantId") int sessionParticipantId) {
        logger.info("reviewActionTrainingActionPlan: " + sessionParticipantId);
        return learningDashboardService
                .reviewActionTrainingActionPlan(sessionParticipantId);
    }

    @RequestMapping(value = "/trainingAction/trainningActionDetail/quiz/{sessionParticipantId}", method = RequestMethod.GET)
    public ResponseEntity<?> reviewActionTrainingQuiz(
            @PathVariable("sessionParticipantId") int sessionParticipantId) {
        logger.info("reviewActionTrainingQuiz: " + sessionParticipantId);
        return learningDashboardService
                .reviewActionTrainingQuiz(sessionParticipantId);
    }

}
