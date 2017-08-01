package com.amway.lms.backend.model;

public class ActionPlan {

    private int sessionParticipantId;
    private String todoPlan;
    private String startTime;
    private String endTime;    
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTodoPlan() {
        return todoPlan;
    }

    public int getSessionParticipantId() {
        return sessionParticipantId;
    }

    public void setSessionParticipantId(int sessionParticipantId) {
        this.sessionParticipantId = sessionParticipantId;
    }

    public void setTodoPlan(String todoPlan) {
        this.todoPlan = todoPlan;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
 

}
