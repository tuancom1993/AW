package com.amway.lms.backend.model;

import java.util.List;

import javax.persistence.Transient;

import com.amway.lms.backend.entity.Result;

public class TestResultModel {
    private int testId;
    private int userId;
    private List<Result> results;
    
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
    public List<Result> getResults() {
        return results;
    }
    public void setResults(List<Result> results) {
        this.results = results;
    }
    
    
}
