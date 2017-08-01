package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the training_needs database table.
 * 
 */
@Entity
@Table(name="training_needs")
@NamedQuery(name="TrainingNeed.findAll", query="SELECT t FROM TrainingNeed t")
public class TrainingNeed implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="current_issue")
	private String currentIssue;

	@Column(name="training_needs")
	private String trainingNeeds;

	@Column(name="user_id")
	private int userId;

	public TrainingNeed() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCurrentIssue() {
		return this.currentIssue;
	}

	public void setCurrentIssue(String currentIssue) {
		this.currentIssue = currentIssue;
	}

	public String getTrainingNeeds() {
		return this.trainingNeeds;
	}

	public void setTrainingNeeds(String trainingNeeds) {
		this.trainingNeeds = trainingNeeds;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}