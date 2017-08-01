package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the test_participants database table.
 * 
 */
@Entity
@Table(name="test_participants") 
@NamedQueries({
    @NamedQuery(name="TestParticipant.findAll", query="SELECT t FROM TestParticipant t"),
    @NamedQuery(name="getTestParticipantListByTestId", query="SELECT t FROM TestParticipant t WHERE t.testId=?"),
    @NamedQuery(name="getTestParticipantByUserId", query="SELECT t FROM TestParticipant t WHERE t.userId=?"),    
    @NamedQuery(name="searchTestParticipantListByTestIdAndTraineeName", query="SELECT t FROM TestParticipant t WHERE t.testId=? AND t.userId IN (SELECT u.id FROM User u WHERE u.firstName LIKE ? OR u.lastName LIKE ?)"),
    @NamedQuery(name="getTestParticipantListByTestIdAndUserId", query="SELECT t FROM TestParticipant t WHERE t.testId=? AND t.userId = ?")
})

public class TestParticipant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="number_of_tested")
	private int numberOfTested;

	@Column(name="test_correct_answers")
	private int testCorrectAnswers;

	@Column(name="test_id")
	private int testId;

	@Column(name="test_result_status")
	private int testResultStatus;

	@Column(name="test_status")
	private int testStatus;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	@Column(name="user_id")
	private int userId;
	

	public TestParticipant() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public int getNumberOfTested() {
		return this.numberOfTested;
	}

	public void setNumberOfTested(int numberOfTested) {
		this.numberOfTested = numberOfTested;
	}

	public int getTestCorrectAnswers() {
		return this.testCorrectAnswers;
	}

	public void setTestCorrectAnswers(int testCorrectAnswers) {
		this.testCorrectAnswers = testCorrectAnswers;
	}

	public int getTestId() {
		return this.testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public int getTestResultStatus() {
		return this.testResultStatus;
	}

	public void setTestResultStatus(int testResultStatus) {
		this.testResultStatus = testResultStatus;
	}

	public int getTestStatus() {
		return this.testStatus;
	}

	public void setTestStatus(int testStatus) {
		this.testStatus = testStatus;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}