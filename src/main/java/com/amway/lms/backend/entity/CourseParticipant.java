package com.amway.lms.backend.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;

/**
 * The persistent class for the course_participants database table.
 * 
 */
@Entity
@Table(name = "course_participants")
@NamedQueries({
        @NamedQuery(name = "CourseParticipant.findAll", query = "SELECT c FROM CourseParticipant c"),
        @NamedQuery(name = "getCourseParticipant", query = "SELECT cp FROM CourseParticipant cp WHERE cp.courseId = ? and cp.userId = ?"),
        @NamedQuery(name = "delCourseParticipant", query = "DELETE CourseParticipant cp WHERE cp.courseId = ? and cp.userId = ?"),
        @NamedQuery(name = "getListCourseParticipantByUserId", query = "SELECT a FROM CourseParticipant a  where a.userId=?"),
        @NamedQuery(name = "getCourseParticipantsByCourseId", query = "SELECT a FROM CourseParticipant a  where a.courseId=?"),
        @NamedQuery(name = "getCourseParticipantById", query = "SELECT c FROM CourseParticipant c where c.id=?"),
        @NamedQuery(name = "getCourseParticipantCourseIdandUserId", query = "SELECT c FROM CourseParticipant c where c.courseId=? and c.userId=?"),
        @NamedQuery(name = "updateCheckIn", query = "UPDATE CourseParticipant c SET c.checkinAt=? WHERE c.courseId=? and c.userId=?"),
        @NamedQuery(name = "updateCheckOut", query = "UPDATE CourseParticipant c SET c.checkoutAt=? WHERE c.courseId=? and c.userId=?") })
public class CourseParticipant implements Serializable {
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

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "number_of_testing")
    private int numberOfTesting;

    @Lob
    @Column(name = "quiz_correct_answers")
    private String quizCorrectAnswers;

    @Column(name = "quiz_score")
    private int quizScore;

    @Column(name = "quiz_status")
    private int quizStatus;

    @Column(name = "user_id")
    private int userId;

    @Transient
    private User user;

    @Column(name = "deny_comment")
    private String denyComment;

    @Column(name = "is_submit")
    private int isSubmit;

    public CourseParticipant() {
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

    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getNumberOfTesting() {
        return this.numberOfTesting;
    }

    public void setNumberOfTesting(int numberOfTesting) {
        this.numberOfTesting = numberOfTesting;
    }

    public String getQuizCorrectAnswers() {
        return this.quizCorrectAnswers;
    }

    public void setQuizCorrectAnswers(String quizCorrectAnswers) {
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

    public String getDenyComment() {
        return denyComment;
    }

    public void setDenyComment(String denyComment) {
        this.denyComment = denyComment;
    }

    public int getIsSubmit() {
        return isSubmit;
    }

    public void setIsSubmit(int isSubmit) {
        this.isSubmit = isSubmit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    

}