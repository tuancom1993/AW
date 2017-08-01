package com.amway.lms.backend.model;

public class TestPartResultView {
    private int testPartId;
    private String testPartDesc;
    private int numberQuestionInPart;
    private int totalCorrectAnswerInPart;

    public int getTestPartId() {
        return testPartId;
    }

    public void setTestPartId(int testPartId) {
        this.testPartId = testPartId;
    }

    public String getTestPartDesc() {
        return testPartDesc;
    }

    public void setTestPartDesc(String testPartDesc) {
        this.testPartDesc = testPartDesc;
    }

    public int getNumberQuestionInPart() {
        return numberQuestionInPart;
    }

    public void setNumberQuestionInPart(int numberQuestionInPart) {
        this.numberQuestionInPart = numberQuestionInPart;
    }

    public int getTotalCorrectAnswerInPart() {
        return totalCorrectAnswerInPart;
    }

    public void setTotalCorrectAnswerInPart(int totalCorrectAnswerInPart) {
        this.totalCorrectAnswerInPart = totalCorrectAnswerInPart;
    }

}
