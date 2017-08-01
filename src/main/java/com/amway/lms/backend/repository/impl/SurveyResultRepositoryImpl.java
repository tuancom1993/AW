/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.SurveyResult;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.SurveyResultRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class SurveyResultRepositoryImpl extends AbstractRepository<Integer, SurveyResult> implements SurveyResultRepository{
    
    @Override
    public void addSurveyResult(SurveyResult surveyResult) {
        persist(surveyResult);
    }

    @Override
    public void deleteSurveyResultBySurveyId(int surveyId) {
        Query query = createNamedQuery("deleteSurveyResultBySurveyId", -1, -1, surveyId);
        query.executeUpdate();
    }

    @Override
    public List<SurveyResult> getSurveyResultListBySurveyId(int surveyId) {
        Query query = createNamedQuery("getSurveyResultListBySurveyId", -1, -1, surveyId);
        return query.list();
    }
}
