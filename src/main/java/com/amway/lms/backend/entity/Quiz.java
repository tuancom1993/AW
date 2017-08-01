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
 * The persistent class for the quiz database table.
 * 
 */
@Entity
@NamedQueries({ 
    @NamedQuery(name = "getQuizList", query = "SELECT q FROM Quiz q Order By q.id Desc"),
    @NamedQuery(name = "getQuizByCourseId", query = "SELECT q FROM Quiz q Where q.courseId = ?"),
    @NamedQuery(name = "searchQuizByName", query = "SELECT q FROM Quiz q Where q.title Like ?")
})
@Table(name = "quiz")
public class Quiz implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name = "amount_question_passed")
    private int amountQuestionPassed;

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "max_test_times")
    private int maxTestTimes;

    @Column(name = "number_questions")
    private int numberQuestions;

    private String title;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "expire_day")
    private int expireDay;
    
    @Transient
    private List<Question> questions;

    public Quiz() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmountQuestionPassed() {
        return this.amountQuestionPassed;
    }

    public void setAmountQuestionPassed(int amountQuestionPassed) {
        this.amountQuestionPassed = amountQuestionPassed;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getMaxTestTimes() {
        return this.maxTestTimes;
    }

    public void setMaxTestTimes(int maxTestTimes) {
        this.maxTestTimes = maxTestTimes;
    }

    public int getNumberQuestions() {
        return this.numberQuestions;
    }

    public void setNumberQuestions(int numberQuestions) {
        this.numberQuestions = numberQuestions;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getExpireDay() {
        return expireDay;
    }

    public void setExpireDay(int expireDay) {
        this.expireDay = expireDay;
    }
}