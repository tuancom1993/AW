package com.amway.lms.backend.model;

public class TestParticipantInfoModel {
    private int testParticipantId;
    private String testTitle;
    private String testResultStatus;
    private int correctAnswer;
    private String testStatus;
    private int takeTest;
    private String urlTesting;
    
    public int getTestParticipantId() {
        return testParticipantId;
    }
    public void setTestParticipantId(int testParticipantId) {
        this.testParticipantId = testParticipantId;
    }
    public String getTestTitle() {
        return testTitle;
    }
    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }
    public String getTestResultStatus() {
        return testResultStatus;
    }
    public void setTestResultStatus(String testResultStatus) {
        this.testResultStatus = testResultStatus;
    }
    public int getCorrectAnswer() {
        return correctAnswer;
    }
    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    public String getTestStatus() {
        return testStatus;
    }
    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }
    public int getTakeTest() {
        return takeTest;
    }
    public void setTakeTest(int takeTest) {
        this.takeTest = takeTest;
    }
    public String getUrlTesting() {
        return urlTesting;
    }
    public void setUrlTesting(String urlTesting) {
        this.urlTesting = urlTesting;
    }

}
