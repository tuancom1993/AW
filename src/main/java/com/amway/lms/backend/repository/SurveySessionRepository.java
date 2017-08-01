/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.SurveySession;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface SurveySessionRepository {
    public SurveySession getSurveySessionById(int surveySessionId);
    public void addSurveySession(SurveySession surveySession);
    public void deleteSurveySession(SurveySession surveySession);
    public List<SurveySession> getSurveySessionListBySurveyId(int surveyId);
    public void editSurveySession(SurveySession surveySession);
}
