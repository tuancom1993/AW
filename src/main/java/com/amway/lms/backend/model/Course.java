package com.amway.lms.backend.model;

import java.util.Date;

public class Course extends DashBoard {

	private String courseCode;
	private String courseName;
	private String startTime;
	private String endTime;
	private String courseStatus;
	private String completionStatus;
	private String quizStatus;
	private int managerActionStatusValue;
	private Date time;
	private int trainingActionStatus;
	private String sessionName;
	private String comment;
	private String confirmationStatus;
	private int isOffline;
	private int isOptional;
	private int sessionId;
	private String location;
	private String date;
	private String sessionStatus;
	private String managerActionStatus;
	private String traineeName;
	private Integer ajpvpoint;
	private int trainingPlanId;

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}

	public String getCompletionStatus() {
		return completionStatus;
	}

	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
	}

	public String getQuizStatus() {
		return quizStatus;
	}

	public void setQuizStatus(String quizStatus) {
		this.quizStatus = quizStatus;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getManagerActionStatusValue() {
		return managerActionStatusValue;
	}

	public void setManagerActionStatusValue(int managerActionStatusValue) {
		this.managerActionStatusValue = managerActionStatusValue;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	/**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getConfirmationStatus() {
		return confirmationStatus;
	}

	public void setConfirmationStatus(String confirmationStatus) {
		this.confirmationStatus = confirmationStatus;
	}

	public int getIsOffline() {
		return isOffline;
	}

	public void setIsOffline(int isOffline) {
		this.isOffline = isOffline;
	}

	public int getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(int isOptional) {
        this.isOptional = isOptional;
    }

    public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDate() {
		return date;
	}

	public int getTrainingActionStatus() {
        return trainingActionStatus;
    }

    public void setTrainingActionStatus(int trainingActionStatus) {
        this.trainingActionStatus = trainingActionStatus;
    }

    public void setDate(String date) {
		this.date = date;
	}

	public String getSessionStatus() {
		return sessionStatus;
	}

	public void setSessionStatus(String sessionStatus) {
		this.sessionStatus = sessionStatus;
	}

	public String getManagerActionStatus() {
		return managerActionStatus;
	}

	public void setManagerActionStatus(String managerActionStatus) {
		this.managerActionStatus = managerActionStatus;
	}

	public String getTraineeName() {
		return traineeName;
	}

	public void setTraineeName(String traineeName) {
		this.traineeName = traineeName;
	}

	public Integer getAjpvpoint() {
		return ajpvpoint;
	}

	public void setAjpvpoint(Integer ajpvpoint) {
		this.ajpvpoint = ajpvpoint;
	}

    public int getTrainingPlanId() {
        return trainingPlanId;
    }

    public void setTrainingPlanId(int trainingPlanId) {
        this.trainingPlanId = trainingPlanId;
    }
	
}
