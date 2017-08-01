package com.amway.lms.backend.model;

import java.util.List;

import com.amway.lms.backend.entity.SurveySession;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public class SurveyModelForCreate {
    private SurveyInformation surveyInfo;
    private List<SurveySession> parts;

    public SurveyInformation getSurveyInfo() {
        return surveyInfo;
    }

    public void setSurveyInfo(SurveyInformation surveyInfo) {
        this.surveyInfo = surveyInfo;
    }

    public List<SurveySession> getParts() {
        return parts;
    }

    public void setParts(List<SurveySession> parts) {
        this.parts = parts;
    }
}
