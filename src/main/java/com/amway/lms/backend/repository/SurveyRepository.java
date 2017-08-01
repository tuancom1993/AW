/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.Survey;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface SurveyRepository {
    public Survey getSurveyById(int surveyId);
    public List<Survey> getSurveyList();
    public void addSurvey(Survey survey);
    public void editSurvey(Survey survey);
    public void deleteSurvey(Survey survey);
    public List<Survey> getSurveyListByIsSent(int isSent);
}
