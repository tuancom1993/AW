package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.SurveySent;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.SurveySentRepository;

@Repository
public class SurveySentRepositoryImpl extends AbstractRepository<Integer, SurveySent> implements SurveySentRepository{

    @Override
    public void addSurveySent(SurveySent surveySent) {
        persist(surveySent);
    }

    @Override
    public void deleteSureySent(SurveySent surveySent) {
        delete(surveySent);
    }

    @Override
    public List<SurveySent> getSurveySentList() {
        Query query = createNamedQuery("getSurveySentList", -1, -1);
        return query.list();
    }

    @Override
    public SurveySent getSurveySentById(int surveySentId) {
        return getByKey(surveySentId);
    }

    @Override
    public List<SurveySent> getSurveySentBySurveyId(int surveyId) {
        Query query = createNamedQuery("getSurveySentBySurveyId", -1, -1, surveyId);
        return query.list();
    }

    @Override
    public SurveySent getLastSurveySentBySurveyId(int surveyId) {
        Query query = createNamedQuery("getLastSurveySentBySurveyId", -1, 1, surveyId);
        return (SurveySent) query.uniqueResult();
    }

    @Override
    public SurveySent getFirstSurveySentBySurveyId(int surveyId) {
        Query query = createNamedQuery("getFirstSurveySentBySurveyId", -1, 1, surveyId);
        return (SurveySent) query.uniqueResult();
    }

    @Override
    public void updateSurveySent(SurveySent surveySent) {
        update(surveySent);
        
    }

}
