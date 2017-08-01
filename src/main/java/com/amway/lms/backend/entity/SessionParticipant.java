package com.amway.lms.backend.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;

/**
 * The persistent class for the session_participants database table.
 * 
 */
@Entity
@Table(name = "session_participants")
@NamedQueries({
        @NamedQuery(name = "getSessionParticipant", query = "SELECT sp FROM SessionParticipant sp WHERE sp.sessionId = ? and sp.userId = ?"),
        @NamedQuery(name = "getSessionParticipantByStatus", query = "SELECT sp FROM SessionParticipant sp WHERE sp.completionStatus = ? AND sp.confirmationStatus = 1"),
        @NamedQuery(name = "getListByUserAndCompletionStatus", query = "SELECT sp FROM SessionParticipant sp WHERE sp.userId = ? AND sp.completionStatus = ?"),
        @NamedQuery(name = "getSessionParticipantByUserAndCourse", query = "SELECT sp FROM SessionParticipant sp WHERE sp.sessionId = ? and sp.userId = ?"),
        @NamedQuery(name = "delSessionParticipant", query = "DELETE SessionParticipant sp WHERE sp.sessionId = ? and sp.userId = ?"),
        @NamedQuery(name = "getSessionParticipantByUserId", query = "SELECT a FROM SessionParticipant a  where a.userId=? AND a.sessionId IN (SELECT s.id FROM Session s )"),
        @NamedQuery(name = "getApprovedSessionParticipantByUserId", query = "SELECT a FROM SessionParticipant a  where a.userId=? AND a.managerActionStatus=? AND a.sessionId IN (SELECT s.id FROM Session s )"),
        @NamedQuery(name = "getSessionParticipantById", query = "SELECT a FROM SessionParticipant a  where a.id=?"),
        @NamedQuery(name = "getSessionParticipantBySessionId", query = "SELECT a FROM SessionParticipant a  where a.sessionId=?"),
        @NamedQuery(name = "isExistUserInSessionParticipant", query = "SELECT u FROM User u WHERE u.id IN(SELECT sp.userId FROM SessionParticipant sp WHERE sp.sessionId IN(SELECT s1.id FROM Session s1 WHERE s1.courseId IN(SELECT s2.courseId FROM Session s2 WHERE s2.id=?)) AND sp.isSendingList=1 AND sp.userId = ?) OR u.id IN (SELECT s3.trainerId FROM Session s3 WHERE s3.id=? and s3.trainerId = ?)"),
        @NamedQuery(name = "isExistTrainerInSessionParticipant", query = "SELECT u FROM User u WHERE u.id IN(SELECT sp.userId FROM SessionParticipant sp WHERE sp.sessionId IN(SELECT s1.id FROM Session s1 WHERE s1.courseId IN(SELECT s2.courseId FROM Session s2 WHERE s2.id=?)) AND sp.isSendingList=1 AND sp.userId = ?) AND u.id IN (SELECT s3.trainerId FROM Session s3 WHERE s3.id=? and s3.trainerId = ?)")

})
public class SessionParticipant implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "checkin_at")
    private Timestamp checkinAt;

    @Column(name = "checkout_at")
    private Timestamp checkoutAt;

    @Column(name = "completion_status")
    private int completionStatus;
    
    @Column(name = "confirmation_status")
    private int confirmationStatus;
    
    @Column(name = "confirmation_date")
    private Timestamp confirmationDate;

    @Column(name = "session_id")
    private int sessionId;

    @Column(name = "number_of_testing")
    private int numberOfTesting;

    @Column(name = "quiz_correct_answers")
    private int quizCorrectAnswers;

    @Column(name = "quiz_score")
    private int quizScore;

    @Column(name = "quiz_status")
    private int quizStatus;
    
    @Column(name = "quiz_result_status")
    private int quizResultStatus;

    @Column(name = "user_id")
    private int userId;

    @Transient
    private User user;

    @Column(name = "comment")
    private String comment;
    
    @Column(name = "manager_action_status")
    private int managerActionStatus;
    
    @Column(name="is_sending_list")
    private int isSendingList;
    
    @Column(name="pre_training_comment")
    private String preTrainingComment;
    
    public SessionParticipant() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getCheckinAt() {
        return this.checkinAt;
    }

    public void setCheckinAt(Timestamp checkinAt) {
        this.checkinAt = checkinAt;
    }

    public Timestamp getCheckoutAt() {
        return this.checkoutAt;
    }

    public void setCheckoutAt(Timestamp checkoutAt) {
        this.checkoutAt = checkoutAt;
    }

    public int getCompletionStatus() {
        return this.completionStatus;
    }

    public void setCompletionStatus(int completionStatus) {
        this.completionStatus = completionStatus;
    }

    public int getConfirmationStatus() {
        return this.confirmationStatus;
    }

    public void setConfirmationStatus(int confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getNumberOfTesting() {
        return this.numberOfTesting;
    }

    public void setNumberOfTesting(int numberOfTesting) {
        this.numberOfTesting = numberOfTesting;
    }

    public int getQuizCorrectAnswers() {
        return this.quizCorrectAnswers;
    }

    public void setQuizCorrectAnswers(int quizCorrectAnswers) {
        this.quizCorrectAnswers = quizCorrectAnswers;
    }

    public int getQuizScore() {
        return this.quizScore;
    }

    public void setQuizScore(int quizScore) {
        this.quizScore = quizScore;
    }

    public int getQuizStatus() {
        return this.quizStatus;
    }

    public void setQuizStatus(int quizStatus) {
        this.quizStatus = quizStatus;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

	public int getManagerActionStatus() {
		return managerActionStatus;
	}

	public void setManagerActionStatus(int managerActionStatus) {
		this.managerActionStatus = managerActionStatus;
	}

    public int getQuizResultStatus() {
        return quizResultStatus;
    }

    public void setQuizResultStatus(int quizResultStatus) {
        this.quizResultStatus = quizResultStatus;
    }

    public Timestamp getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Timestamp confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public int getIsSendingList() {
        return isSendingList;
    }

    public void setIsSendingList(int isSendingList) {
        this.isSendingList = isSendingList;
    }

    public String getPreTrainingComment() {
        return preTrainingComment;
    }

    public void setPreTrainingComment(String preTrainingComment) {
        this.preTrainingComment = preTrainingComment;
    }

}