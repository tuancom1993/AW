package com.amway.lms.backend.model;

public class TestParticipantResultView {
    private String traineeName;
    private int numberOfTested;
    private String testResultStatus;
    public String getTraineeName() {
        return traineeName;
    }
    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }
    public int getNumberOfTested() {
        return numberOfTested;
    }
    public void setNumberOfTested(int numberOfTested) {
        this.numberOfTested = numberOfTested;
    }
    public String getTestResultStatus() {
        return testResultStatus;
    }
    public void setTestResultStatus(String testResultStatus) {
        this.testResultStatus = testResultStatus;
    }
}
