package com.amway.lms.backend.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the questions database table.
 * 
 */
@Entity
@Table(name = "questions")
@NamedQueries({
    @NamedQuery(name = "getQuestionById", query = "SELECT q FROM Question q Where q.id = ?"),
//    @NamedQuery(name = "getSurveyQuestionList", query = "Select s From Question s Where s.isQuiz = 0"),
//    @NamedQuery(name = "getQuizQuestionList", query = "Select s From Question s Where s.isQuiz = 1"),
    @NamedQuery(name = "getQuestionListNotInSurvey", query = "Select s From Question s Where s.isQuiz = 0 And s.id Not In (Select sq.questionId From SurveyQuestion sq Where sq.surveyId = ?) And s.id Not In (Select q.parentQuestionId From Question q Where q.parentQuestionId > 0)"),
    @NamedQuery(name = "getQuestionListNotInQuiz", query = "Select s From Question s Where s.isQuiz = 1 And s.id Not In (Select qq.questionId From QuizQuestion qq Where qq.quizId = ?) And s.id Not In (Select q.parentQuestionId From Question q Where q.parentQuestionId > 0)"),
    @NamedQuery(name = "getQuestionListNotInTest", query = "Select s From Question s Where s.isQuiz = 2 And s.id Not In (Select tq.questionId From TestQuestion tq Where tq.testId = ?) And s.id Not In (Select q.parentQuestionId From Question q Where q.parentQuestionId > 0)")
    })

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Transient
    @JsonIgnore
    private SurveySession surveySession; 

    @Column(name = "is_quiz")
    private int isQuiz;

    @Lob
    @Column(name = "question_desc")
    private String questionDesc;

    @Column(name = "question_type_id")
    private int questionTypeId;
    
    @Transient
    private String questionType;
    
    @Column(name ="is_required")
    private int isRequired;
    
    @Column(name="file_location")
    private String fileLocation;
    
    @Column(name="parent_question_id")
    private int parentQuestionId;
    
    @Transient
    private List<Answer> answers;
    
    @Transient 
    private List<MatrixColumn> matrixColumns;
    
    @Transient
    private List<MatrixRow> matrixRows;
    
    @Transient
    private List<MatrixCorrect> matrixCorrects;
    
    @Transient
    private int no;
    
    @Transient
    @JsonIgnore
    private long total;
    
    public Question() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsQuiz() {
        return this.isQuiz;
    }

    public void setIsQuiz(int isQuiz) {
        this.isQuiz = isQuiz;
    }

    public String getQuestionDesc() {
        return this.questionDesc;
    }

    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }

    public int getQuestionTypeId() {
        return this.questionTypeId;
    }

    public void setQuestionTypeId(int questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<MatrixColumn> getMatrixColumns() {
        return matrixColumns;
    }

    public void setMatrixColumns(List<MatrixColumn> matrixColumns) {
        this.matrixColumns = matrixColumns;
    }

    public List<MatrixRow> getMatrixRows() {
        return matrixRows;
    }

    public void setMatrixRows(List<MatrixRow> matrixRows) {
        this.matrixRows = matrixRows;
    }

    public int getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(int isRequired) {
        this.isRequired = isRequired;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public List<MatrixCorrect> getMatrixCorrects() {
        return matrixCorrects;
    }

    public void setMatrixCorrects(List<MatrixCorrect> matrixCorrects) {
        this.matrixCorrects = matrixCorrects;
    }

    public SurveySession getSurveySession() {
        return surveySession;
    }

    public void setSurveySession(SurveySession surveySession) {
        this.surveySession = surveySession;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getParentQuestionId() {
        return parentQuestionId;
    }

    public void setParentQuestionId(int parentQuestionId) {
        this.parentQuestionId = parentQuestionId;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }


}