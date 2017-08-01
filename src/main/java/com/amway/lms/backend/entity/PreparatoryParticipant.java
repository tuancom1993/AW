package com.amway.lms.backend.entity;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the training_plan_participants database table.
 * 
 */
@Entity
@Table(name = "preparatory_participants")
@NamedQueries({ @NamedQuery(name = "PreparatoryParticipant.findAll", query = "SELECT p FROM PreparatoryParticipant p"),
		@NamedQuery(name = "getPreparatoryParticipantByCourseIdAndUserId", query = "FROM PreparatoryParticipant p WHERE p.courseId = ? AND p.userId = ? AND p.sessionId=?"),
		@NamedQuery(name = "getPreparatoryParticipantByCourse", query = "FROM PreparatoryParticipant p WHERE p.courseId = ?"),
		@NamedQuery(name = "getPreparatoryParticipantByTrainingPlanId", query = "FROM PreparatoryParticipant p WHERE p.trainingPlanId = ?"),		
		@NamedQuery(name = "getPreparatoryParticipant", query = "FROM PreparatoryParticipant p WHERE p.trainingPlanId = ? AND p.userId = ?"), })
public class PreparatoryParticipant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "training_plan_id")
	private int trainingPlanId;

	@Column(name = "course_id")
	private int courseId;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "session_id")
	private int sessionId;

	@Column(name = "is_requested_by_manager")
	private int isRequestedByManager;

	@Column(name = "requested_manager_name")
	private String requestedManagerName;

	public PreparatoryParticipant() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTrainingPlanId() {
		return trainingPlanId;
	}

	public void setTrainingPlanId(int trainingPlanId) {
		this.trainingPlanId = trainingPlanId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getIsRequestedByManager() {
		return isRequestedByManager;
	}

	public void setIsRequestedByManager(int isRequestedByManager) {
		this.isRequestedByManager = isRequestedByManager;
	}

	public String getRequestedManagerName() {
		return requestedManagerName;
	}

	public void setRequestedManagerName(String requestedManagerName) {
		this.requestedManagerName = requestedManagerName;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

}