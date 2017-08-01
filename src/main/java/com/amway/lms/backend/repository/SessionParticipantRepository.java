package com.amway.lms.backend.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.amway.lms.backend.entity.SessionParticipant;
import com.amway.lms.backend.entity.User;

public interface SessionParticipantRepository {
	public void addSessionParticipantFromPlan(int sessionId, int userId);
	
	public void addSessionParticipantFromSession(int sessionId, int userId);
	
	public void addSessionParticipantSentMail(int sessionId, int userId);

	public void delSessionParticipant(int sessionId, int userId);

	public SessionParticipant getSessionParticipant(int sessionId, int userId);
	
	public List<User> getUsersAcceptedBySessionId(int sessionId);
	public List<SessionParticipant> getListByUserTrainingPlanYear(int userId, Integer trainingPlanId, Integer year);
	public List<SessionParticipant> getApprovedListByUserTrainingPlanYear(int userId, Integer trainingPlanId, Integer year);
	public List<SessionParticipant> getListByUserTrainingPlan(int userId, Integer trainingPlanId);
	public List<SessionParticipant> getApprovedListByUserTrainingPlan(int userId, Integer trainingPlanId);
	public List<SessionParticipant> getListByUserYear(int userId, Integer year);
	public List<SessionParticipant> getApprovedListByUserYear(int userId, Integer year);
	public List<SessionParticipant> getListByUserAndCompletionStatus(int userId, int status);
	public List<SessionParticipant> getListByUserPlanCompletionStatus(int userId, int trainingPlanId, int status);
	public List<SessionParticipant> getApprovedSessionParticipantByUserId(int userId);
	public boolean isExistUserInSessionParticipant(int sessionId, int userId);
	public boolean isExistTrainerInSessionParticipant(int sessionId, int userId);
	
	/****************************** DINO******************/
	public List<SessionParticipant> getSessionParticipantByUserId(int userId) throws DataAccessException;

	public SessionParticipant getSessionParticipantById(int id);

	public void updateSessionParticipant(SessionParticipant sessionParticipant);

	public void saveSessionParticipant(SessionParticipant sessionParticipant);

	public List<SessionParticipant> getSessionParticipantBySessionId(int sessionId) throws DataAccessException;
	public List<SessionParticipant> getSessionParticipantByStatus();

	public List<User> getUsersBySessionId(int sessionId, Integer confirmationStatus, Integer departmentId);

}
