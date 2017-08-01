package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the survey_sent database table.
 * 
 */
@Entity
@Table(name = "survey_sents")
@NamedQueries({
    @NamedQuery(name = "getSurveySentList", query = "SELECT s FROM SurveySent s"),
    @NamedQuery(name = "getSurveySentBySurveyId", query = "SELECT s FROM SurveySent s WHERE s.surveyId = ?"),
    @NamedQuery(name = "getLastSurveySentBySurveyId", query = "SELECT s FROM SurveySent s WHERE s.surveyId = ? ORDER BY s.id DESC"),
    @NamedQuery(name = "getFirstSurveySentBySurveyId", query = "SELECT s FROM SurveySent s WHERE s.surveyId = ? ORDER BY s.id ASC")
})

public class SurveySent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Lob
    @Column(name = "html_content")
    private String htmlContent;

    @Column(name = "link_survey")
    private String linkSurvey;

    @Column(name = "sent_time")
    private Timestamp sentTime;

    private String subject;

    @Column(name = "survey_id")
    private int surveyId;

    @Column(name = "is_remind")
    private int isRemind;
    
    @Column(name = "remind_time")
    private Timestamp remindTime;
    
    public SurveySent() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHtmlContent() {
        return this.htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getLinkSurvey() {
        return this.linkSurvey;
    }

    public void setLinkSurvey(String linkSurvey) {
        this.linkSurvey = linkSurvey;
    }

    public Timestamp getSentTime() {
        return this.sentTime;
    }

    public void setSentTime(Timestamp sentTime) {
        this.sentTime = sentTime;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getSurveyId() {
        return this.surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public int getIsRemind() {
        return isRemind;
    }

    public void setIsRemind(int isRemind) {
        this.isRemind = isRemind;
    }

    public Timestamp getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Timestamp remindTime) {
        this.remindTime = remindTime;
    }

}