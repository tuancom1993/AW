/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.SurveyResult;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface SurveyResultRepository {
    public void addSurveyResult(SurveyResult surveyResult);
    public void deleteSurveyResultBySurveyId(int surveyId);
    public List<SurveyResult> getSurveyResultListBySurveyId(int surveyId);
}
