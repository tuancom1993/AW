package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the quiz_results database table.
 * 
 */
@Entity
@Table(name = "quiz_results")
@NamedQuery(name = "QuizResult.findAll", query = "SELECT q FROM QuizResult q")
public class QuizResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    @Column(name = "create_at")
    private Timestamp createAt;

    @Column(name = "quiz_id")
    private int quizId;

    @Column(name = "user_id")
    private int userId;
    
    @Column(name = "course_id")
    private int courseId;
    
    @Column(name = "session_id")
    private int sessionId;

    @Transient
    private List<Result> results;

    public QuizResult() {
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

    public int getQuizId() {
        return this.quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}