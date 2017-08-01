package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the tests database table.
 * 
 */
@Entity
@Table(name = "tests")
@NamedQueries({
    @NamedQuery(name = "getTestList", query = "SELECT t FROM Test t Order By t.id Desc")})
public class Test implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "amount_question_passed")
    private int amountQuestionPassed;

    @Column(name = "begining_time")
    private Timestamp beginingTime;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "date_sent_mail")
    private Timestamp dateSentMail;

    @Column(name = "ending_time")
    private Timestamp endingTime;

    @Column(name = "max_test_times")
    private int maxTestTimes;

    @Column(name = "number_questions")
    private int numberQuestions;

    private String title;

    private String topic;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "user_id")
    private int userId;
    
    @Transient
    private List<TestPart> testParts;

    public Test() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmountQuestionPassed() {
        return this.amountQuestionPassed;
    }

    public void setAmountQuestionPassed(int amountQuestionPassed) {
        this.amountQuestionPassed = amountQuestionPassed;
    }

    public Timestamp getBeginingTime() {
        return this.beginingTime;
    }

    public void setBeginingTime(Timestamp beginingTime) {
        this.beginingTime = beginingTime;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getDateSentMail() {
        return this.dateSentMail;
    }

    public void setDateSentMail(Timestamp dateSentMail) {
        this.dateSentMail = dateSentMail;
    }

    public Timestamp getEndingTime() {
        return this.endingTime;
    }

    public void setEndingTime(Timestamp endingTime) {
        this.endingTime = endingTime;
    }

    public int getMaxTestTimes() {
        return this.maxTestTimes;
    }

    public void setMaxTestTimes(int maxTestTimes) {
        this.maxTestTimes = maxTestTimes;
    }

    public int getNumberQuestions() {
        return this.numberQuestions;
    }

    public void setNumberQuestions(int numberQuestions) {
        this.numberQuestions = numberQuestions;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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

    public List<TestPart> getTestParts() {
        return testParts;
    }

    public void setTestParts(List<TestPart> testParts) {
        this.testParts = testParts;
    }

}