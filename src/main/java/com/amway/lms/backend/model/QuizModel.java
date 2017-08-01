package com.amway.lms.backend.model;

import java.util.List;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public class QuizModel {
    private String title;
    private String name;
    private String questionTitleTemplate;
    private int quizId;
    private int courseId;
    private int sessionId;
    private int userId;
    private List<PageModel> pages;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getQuestionTitleTemplate() {
        return questionTitleTemplate;
    }
    public void setQuestionTitleTemplate(String questionTitleTemplate) {
        this.questionTitleTemplate = questionTitleTemplate;
    }
    public List<PageModel> getPages() {
        return pages;
    }
    public void setPages(List<PageModel> pages) {
        this.pages = pages;
    }
    public int getQuizId() {
        return quizId;
    }
    public void setQuizId(int quizId) {
        this.quizId = quizId;
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
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    
}
