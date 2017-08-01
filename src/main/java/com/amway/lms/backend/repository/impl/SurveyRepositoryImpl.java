/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.entity.Survey;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.SurveyRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class SurveyRepositoryImpl extends AbstractRepository<Integer, Survey> implements SurveyRepository{

    @Override
    public Survey getSurveyById(int surveyId) {
        Query query = createNamedQuery("getSurveyById", -1 , -1, surveyId);
        return (Survey) query.uniqueResult();
    }

    @Override
    public List<Survey> getSurveyList() {
        Query query = createNamedQuery("getSurveyList", -1 , -1);
        return query.list();
    }

    @Override
    public void addSurvey(Survey survey) {
        survey.setBeginingTime(Common.setTimeToZero(survey.getBeginingTime()));
        survey.setEndingTime(Common.setTimeToMidnight(survey.getEndingTime()));
        persist(survey);
    }

    @Override
    public void editSurvey(Survey survey) {
        survey.setBeginingTime(Common.setTimeToZero(survey.getBeginingTime()));
        survey.setEndingTime(Common.setTimeToMidnight(survey.getEndingTime()));
        update(survey);
    }

    @Override
    public void deleteSurvey(Survey survey) {
        delete(survey);
        
    }

    @Override
    public List<Survey> getSurveyListByIsSent(int isSent) {
        Query query = createNamedQuery("getSurveyListByIsSent", -1 , -1, isSent);
        return query.list();
    }

}
