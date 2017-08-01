/**    
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.SurveyQuestion;
import com.amway.lms.backend.entity.SurveySession;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface SurveyQuestionRepository {
    public List<SurveyQuestion> getSurveyQuestionListBySurveyId(int surveyId);
    public List<SurveyQuestion> getSurveyQuestionListBySurveyIdOrderBySurveySessionId(int surveyId);
    public void addSurveyQuestion(SurveyQuestion surveyQuestion);
    public SurveyQuestion getSurveyQuestionBySurveyIdAndQuestionId(int surveyId, int questionId);
    public void deleteSurveyQuestion(SurveyQuestion surveyQuestion);
    public int getSurveySessionIdBySurveyIdAndQuestionId(int surveyId, int questionId);
    public void deleteSurveyQuestionBySurveyIdAndSurveySessionId(int surveyId, int surveySessionId);
    public List<SurveyQuestion> getSurveyQuestionListBySurveyIdAndSurveySessionId(int surveyId, int surveySessionId);
    public void deleteSurveyQuestionBySurveyId(int surveyId);
    public List<Integer> getSurveySessionIdListBySurveyId(int surveyId);
    public void editSurveyQuestion(SurveyQuestion surveyQuestion);
}
