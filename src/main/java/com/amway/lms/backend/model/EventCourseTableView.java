package com.amway.lms.backend.model;

public class EventCourseTableView {
    private int sessionParticipantId;
    private String courseName;
    private String sessionName;
    private String courseStatus;
    private int trainingActionStatus;
    private int sessionId;
    
    public int getSessionParticipantId() {
        return sessionParticipantId;
    }
    public void setSessionParticipantId(int sessionParticipantId) {
        this.sessionParticipantId = sessionParticipantId;
    }
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getSessionName() {
        return sessionName;
    }
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
    public String getCourseStatus() {
        return courseStatus;
    }
    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }
    public int getTrainingActionStatus() {
        return trainingActionStatus;
    }
    public void setTrainingActionStatus(int trainingActionStatus) {
        this.trainingActionStatus = trainingActionStatus;
    }
    public int getSessionId() {
        return sessionId;
    }
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
    
    

}
