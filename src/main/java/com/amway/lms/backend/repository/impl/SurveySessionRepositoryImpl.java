/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.SurveySession;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.SurveySessionRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class SurveySessionRepositoryImpl extends AbstractRepository<Integer, SurveySession> implements SurveySessionRepository{

    @Override
    public SurveySession getSurveySessionById(int surveySessionId) {
        return getByKey(surveySessionId);
    }

    @Override
    public void addSurveySession(SurveySession surveySession) {
        persist(surveySession);
    }

    @Override
    public void deleteSurveySession(SurveySession surveySession) {
        delete(surveySession);
    }

    @Override
    public List<SurveySession> getSurveySessionListBySurveyId(int surveyId) {
        Query query = createNamedQuery("getSurveySessionListBySurveyId", -1, -1, surveyId);
        return query.list();
    }

    @Override
    public void editSurveySession(SurveySession surveySession) {
        update(surveySession);
    }


}
