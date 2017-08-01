package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the test_questions database table.
 * 
 */
@Entity
@Table(name = "test_questions")
@NamedQueries({ @NamedQuery(name = "TestQuestion.findAll", query = "SELECT t FROM TestQuestion t"),
        @NamedQuery(name = "getTestQuestionListByTestPartId", query = "SELECT t FROM TestQuestion t WHERE t.testPartId = ? ORDER BY t.indexQuestion"),
        @NamedQuery(name = "getTestQuestionListByTestId", query = "SELECT t FROM TestQuestion t WHERE t.testId = ? ORDER BY t.indexQuestion"),
        @NamedQuery(name = "getTestQuestionByTestIdAndQuestionId", query = "SELECT t FROM TestQuestion t WHERE t.testId = ? And t.questionId = ?"), })

public class TestQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "index_question")
    private int indexQuestion;

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "test_id")
    private int testId;

    @Column(name = "test_part_id")
    private int testPartId;

    @Transient
    private Question question;

    public TestQuestion() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndexQuestion() {
        return this.indexQuestion;
    }

    public void setIndexQuestion(int indexQuestion) {
        this.indexQuestion = indexQuestion;
    }

    public int getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getTestId() {
        return this.testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getTestPartId() {
        return this.testPartId;
    }

    public void setTestPartId(int testPartId) {
        this.testPartId = testPartId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}