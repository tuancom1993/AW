package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Time;
import java.util.Date;

/**
 * The persistent class for the post_training_survey_participants database
 * table.
 * 
 */
@Entity
@Table(name = "post_training_survey_participants")
@NamedQueries({
    @NamedQuery(name = "PostTrainingSurveyParticipant.findAll", query = "SELECT p FROM PostTrainingSurveyParticipant p"),
    @NamedQuery(name = "getPostTrainingSurveyParticipantListBySessionId", query = "SELECT p FROM PostTrainingSurveyParticipant p Where p.sessionId = ?"),
    @NamedQuery(name = "getPostTrainingSurveyParticipantBySessionIdAndUserId", query = "SELECT p FROM PostTrainingSurveyParticipant p Where p.sessionId = ? and p.userId = ?")
    
})

public class PostTrainingSurveyParticipant implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    @Column(name="session_id")
    private int sessionId;
    
    @Column(name = "user_id")
    private int userId;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date date;

    @Column(name = "learning_session")
    private String learning_session;

    private String location;
    
    @Column(name="facilitator_name")
    private String facilitator_name;

    @Column(name = "q_1_1")
    private String q_1_1;

    @Column(name = "q_1_2")
    private String q_1_2;

    @Column(name = "q_1_3")
    private String q_1_3;

    @Column(name = "q_2")
    private String q_2;

    @Column(name = "q_3")
    private String q_3;

    @Column(name = "q_4")
    private String q_4;

    @Column(name = "q_5")
    private String q_5;

    @Column(name = "q_6")
    private String q_6;

    @Column(name = "q_7")
    private String q_7;

    @Column(name = "q_8")
    private String q_8;

    @Column(name = "q_9")
    private String q_9;

    @JsonFormat(pattern="HH:mm")
    private Time time;

    public PostTrainingSurveyParticipant() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLearning_session() {
        return learning_session;
    }

    public void setLearning_session(String learning_session) {
        this.learning_session = learning_session;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFacilitator_name() {
        return facilitator_name;
    }

    public void setFacilitator_name(String facilitator_name) {
        this.facilitator_name = facilitator_name;
    }

    public String getQ_1_1() {
        return q_1_1;
    }

    public void setQ_1_1(String q_1_1) {
        this.q_1_1 = q_1_1;
    }

    public String getQ_1_2() {
        return q_1_2;
    }

    public void setQ_1_2(String q_1_2) {
        this.q_1_2 = q_1_2;
    }

    public String getQ_1_3() {
        return q_1_3;
    }

    public void setQ_1_3(String q_1_3) {
        this.q_1_3 = q_1_3;
    }

    public String getQ_2() {
        return q_2;
    }

    public void setQ_2(String q_2) {
        this.q_2 = q_2;
    }

    public String getQ_3() {
        return q_3;
    }

    public void setQ_3(String q_3) {
        this.q_3 = q_3;
    }

    public String getQ_4() {
        return q_4;
    }

    public void setQ_4(String q_4) {
        this.q_4 = q_4;
    }

    public String getQ_5() {
        return q_5;
    }

    public void setQ_5(String q_5) {
        this.q_5 = q_5;
    }

    public String getQ_6() {
        return q_6;
    }

    public void setQ_6(String q_6) {
        this.q_6 = q_6;
    }

    public String getQ_7() {
        return q_7;
    }

    public void setQ_7(String q_7) {
        this.q_7 = q_7;
    }

    public String getQ_8() {
        return q_8;
    }

    public void setQ_8(String q_8) {
        this.q_8 = q_8;
    }

    public String getQ_9() {
        return q_9;
    }

    public void setQ_9(String q_9) {
        this.q_9 = q_9;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
}