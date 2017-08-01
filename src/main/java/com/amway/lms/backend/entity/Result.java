package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the results database table.
 * 
 */
@Entity
@Table(name = "results")
@NamedQueries({
    @NamedQuery(name = "deleteResultBySurveyIdAndQuestionId", query = "Delete FROM Result r Where r.surveyId = ? and r.questionId = ?"),
    @NamedQuery(name = "getResultsBySurveyIdAndQuestionId", query = "Select r FROM Result r Where r.surveyId = ? and r.questionId = ?"),
    @NamedQuery(name = "countResultBySurveyIdQuestionIdMatrixRowIdMatrixColumnId", query = "Select count(r.id) FROM Result r Where r.surveyId = ? and r.questionId = ? and r.matrixRowId = ? and r.matrixColumnId = ?"),
    @NamedQuery(name = "countResultBySurveyIdQuestionIdMatrixRowId", query = "Select count(r.id) FROM Result r Where r.surveyId = ? and r.questionId = ? and r.matrixRowId = ?"),
    @NamedQuery(name = "deleteResultBySurveyId", query = "Delete FROM Result r Where r.surveyId = ?"),
    @NamedQuery(name = "getResultsBySurveyResultIdSurveyIdAndQuestionId", query = "Select r FROM Result r Where r.surveyResultId = ? and r.surveyId = ? and r.questionId = ?"),
    @NamedQuery(name = "getResultsBySurveyResultIdSurveyIdAndQuestionIdMatrixRowIdMatrixColumnId", query = "Select r FROM Result r Where r.surveyResultId = ? and r.surveyId = ? and r.questionId = ? and r.matrixRowId = ? and matrixColumnId = ?"),
})

public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "answer_content")
    private String answerContent;

    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "quiz_id")
    private Integer quizId;

    @Column(name = "quiz_result_id")
    private Integer quizResultId;

    @Column(name = "survey_id")
    private Integer surveyId;

    @Column(name = "survey_result_id")
    private Integer surveyResultId;
    
    @Column(name = "answer_id")
    private Integer answerId;
    
    @Column(name = "matrix_row_id")
    private Integer matrixRowId;
    
    @Column(name = "matrix_column_id")
    private Integer matrixColumnId;

    public Result() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }


    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public Integer getQuizResultId() {
        return quizResultId;
    }

    public void setQuizResultId(Integer quizResultId) {
        this.quizResultId = quizResultId;
    }

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public Integer getSurveyResultId() {
        return surveyResultId;
    }

    public void setSurveyResultId(Integer surveyResultId) {
        this.surveyResultId = surveyResultId;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public Integer getMatrixRowId() {
        return matrixRowId;
    }

    public void setMatrixRowId(Integer matrixRowId) {
        this.matrixRowId = matrixRowId;
    }

    public Integer getMatrixColumnId() {
        return matrixColumnId;
    }

    public void setMatrixColumnId(Integer matrixColumnId) {
        this.matrixColumnId = matrixColumnId;
    }
    
}