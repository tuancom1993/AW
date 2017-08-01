package com.amway.lms.backend.model;

import java.sql.Timestamp;

public class CourseOfTrainingPlanDashBoard {
    private int id;
    private String title;
    private Timestamp start;
    private Timestamp end;
    private String sessionStatus;
    private String backgroundColor;
    private String borderColor;
    private String courseStatus;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Timestamp getStart() {
        return start;
    }
    public void setStart(Timestamp start) {
        this.start = start;
    }
    public Timestamp getEnd() {
        return end;
    }
    public void setEnd(Timestamp end) {
        this.end = end;
    }
    public String getSessionStatus() {
        return sessionStatus;
    }
    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }
    public String getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public String getBorderColor() {
        return borderColor;
    }
    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }
    public String getCourseStatus() {
        return courseStatus;
    }
    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }    
}
