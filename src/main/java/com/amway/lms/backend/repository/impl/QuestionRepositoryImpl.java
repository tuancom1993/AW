/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.Question;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.QuestionRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class QuestionRepositoryImpl extends AbstractRepository<Integer, Question> implements QuestionRepository{
    
    @Override
    public Question getQuestionById(int questionId) {
        Query query = createNamedQuery("getQuestionById", -1, -1, questionId);
        return (Question) query.uniqueResult();
    }

    @Override
    public List<Question> getQuestionListBySurveyIdOrderBySurveySessionId(int surveyId) {
//        Query query = createNamedQuery("getQuestionListBySurveyIdOrderByCategoryId", -1, -1, surveyId);
//        return query.list();
        
//        String sql = "Select * from questions q "
//                + "Inner Join survey_questions sq "
//                + "On q.id = sq.question_id "
//                + "Where sq.survey_id = :surveyId order by q.category_id";
        String sql = "Select * from questions q "
                + "Inner Join survey_questions sq "
                + "On q.id = sq.question_id "
                + "Where sq.survey_id = :surveyId order by sq.survey_session_id";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("surveyId", surveyId);
        query.addEntity(Question.class);
        return query.list();
    }

    @Override
    public List<Question> getQuestionListBySurveyIdAndQuestionIdLager6(int surveyId) {
//        Query query = createNamedQuery("getQuestionListBySurveyId", -1, -1, surveyId);
//        return query.list();
        
//        String sql = "Select * from questions q "
//                + "Inner Join survey_questions sq "
//                + "On q.id = sq.question_id "
//                + "Where sq.survey_id = :surveyId "
//                + "And q.id > 6";
//        SQLQuery query = getSession().createSQLQuery(sql);
//        query.setParameter("surveyId", surveyId);
//        query.addEntity(Question.class);
//        return query.list();
        return null;
    }

    /* (non-Javadoc)
     * @see com.amway.lms.backend.repository.QuestionRepository#addNewQuestion(com.amway.lms.backend.entity.Question)
     */
    @Override
    public void addQuestion(Question question) {
        persist(question);
    }

//    @Override
//    public List<Question> getSurveyQuestionList() {
//        Query query = createNamedQuery("getSurveyQuestionList", -1, -1);
//        return query.list();
//    }
//
//    @Override
//    public List<Question> getQuizQuestionList() {
//        Query query = createNamedQuery("getQuizQuestionList", -1, -1);
//        return query.list();
//    }

    /* (non-Javadoc)
     * @see com.amway.lms.backend.repository.QuestionRepository#getQuestionListBySurveyId()
     */
    @Override
    public List<Question> getQuestionListBySurveyId(int surveyId) {
        String sql = "Select * from questions q "
                + "Inner Join survey_questions sq "
                + "On q.id = sq.question_id "
                + "Where sq.survey_id = :surveyId ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("surveyId", surveyId);
        query.addEntity(Question.class);
        return query.list();
    }

    @Override
    public List<Question> getQuestionListNotInSurvey(int surveyId) {
        Query query = createNamedQuery("getQuestionListNotInSurvey", -1, -1, surveyId);
        return query.list();
    }

    @Override
    public List<Question> getQuestionListNotInQuiz(int quizId) {
        Query query = createNamedQuery("getQuestionListNotInQuiz", -1, -1, quizId);
        return query.list();
    }

    @Override
    public List<Question> getQuestionListBySurveySessionId(int surveySessionId) {
        String sql = "Select * from questions q "
                + "Inner Join survey_questions sq "
                + "On q.id = sq.question_id "
                + "Where sq.survey_session_id = :surveySessionId Order By sq.index_question";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("surveySessionId", surveySessionId);
        query.addEntity(Question.class);
        return query.list();
    }

    @Override
    public List<Question> getQuestionListByQuizId(int quizId) {
        String sql = "Select * from questions q "
                + "Inner Join quiz_questions qq "
                + "On q.id = qq.question_id "
                + "Where qq.quiz_id = :quizId Order By qq.index_question";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("quizId", quizId);
        query.addEntity(Question.class);
        return query.list();
    }

    @Override
    public List<Question> getQuestionListNotInTest(int testId) {
        Query query = createNamedQuery("getQuestionListNotInTest", -1, -1, testId);
        return query.list();
    }

    @Override
    public List<Question> getQuestionListByTestPartId(int testPartId) {
        String sql = "Select * from questions q "
                + "Inner Join test_questions tq "
                + "On q.id = tq.question_id "
                + "Where tq.test_part_id = :testPartId Order By tq.index_question";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("testPartId", testPartId);
        query.addEntity(Question.class);
        return query.list();
    }

}
