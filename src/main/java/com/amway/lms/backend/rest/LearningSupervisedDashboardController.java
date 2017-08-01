package com.amway.lms.backend.rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;

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

import com.amway.lms.backend.common.AmwayEnum.ManagerActionStatus;
import com.amway.lms.backend.entity.TrainingAction;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.Course;
import com.amway.lms.backend.model.PresentationSkill;
import com.amway.lms.backend.service.LearningDashboardService;

@RestController
@RequestMapping("/api/v1/suppervise")
public class LearningSupervisedDashboardController {
	private static final Logger logger = LoggerFactory.getLogger(LearningSelfDashboardController.class);

	@Autowired
	private LearningDashboardService learningDashboardService;

	/**
	 * START APIs for RoaldMap
	 * 
	 * @param userId
	 * @return
	 * @throws ObjectNotFoundException 
	 */
	@RequestMapping(value = "/roaldMap/courses/{supperviseId}", method = RequestMethod.GET)
	public ResponseEntity<?> getListCourseOfTrainee(@PathVariable("supperviseId") int supperviseId) throws ObjectNotFoundException {
		logger.debug("getListCourseOfTrainee with userId: " + supperviseId);
		List<Integer> userIds = learningDashboardService.getListUserIdOfSuppervise(supperviseId);
		return learningDashboardService.getAllCourseBySuper(userIds);
	}

	@RequestMapping(value = "/roaldMap/course/presentationSkills/{courseId}", method = RequestMethod.GET)
	public ResponseEntity<?> getPresentationSkill(@PathVariable("courseId") int courseId) {
		logger.debug("getPresentationSkill  with courseID: " + courseId);
		return learningDashboardService.getPresentationSkill(courseId);
	}
	
	@RequestMapping(value = "/roaldMap/course/presentationSkillsBySession/{sessionId}/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getPresentationSkill(@PathVariable("sessionId") int sessionId, @PathVariable("userId") int userId) {
        logger.debug("getPresentationSkill  with sessionID: " + sessionId + "and UserID: " + userId);
        return learningDashboardService.getPresentationSkillBySession(sessionId, userId);
    }
	/**
	 * END of API roald Map
	 */

	/**
	 * START API OF Optional Training course
	 * @throws ObjectNotFoundException 
	 */

	@RequestMapping(value = "/optionalTraining/courses/{supperviseId}", method = RequestMethod.GET)
	public ResponseEntity<?> getCoursesInoptionalTraining(@PathVariable("supperviseId") int supperviseId) throws ObjectNotFoundException {
		logger.debug("getCoursesInoptionalTraining");
		List<Integer> userIds = learningDashboardService.getListUserIdOfSuppervise(supperviseId);
		return learningDashboardService.getListCourseCoursesApprovedByHR(userIds);
	}

	@RequestMapping(value = "/optionalTraining/learningAction", method = RequestMethod.PUT)
	public ResponseEntity<?> addPresentationSkill(@RequestBody PresentationSkill presentationSkill) {
		logger.debug("addPresentationSkill");
		return learningDashboardService.addPretrainingComment(presentationSkill.getCourseId(), presentationSkill.getPreTrainingComment());
	}

	@RequestMapping(value = "/optionalTraining/acceptCourse", method = RequestMethod.PUT)
	public ResponseEntity<?> acceptCourseForTrainee(@RequestBody PresentationSkill presentationSkill) throws MessagingException, IOException, ObjectNotFoundException {
		return learningDashboardService.acceptOrDenyCourseByManager(presentationSkill.getSessionId(), presentationSkill.getTraineeId(), presentationSkill.getCourseId(),
				ManagerActionStatus.ACCEPTED.getValue(), presentationSkill.getPreTrainingComment());
	}

	@RequestMapping(value = "/optionalTraining/denyCourse", method = RequestMethod.PUT)
	public ResponseEntity<?> denyCourseForTrainee(@RequestBody PresentationSkill presentationSkill) throws MessagingException, IOException, ObjectNotFoundException {
		return learningDashboardService.acceptOrDenyCourseByManager(presentationSkill.getSessionId(), presentationSkill.getTraineeId(), presentationSkill.getCourseId(),
				ManagerActionStatus.DENIED.getValue(), presentationSkill.getPreTrainingComment());
	}

	/**
	* APIs POST training Action
	* 
	* @param staffCode
	* @return
	* @throws SQLException
	* @throws Exception
	*/
	@RequestMapping(value = "/trainingAction/{supperviseId}/search", method = RequestMethod.GET)
	public ResponseEntity<?> getEmployeeAndCoursesByCode(@PathVariable("supperviseId") int supperviseId, @RequestParam(value = "staffCode", required = false) String staffCode)
			throws SQLException, Exception {
		logger.info("getEmployeeAndCoursesByCode: " + staffCode);
		return learningDashboardService.searchByStaffCode(supperviseId, staffCode);
	}

	@RequestMapping(value = "/trainingAction/getCourseByTrainingPlan/{userId}/{trainingPlanId}", method = RequestMethod.GET)
	public ResponseEntity<?> getCourseByTrainingPlan( @PathVariable("userId") int userId,@PathVariable("trainingPlanId") int trainingPlanId) throws SQLException, Exception {
		logger.info("getEmployeeAndCoursesByCode: " + trainingPlanId);
		return learningDashboardService.getCourseByTrainingPlan( userId,trainingPlanId);
	}

	@RequestMapping(value = "/trainingAction/trainningActionDetail/actionPlan/{sessionParticipantId}", method = RequestMethod.GET)
	public ResponseEntity<?> reviewActionTrainingActionPlan(@PathVariable("sessionParticipantId") int sessionParticipantId) {
		logger.info("reviewActionTrainingActionPlan: " + sessionParticipantId);
		return learningDashboardService.reviewActionTrainingActionPlan(sessionParticipantId);
	}
	
	@RequestMapping(value = "/trainingAction/trainningActionDetail/actionPlan/getForm/{sessionParticipantId}", method = RequestMethod.GET)
 public ResponseEntity<?> getForm(@PathVariable("sessionParticipantId") int sessionParticipantId) {
  logger.info("reviewActionTrainingActionPlan: " + sessionParticipantId);
  return learningDashboardService.getForm(sessionParticipantId);
 }
	
 @RequestMapping(value = "/trainingAction/trainningActionDetail/actionPlan/evaluate", method = RequestMethod.PUT)
 public ResponseEntity<?> evaluate(@RequestBody final TrainingAction trainingAction) {
  logger.info("evaluate: " + trainingAction);
  return learningDashboardService.evaluate(trainingAction);
 }

	@RequestMapping(value = "/trainingAction/trainningActionDetail/quiz/{sessionParticipantId}", method = RequestMethod.GET)
	public ResponseEntity<?> reviewActionTrainingQuiz(@PathVariable("sessionParticipantId") int sessionParticipantId) {
		logger.info("reviewActionTrainingQuiz: " + sessionParticipantId);
		return learningDashboardService.reviewActionTrainingQuiz(sessionParticipantId);
	}

	@RequestMapping(value = "/trainingAction/trainningActionDetail/acceptCourse/{sessionParticipantId}", method = RequestMethod.PUT)
	public ResponseEntity<?> acceptCourse(@PathVariable("sessionParticipantId") int sessionParticipantId) {
		logger.info("reviewActionTrainingQuiz: " + sessionParticipantId);
		return learningDashboardService.acceptCourse(sessionParticipantId);
	}

	@RequestMapping(value = "/trainingAction/trainningActionDetail/denyCourse/{sessionParticipantId}", method = RequestMethod.PUT)
	public ResponseEntity<?> denyCourse(@PathVariable("sessionParticipantId") int sessionParticipantId) {
		logger.info("reviewActionTrainingQuiz: " + sessionParticipantId);
		return learningDashboardService.denyCourse(sessionParticipantId);
	}

	/**
	 *****************************************************************
	 *****************************************************************
	 
	 * APIS FOR 5MANAGE LEARNING DASHBOARD - SUPERVISED DASHBOARD 2 **
	 * 
	 *****************************************************************
	 ******************************************************************
	 */

	@RequestMapping(value = "/getAllEmployees/{supperviseId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllEmployees(@PathVariable("supperviseId") int supperviseId) {
		return learningDashboardService.getAllEmployees(supperviseId);
	}

	@RequestMapping(value = "/learningPlan/userDetail/{userId}", method = RequestMethod.GET)
	public ResponseEntity<?> getLearningPlan(@PathVariable("userId") int userId) throws ObjectNotFoundException {
		return learningDashboardService.getLearningPlanUSer(userId);
	}

	@RequestMapping(value = "/learningPlan/getSuggestCourse/{userId}/{trainingPlanId}", method = RequestMethod.GET)
	public ResponseEntity<?> getSuggestCourse(@PathVariable("userId") int userId, @PathVariable("trainingPlanId") int trainingPlanId) {
		return learningDashboardService.getSuggestCourse(userId, trainingPlanId);
	}

	@RequestMapping(value = "/learningPlan/addSuggestCourse/", method = RequestMethod.POST)
	public ResponseEntity<?> addSuggestCourse(@RequestBody List<Course> courses) {
		return learningDashboardService.addSuggestCourse(courses);
	}

	//

}
