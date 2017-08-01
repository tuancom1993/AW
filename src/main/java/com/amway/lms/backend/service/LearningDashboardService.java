package com.amway.lms.backend.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.TrainingAction;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.ActionPlan;
import com.amway.lms.backend.model.CourseSessionExport;

public interface LearningDashboardService {
	public ResponseEntity<?> getAllCourse(List<Integer> userIds, int trainingPlandId, int year) throws ObjectNotFoundException;
	public ResponseEntity<?> getCourseListByCompletionStatus(int userId, int completionStatus);

	public ResponseEntity<?> getCourseInformation(int sessionParticipantId) throws ObjectNotFoundException;

	public ResponseEntity<?> updateStatusOfCourse(int sessionParticipantId, int status);

	public ResponseEntity<?> acceptOrDenyCourseByManager(int sessionId, int userId, int courseId, int status, String comment) throws MessagingException, IOException, ObjectNotFoundException;

	public ResponseEntity<?> getParticipatedCourses(int userId, Integer trainingPlanId) throws ObjectNotFoundException;

	public ResponseEntity<?> getInfoOfTraineeOfTrainningAction(int sessionParticipantId) throws ObjectNotFoundException;

	public ResponseEntity<?> submitTrainingAction(ActionPlan actionPlan);

	public ResponseEntity<?> getListCourseOfTraineeOfOptionCourse(int userId, Integer trainingPlanId) throws ObjectNotFoundException;

	public ResponseEntity<?> getPresentationSkill(int courseId);
	
	public ResponseEntity<?> getPresentationSkillBySession(int sessionId, int userId);
	
	public ResponseEntity<?> addPretrainingComment(int courseId, String preTrainingComment);

	public ResponseEntity<?> searchByStaffCode(int supperviseId, String staffCode) throws ObjectNotFoundException;
	
	public ResponseEntity<?> searchByHodStaffCode(int hodId, String staffCode) throws ObjectNotFoundException;
	
	public ResponseEntity<?> reviewActionTrainingActionPlan(int sessionParticipantId);
	
	public ResponseEntity<?> getForm(int sessionParticipantId);
	public ResponseEntity<?> evaluate(TrainingAction trainingAction);

	public ResponseEntity<?> reviewActionTrainingQuiz(int sessionParticipantId);

	public ResponseEntity<?> getListCourseCoursesApprovedByHR(List<Integer> userIds) throws ObjectNotFoundException;

	public void updateComment(int sessionParticipantId, String denyComment);
	
	public String getCommentBySessionParticipantId(int sessionParticipantId) throws ObjectNotFoundException;
	
	//Get Emloyees for supervise 
	public List<Integer> getListUserIdOfSuppervise(int supperviseId);
	public ResponseEntity<?> getAllEmployees(int supperviseId);
	
	//Get All Employees for HOD
	public List<Integer> getListUserIdOfHod(int hodId);
	public ResponseEntity<?> getAllEmployeesByHod(int hodId);
	
	public List<Integer> getListUserIdOfHodByDepartment(int hodId, int departmentId);
	public ResponseEntity<?> getEmployeesByDepartment(int hodId, int departmentId);
	
	public List<Integer> getListUserIdOfAmByDepartment(int approvalManagerId, int departmentId); 
	public ResponseEntity<?> getEmployeesByApprovalManagerId(int approvalManagerId, int departmentId); 
	
	public ResponseEntity<?> getLearningPlanUSer(int userId) throws ObjectNotFoundException;

	public ResponseEntity<?> getSuggestCourse(int userid,int trainingPlanId);

	public ResponseEntity<?> addSuggestCourse(List<com.amway.lms.backend.model.Course> courses);

	public ResponseEntity<?> getLearningPlanProcess(int userId, Integer trainingPlanId) throws ObjectNotFoundException;

	public ResponseEntity<?> getAllCourseBySuper(List<Integer> userIds) throws ObjectNotFoundException;

	public ResponseEntity<?> acceptCourse(int sessionParticipantId);

	public ResponseEntity<?> denyCourse(int sessionParticipantId);

	public List<CourseSessionExport> getAllCourseSessionExport(Integer userIds) throws ObjectNotFoundException;

	public ResponseEntity<?> getCourseByTrainingPlan(int userId,int trainingPlanId) throws ObjectNotFoundException;
	
	public List<Integer> getListApprovalManagerIdOfHod(int hodId);

}
