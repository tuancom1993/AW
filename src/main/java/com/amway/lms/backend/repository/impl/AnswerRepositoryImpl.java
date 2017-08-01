/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.Answer;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.AnswerRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class AnswerRepositoryImpl extends AbstractRepository<Integer, Answer> implements AnswerRepository{
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @Override
    public List<Answer> getAnswerListByQuestionId(int questionId) {
//        Query query = createNamedQuery("getAnswerListByQuestionId", -1, -1, questionId);
//        return query.list();
        Session session = sessionFactory.getCurrentSession();
        session.setFlushMode(FlushMode.MANUAL);
        org.hibernate.Query query = session.getNamedQuery("getAnswerListByQuestionId");
        query.setParameter(0, questionId);
        return query.list();
    }

    @Override
    public void addAnswer(Answer answer) {
        persist(answer);
    }

    @Override
    public List<Answer> getAnswerListByQuestionIdAndIsCorrect(int questionId) {
        Session session = sessionFactory.getCurrentSession();
        //session.setFlushMode(FlushMode.MANUAL);
        org.hibernate.Query query = session.getNamedQuery("getAnswerListByQuestionIdAndIsCorrect");
        query.setParameter(0, questionId);
        List<Answer> answers = query.list();
        return answers;
    }

    @Override
    public Answer getAnswerById(int answerId) {
        return getByKey(answerId);
    }

}
