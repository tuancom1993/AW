package com.amway.lms.backend.model;

public class CourseSessionStatus {
    private int notStarted;
    private int inProgress;
    private int completed;
    private int inactiveSession;
    public int getNotStarted() {
        return notStarted;
    }
    public void setNotStarted(int notStarted) {
        this.notStarted = notStarted;
    }
    public int getInProgress() {
        return inProgress;
    }
    public void setInProgress(int inProgress) {
        this.inProgress = inProgress;
    }
    public int getCompleted() {
        return completed;
    }
    public void setCompleted(int completed) {
        this.completed = completed;
    }
    public int getInactiveSession() {
        return inactiveSession;
    }
    public void setInactiveSession(int inactiveSession) {
        this.inactiveSession = inactiveSession;
    }
        
}
