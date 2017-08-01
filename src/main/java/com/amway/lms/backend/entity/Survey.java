package com.amway.lms.backend.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the surveys database table.
 * 
 */
@Entity
@Table(name = "surveys")
@NamedQueries({ 
    @NamedQuery(name = "getSurveyById", query = "Select s From Survey s Where s.id = ?"),
    @NamedQuery(name = "getSurveyList", query = "SELECT s FROM Survey s Order By s.id Desc"),
    @NamedQuery(name = "getSurveyListByIsSent", query = "SELECT s FROM Survey s WHERE s.isSent = ?"),
})
public class Survey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name = "begining_time")
    private Timestamp beginingTime;
    
    @Transient
    private String beginingTimeStr;

    @Column(name = "ending_time")
    private Timestamp endingTime;

    @Transient
    private String endingTimeStr;
    
    @Lob
    @Column(name = "survey_title")
    private String surveyTitle;

    @Column(name = "user_id")
    private int userId;
    
    @Column(name="survey_desc")
    private String surveyDesc;
    
    @Column(name="survey_type_id")
    private int surveyTypeId;
    
    @Transient
    private String surveyType;
    
    @Column(name="sub_desc")
    private String subDesc;
    
    @Column(name="is_sent")
    private int isSent;
    
    @Transient
    private List<Question> questions;
    
    @Transient
    private List<SurveySession> surveySessions;
    
    @Transient
    private String status;

    public Survey() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getBeginingTime() {
        return this.beginingTime;
    }

    public void setBeginingTime(Timestamp beginingTime) {
        this.beginingTime = beginingTime;
    }

    public Timestamp getEndingTime() {
        return this.endingTime;
    }

    public void setEndingTime(Timestamp endingTime) {
        this.endingTime = endingTime;
    }

    public String getSurveyTitle() {
        return this.surveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getSurveyDesc() {
        return surveyDesc;
    }

    public void setSurveyDesc(String surveyDesc) {
        this.surveyDesc = surveyDesc;
    }

    public int getSurveyTypeId() {
        return surveyTypeId;
    }

    public void setSurveyTypeId(int surveyTypeId) {
        this.surveyTypeId = surveyTypeId;
    }

    public String getSubDesc() {
        return subDesc;
    }

    public void setSubDesc(String subDesc) {
        this.subDesc = subDesc;
    }

    public String getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public List<SurveySession> getSurveySessions() {
        return surveySessions;
    }

    public void setSurveySessions(List<SurveySession> surveySessions) {
        this.surveySessions = surveySessions;
    }

    public int getIsSent() {
        return isSent;
    }

    public void setIsSent(int isSent) {
        this.isSent = isSent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBeginingTimeStr() {
        return beginingTimeStr;
    }

    public void setBeginingTimeStr(String beginingTimeStr) {
        this.beginingTimeStr = beginingTimeStr;
    }

    public String getEndingTimeStr() {
        return endingTimeStr;
    }

    public void setEndingTimeStr(String endingTimeStr) {
        this.endingTimeStr = endingTimeStr;
    }
    
}