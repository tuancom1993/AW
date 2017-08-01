package com.amway.lms.backend.model;

public class DashBoard {

	private int courseId;
	private int traineeId;
	private int sessionParticipantId;

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(int traineeId) {
		this.traineeId = traineeId;
	}

	public int getSessionParticipantId() {
		return sessionParticipantId;
	}

	public void setSessionParticipantId(int sessionParticipantId) {
		this.sessionParticipantId = sessionParticipantId;
	}

}
