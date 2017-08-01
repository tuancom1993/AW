package com.amway.lms.backend.model;

public class TestInformation {

    private int id;

    private String startDate;

    private String endDate;

    private int numberOfQuestionPass;
    
    private int numberOfQuestion;

    private int maxTestingTime;

    private String testTitle;

    private String topic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfQuestionPass() {
        return numberOfQuestionPass;
    }

    public void setNumberOfQuestionPass(int numberOfQuestionPass) {
        this.numberOfQuestionPass = numberOfQuestionPass;
    }

    public int getMaxTestingTime() {
        return maxTestingTime;
    }

    public void setMaxTestingTime(int maxTestingTime) {
        this.maxTestingTime = maxTestingTime;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getNumberOfQuestion() {
        return numberOfQuestion;
    }

    public void setNumberOfQuestion(int numberOfQuestion) {
        this.numberOfQuestion = numberOfQuestion;
    }

}
