package com.amway.lms.backend.model;

import java.util.Date;

public class EventCourse {

	private int id;
	private int courseId;
	private int sessionParticipantId;
	private String title;
	private Date start;
	private Date end;
	private String backgroundColor;
	private String borderColor;
	private String quizStatus;
	private String courseStatus;
	private int trainingActionStatus;
	private int isOffline;
	
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	
	public int getSessionParticipantId() {
		return sessionParticipantId;
	}
	public void setSessionParticipantId(int sessionParticipantId) {
		this.sessionParticipantId = sessionParticipantId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}
	public String getQuizStatus() {
		return quizStatus;
	}
	public void setQuizStatus(String quizStatus) {
		this.quizStatus = quizStatus;
	}
	public String getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}	
	
	public int getTrainingActionStatus() {
        return trainingActionStatus;
    }
    public void setTrainingActionStatus(int trainingActionStatus) {
        this.trainingActionStatus = trainingActionStatus;
    }
    public int getIsOffline() {
		return isOffline;
	}
	public void setIsOffline(int isOffline) {
		this.isOffline = isOffline;
	}
	
	
	
}
