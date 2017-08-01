package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the survey_questions database table.
 * 
 */
@Entity
@Table(name = "survey_questions")
@NamedQueries({
        @NamedQuery(name = "getSurveyQuestionListBySurveyId", query = "SELECT s FROM SurveyQuestion s Where s.surveyId = ?"),
        @NamedQuery(name = "getSurveyQuestionListBySurveyIdOrderBySurveySessionId", query = "SELECT s FROM SurveyQuestion s Where s.surveyId = ? Order By s.surveySessionId, s.indexQuestion"),
        @NamedQuery(name = "getSurveyQuestionBySurveyIdAndQuestionId", query = "SELECT s FROM SurveyQuestion s Where s.surveyId = ? and s.questionId = ?"),
        @NamedQuery(name = "getSurveySessionIdBySurveyIdAndQuestionId", query = "SELECT s.surveySessionId FROM SurveyQuestion s Where s.surveyId = ? and s.questionId = ?"),
        @NamedQuery(name = "deleteSurveyQuestionBySurveyIdAndSurveySessionId", query = "DELETE FROM SurveyQuestion s WHERE s.surveyId =? And s.surveySessionId = ?"),
        @NamedQuery(name = "getSurveyQuestionListBySurveyIdAndSurveySessionId", query = "SELECT s FROM SurveyQuestion s Where s.surveyId = ? and s.surveySessionId = ? Order By s.indexQuestion"),
        @NamedQuery(name = "deleteSurveyQuestionBySurveyId", query = "DELETE FROM SurveyQuestion s WHERE s.surveyId =?"),
        @NamedQuery(name = "getSurveySessionIdListBySurveyId", query = "Select sq.surveySessionId From SurveyQuestion sq Where sq.surveyId = ? And sq.surveySessionId In (Select s.id FROM SurveySession s) Group By sq.surveySessionId")
})

public class SurveyQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "survey_id")
    private int surveyId;
    
    @Column(name = "survey_session_id")
    private int surveySessionId;

    @Column(name = "index_question")
    private int indexQuestion;
    
    @Transient
    private Question question;

    public SurveyQuestion() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getSurveyId() {
        return this.surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getSurveySessionId() {
        return surveySessionId;
    }

    public void setSurveySessionId(int surveySessionId) {
        this.surveySessionId = surveySessionId;
    }

    public int getIndexQuestion() {
        return indexQuestion;
    }

    public void setIndexQuestion(int indexQuestion) {
        this.indexQuestion = indexQuestion;
    }

}