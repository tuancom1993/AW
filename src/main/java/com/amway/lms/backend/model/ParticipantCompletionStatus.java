package com.amway.lms.backend.model;

public class ParticipantCompletionStatus {
    private int waitingForResponding;
    private int accepted;
    private int denied;
    private int participated;
    private int endedSession;
    private int completed;
    
    public int getWaitingForResponding() {
        return waitingForResponding;
    }
    public void setWaitingForResponding(int waitingForResponding) {
        this.waitingForResponding = waitingForResponding;
    }
    public int getAccepted() {
        return accepted;
    }
    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }
    public int getDenied() {
        return denied;
    }
    public void setDenied(int denied) {
        this.denied = denied;
    }
    public int getParticipated() {
        return participated;
    }
    public void setParticipated(int participated) {
        this.participated = participated;
    }
    public int getEndedSession() {
        return endedSession;
    }
    public void setEndedSession(int endedSession) {
        this.endedSession = endedSession;
    }
    public int getCompleted() {
        return completed;
    }
    public void setCompleted(int completed) {
        this.completed = completed;
    }

    
}
