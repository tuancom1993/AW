package com.amway.lms.backend.repository;

import java.sql.Timestamp;
import java.util.List;

import com.amway.lms.backend.entity.Session;

public interface SessionRepository {
	public void addSession(Session session);
	public void updateSession(Session session);
	public void deleteSession(Session session);
	public List<Session> getSessionsByCourseId(int courseId);
	public List<Session> getSessionsByCourseIdAndStatus(int courseId, int sessionStatus);
	public List<Session> getSessionsStartToday(int courseId);
	public long countSessionInPass(int courseId);
	public Timestamp findSoonestTime(List<Session> sessionList);
	public List<Session> getSessionsNotSentEmailToTrainer();
	//Dino
	public Session getSessionById(int id);
	public List<Session> getListCourseSuggest(int userId);	
}

