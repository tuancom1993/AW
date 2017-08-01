/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.SurveyQuestion;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.SurveyQuestionRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class SurveyQuestionRepositoryImpl extends AbstractRepository<Integer, SurveyQuestion> implements SurveyQuestionRepository {

    @Override
    public List<SurveyQuestion> getSurveyQuestionListBySurveyId(int surveyId) {
        Query query = createNamedQuery("getSurveyQuestionListBySurveyId", -1, -1, surveyId);
        return query.list();
//        String sql = "Select * from survey_questions s where s.survey_id = :surveyId";
//        SQLQuery query = getSession().createSQLQuery(sql);
//        query.setParameter("surveyId", surveyId);
//        query.addEntity(SurveyQuestion.class);
//        return query.list();
    }

    @Override
    public void addSurveyQuestion(SurveyQuestion surveyQuestion) {
        persist(surveyQuestion);
    }

    @Override
    public SurveyQuestion getSurveyQuestionBySurveyIdAndQuestionId(int surveyId, int questionId) {
        Query query = createNamedQuery("getSurveyQuestionBySurveyIdAndQuestionId", -1, -1, surveyId, questionId);
        List<SurveyQuestion> list = query.list();
        if (list.isEmpty()) return null;
        else return list.get(0);
    }

    @Override
    public void deleteSurveyQuestion(SurveyQuestion surveyQuestion) {
        delete(surveyQuestion);
    }

    @Override
    public int getSurveySessionIdBySurveyIdAndQuestionId(int surveyId, int questionId) {
        Query query = createNamedQuery("getSurveySessionIdBySurveyIdAndQuestionId", -1, -1, surveyId, questionId);
        
        try {
            return (int) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<SurveyQuestion> getSurveyQuestionListBySurveyIdOrderBySurveySessionId(int surveyId) {
        Query query = createNamedQuery("getSurveyQuestionListBySurveyIdOrderBySurveySessionId", -1, -1, surveyId);
        return query.list();
    }


    @Override
    public void deleteSurveyQuestionBySurveyIdAndSurveySessionId(int surveyId, int surveySessionId) {
        Query query = createNamedQuery("deleteSurveyQuestionBySurveySessionId", -1, -1,surveyId, surveySessionId);
        query.executeUpdate();
        
    }

    @Override
    public List<SurveyQuestion> getSurveyQuestionListBySurveyIdAndSurveySessionId(int surveyId, int surveySessionId) {
        Query query = createNamedQuery("getSurveyQuestionListBySurveyIdAndSurveySessionId", -1, -1,surveyId, surveySessionId);
        return query.list();
    }

    @Override
    public void deleteSurveyQuestionBySurveyId(int surveyId) {
        Query query = createNamedQuery("deleteSurveyQuestionBySurveyId", -1, -1, surveyId);
        query.executeUpdate();
    }


    @Override
    public List<Integer> getSurveySessionIdListBySurveyId(int surveyId) {
        Query query = createNamedQuery("getSurveySessionIdListBySurveyId", -1, -1, surveyId);
        return query.list();
    }

    @Override
    public void editSurveyQuestion(SurveyQuestion surveyQuestion) {
        update(surveyQuestion);
    }

}
