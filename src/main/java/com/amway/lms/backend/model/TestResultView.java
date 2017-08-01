package com.amway.lms.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TestResultView {
    private int testId;
    private int totalCorrectAnswer;
    private int amountQuestionPassed;
    private int numberOfTested;
    private String testStatus;
    private String testResultStatus;
    @JsonIgnore
    private int testStatusInt;
    @JsonIgnore
    private int testResultStatusInt;
    private int maxTestTimes;
    private int numberQuestions;
    private List<TestPartResultView> testPartResults;

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getTotalCorrectAnswer() {
        return totalCorrectAnswer;
    }

    public void setTotalCorrectAnswer(int totalCorrectAnswer) {
        this.totalCorrectAnswer = totalCorrectAnswer;
    }

    public int getAmountQuestionPassed() {
        return amountQuestionPassed;
    }

    public void setAmountQuestionPassed(int amountQuestionPassed) {
        this.amountQuestionPassed = amountQuestionPassed;
    }

    public int getNumberOfTested() {
        return numberOfTested;
    }

    public void setNumberOfTested(int numberOfTested) {
        this.numberOfTested = numberOfTested;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public String getTestResultStatus() {
        return testResultStatus;
    }

    public void setTestResultStatus(String testResultStatus) {
        this.testResultStatus = testResultStatus;
    }

    public int getTestStatusInt() {
        return testStatusInt;
    }

    public void setTestStatusInt(int testStatusInt) {
        this.testStatusInt = testStatusInt;
    }

    public int getTestResultStatusInt() {
        return testResultStatusInt;
    }

    public void setTestResultStatusInt(int testResultStatusInt) {
        this.testResultStatusInt = testResultStatusInt;
    }

    public int getMaxTestTimes() {
        return maxTestTimes;
    }

    public void setMaxTestTimes(int maxTestTimes) {
        this.maxTestTimes = maxTestTimes;
    }

    public int getNumberQuestions() {
        return numberQuestions;
    }

    public void setNumberQuestions(int numberQuestions) {
        this.numberQuestions = numberQuestions;
    }

    public List<TestPartResultView> getTestPartResults() {
        return testPartResults;
    }

    public void setTestPartResults(List<TestPartResultView> testPartResults) {
        this.testPartResults = testPartResults;
    }

}
