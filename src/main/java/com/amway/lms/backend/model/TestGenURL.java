package com.amway.lms.backend.model;

public class TestGenURL {
    private int testId;
    private int testParticipantId;

    public TestGenURL() {
    }

    public TestGenURL(int testId, int testParticipantId) {
        super();
        this.testId = testId;
        this.testParticipantId = testParticipantId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getTestParticipantId() {
        return testParticipantId;
    }

    public void setTestParticipantId(int testParticipantId) {
        this.testParticipantId = testParticipantId;
    }

}
