package com.amway.lms.backend.model;

import java.util.List;

public class TestModel {
    private String title;
    private String name;
    private String questionTitleTemplate;
    private int testId;
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
    public int getTestId() {
        return testId;
    }
    public void setTestId(int testId) {
        this.testId = testId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public List<PageModel> getPages() {
        return pages;
    }
    public void setPages(List<PageModel> pages) {
        this.pages = pages;
    }
    
}
