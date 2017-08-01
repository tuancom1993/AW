package com.amway.lms.backend.model;

import java.sql.Timestamp;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public class SurveyInformation {
    
    private Timestamp beginingTime;
    
    private String beginingTimeStr;

    private Timestamp endingTime;
    
    private String endingTimeStr;

    private String surveyTitle;

    private String surveyDesc;

    private int surveyTypeId;

    private String subDesc;

    public Timestamp getBeginingTime() {
        return beginingTime;
    }

    public void setBeginingTime(Timestamp beginingTime) {
        this.beginingTime = beginingTime;
    }

    public Timestamp getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Timestamp endingTime) {
        this.endingTime = endingTime;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
    }

    public String getSurveyDesc() {
        return surveyDesc;
    }

    public void setSurveyDesc(String surveyDesc) {
        this.surveyDesc = surveyDesc;
    }

    public int getSurveyTypeId() {
        return surveyTypeId;
    }

    public void setSurveyTypeId(int surveyTypeId) {
        this.surveyTypeId = surveyTypeId;
    }

    public String getSubDesc() {
        return subDesc;
    }

    public void setSubDesc(String subDesc) {
        this.subDesc = subDesc;
    }

    public String getBeginingTimeStr() {
        return beginingTimeStr;
    }

    public void setBeginingTimeStr(String beginingTimeStr) {
        this.beginingTimeStr = beginingTimeStr;
    }

    public String getEndingTimeStr() {
        return endingTimeStr;
    }

    public void setEndingTimeStr(String endingTimeStr) {
        this.endingTimeStr = endingTimeStr;
    }

}
