package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the survey_results database table.
 * 
 */
@Entity
@Table(name = "survey_results")
@NamedQueries({
    @NamedQuery(name = "SurveyResult.findAll", query = "SELECT s FROM SurveyResult s"),
    @NamedQuery(name = "deleteSurveyResultBySurveyId", query = "DELETE FROM SurveyResult s WHERE s.surveyId = ?"),
    @NamedQuery(name = "getSurveyResultListBySurveyId", query = "SELECT s FROM SurveyResult s Where surveyId = ?"),
    
})

public class SurveyResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "create_at")
    private Timestamp createAt;

    @Column(name = "survey_id")
    private int surveyId;

    @Column(name = "user_id")
    private Integer userId;

    @Transient
    private List<Result> results;

    public SurveyResult() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getCreateAt() {
        return this.createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public int getSurveyId() {
        return this.surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
    
}