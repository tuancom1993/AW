package com.amway.lms.backend.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the survey_sessions database table.
 * 
 */
@Entity
@Table(name = "survey_sessions")
@NamedQueries({
    @NamedQuery(name = "SurveySession.findAll", query = "SELECT s FROM SurveySession s"),
    @NamedQuery(name = "getSurveySessionListBySurveyId", query = "SELECT s FROM SurveySession s Where s.id In (Select sq.surveySessionId From SurveyQuestion sq Where sq.surveyId = ?) Order By s.id")
})
public class SurveySession implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "session_desc")
    private String sessionDesc;
    
    @Transient
    private String table;
    
    @Transient
    private List<Question> questions;

    public SurveySession() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSessionDesc() {
        return this.sessionDesc;
    }

    public void setSessionDesc(String sessionDesc) {
        this.sessionDesc = sessionDesc;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

}