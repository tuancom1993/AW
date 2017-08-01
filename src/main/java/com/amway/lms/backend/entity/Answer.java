package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the answers database table.
 * 
 */
@Entity
@Table(name = "answers")
@NamedQueries({
        @NamedQuery(name = "getAnswerListByQuestionId", query = "SELECT a FROM Answer a Where a.questionId = ?"),
        @NamedQuery(name = "getAnswerListByQuestionIdAndIsCorrect", query = "SELECT a FROM Answer a Where a.questionId = ? And a.correct = 1") })

public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "answer_desc")
    private String answerDesc;

    private Integer correct;

    @Column(name = "question_id")
    private int questionId;
    
    @Transient
    @JsonIgnore
    private int chooseTimes;

    public Answer() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswerDesc() {
        return this.answerDesc;
    }

    public void setAnswerDesc(String answerDesc) {
        this.answerDesc = answerDesc;
    }

    public Integer getCorrect() {
        return this.correct;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public int getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getChooseTimes() {
        return chooseTimes;
    }

    public void setChooseTimes(int chooseTimes) {
        this.chooseTimes = chooseTimes;
    }

    
}