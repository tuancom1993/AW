/**
 * 
 */
package com.amway.lms.backend.entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author acton
 *
 */
@Entity
@Table(name = "post_training_survey_trainers")
@NamedQueries({
    @NamedQuery(name = "PostTrainingSurveyTrainer.findAll", query = "SELECT p FROM PostTrainingSurveyTrainer p"),
    @NamedQuery(name = "getPostTrainingSurveyTrainerListBySessionId", query = "SELECT p FROM PostTrainingSurveyTrainer p Where p.sessionId = ?")
})
public class PostTrainingSurveyTrainer implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 3394867617046299098L;

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
    
    @JsonFormat(pattern="HH:mm")
    private Time time;
    
    @Column(name = "learning_session")
    private String learning_session;
    
    @Column(name="facilitator_name")
    private String facilitator_name;
    
    private String location;
    
    private String q_1_1;
    
    private String q_1_2;
    
    private String q_1_3;
    
    private String q_2_1_1;
    
    private String q_2_1_2;
    
    private String q_2_2_1;
    
    private String q_2_2_2;
    
    private String q_2_3_1;
    
    private String q_2_3_2;

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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getLearning_session() {
        return learning_session;
    }

    public void setLearning_session(String learning_session) {
        this.learning_session = learning_session;
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

    public String getQ_2_1_1() {
        return q_2_1_1;
    }

    public void setQ_2_1_1(String q_2_1_1) {
        this.q_2_1_1 = q_2_1_1;
    }

    public String getQ_2_1_2() {
        return q_2_1_2;
    }

    public void setQ_2_1_2(String q_2_1_2) {
        this.q_2_1_2 = q_2_1_2;
    }

    public String getQ_2_2_1() {
        return q_2_2_1;
    }

    public void setQ_2_2_1(String q_2_2_1) {
        this.q_2_2_1 = q_2_2_1;
    }

    public String getQ_2_2_2() {
        return q_2_2_2;
    }

    public void setQ_2_2_2(String q_2_2_2) {
        this.q_2_2_2 = q_2_2_2;
    }

    public String getQ_2_3_1() {
        return q_2_3_1;
    }

    public void setQ_2_3_1(String q_2_3_1) {
        this.q_2_3_1 = q_2_3_1;
    }

    public String getQ_2_3_2() {
        return q_2_3_2;
    }

    public void setQ_2_3_2(String q_2_3_2) {
        this.q_2_3_2 = q_2_3_2;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
