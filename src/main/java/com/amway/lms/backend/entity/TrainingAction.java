package com.amway.lms.backend.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the training_actions database table.
 * 
 */
@Entity
@Table(name="training_actions")
@NamedQueries({
	@NamedQuery(name="TrainingAction.findAll", query="SELECT t FROM TrainingAction t"),
	@NamedQuery(name="getTrainingActionById", query="SELECT t FROM TrainingAction t WHERE t.id=?"),	
	@NamedQuery(name="findTrainingActionBySessionParticipantId", query="SELECT t FROM TrainingAction t where t.sessionParticipantId=?")
})
public class TrainingAction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="action_type")
	private String actionType;
	
	@Column(name="todo_plan")
	private String todoPlan;
	
	@Column(name="start_time")
	private Timestamp startTime;

	@Column(name="end_time")	
	private Timestamp endTime;
	
	@Column(name="quiz_status")
	private int quizStatus;
	
	@Column(name="session_participant_id")
	private int sessionParticipantId;

	@Column(name="number_of_testing")
	private int numberOfTesting;

	@Column(name="quiz_score")
	private int quizScore;

	@Column(name="quiz_correct_answer")
	private int quizCorrectAnswer;
	
	private int status;
	
	private int result;
	
	private String comment;


	public TrainingAction() {
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getActionType() {
		return actionType;
	}


	public void setActionType(String actionType) {
		this.actionType = actionType;
	}


	public String getTodoPlan() {
		return todoPlan;
	}


	public void setTodoPlan(String todoPlan) {
		this.todoPlan = todoPlan;
	}

	public int getQuizStatus() {
		return quizStatus;
	}


	public void setQuizStatus(int quizStatus) {
		this.quizStatus = quizStatus;
	}

	public int getNumberOfTesting() {
		return numberOfTesting;
	}


	public void setNumberOfTesting(int numberOfTesting) {
		this.numberOfTesting = numberOfTesting;
	}


	public int getQuizScore() {
		return quizScore;
	}


	public void setQuizScore(int quizScore) {
		this.quizScore = quizScore;
	}


	public int getQuizCorrectAnswer() {
		return quizCorrectAnswer;
	}


	public void setQuizCorrectAnswer(int quizCorrectAnswer) {
		this.quizCorrectAnswer = quizCorrectAnswer;
	}


    /**
     * @return the startTime
     */
    public Timestamp getStartTime() {
        return startTime;
    }


    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }


    /**
     * @return the endTime
     */
    public Timestamp getEndTime() {
        return endTime;
    }


    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }


    /**
     * @return the sessionParticipantId
     */
    public int getSessionParticipantId() {
        return sessionParticipantId;
    }


    /**
     * @param sessionParticipantId the sessionParticipantId to set
     */
    public void setSessionParticipantId(int sessionParticipantId) {
        this.sessionParticipantId = sessionParticipantId;
    }


    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }


    public int getResult() {
        return result;
    }


    public void setResult(int result) {
        this.result = result;
    }


    public String getComment() {
        return comment;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }
   
}