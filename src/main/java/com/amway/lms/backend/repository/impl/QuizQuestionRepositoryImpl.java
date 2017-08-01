/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.QuizQuestion;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.QuizQuestionRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class QuizQuestionRepositoryImpl extends AbstractRepository<Integer, QuizQuestion> implements QuizQuestionRepository{

    @Override
    public List<QuizQuestion> getQuizQuestionListByQuizId(int quizId) {
        Query query = createNamedQuery("getQuizQuestionListByQuizId", -1, -1, quizId);
        return query.list();
    }

    @Override
    public void addQuizQuestion(QuizQuestion quizQuestion) {
        persist(quizQuestion);
    }

    @Override
    public QuizQuestion getQuizQuestionByQuizIdAndQuestionId(int quizId, int questionId) {
        try {
            Query query = createNamedQuery("getQuizQuestionByQuizIdAndQuestionId", -1, -1, quizId, questionId);
            return (QuizQuestion) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteQuizQuestion(QuizQuestion quizQuestion) {
        delete(quizQuestion);
    }

    @Override
    public void deleteQuizQuestionByQuizId(int quizId) {
        Query query = createNamedQuery("deleteQuizQuestionByQuizId", -1, -1, quizId);
        query.executeUpdate();
    }

}
