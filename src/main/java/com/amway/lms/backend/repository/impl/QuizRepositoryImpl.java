package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.Quiz;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.QuizRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class QuizRepositoryImpl extends AbstractRepository<Integer, Quiz> implements QuizRepository {

    @Override
    public List<Quiz> getQuizList() {
        Query query = createNamedQuery("getQuizList", -1, -1);
        return query.list();
    }

    @Override
    public Quiz getQuizById(int quizId) {

        return getByKey(quizId);
    }

    @Override
    public void addQuiz(Quiz quiz) {
        persist(quiz);

    }

    @Override
    public void editQuiz(Quiz quiz) {
        update(quiz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.amway.lms.backend.repository.QuizRepository#getQuizByCourseId(int)
     */
    @Override
    public Quiz getQuizByCourseId(int courseId) {
        try {
            Query query = createNamedQuery("getQuizByCourseId", -1, -1, courseId);
            List<Quiz> quizs = query.list();
            if (quizs.isEmpty())
                return null;
            else
                return quizs.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteQuiz(Quiz quiz) {
        delete(quiz);
    }

    @Override
    public List<Quiz> searchQuizByName(String searchString) {
        Query query = createNamedQuery("searchQuizByName", -1, -1, "%" + searchString + "%");
        return query.list();
    }

    @Override
    public Quiz getQuizBySessionParticipantId(int sessionParticipantId) {
        String sql = "Select * From quiz q "
                + "INNER JOIN courses c ON q.course_id = c.id "
                + "INNER JOIN sessions s ON s.course_id = c.id "
                + "INNER JOIN session_participants sp "
                + "ON sp.session_id = s.id "
                + "Where sp.id = :sessionParticipantId";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("sessionParticipantId", sessionParticipantId);
        query.addEntity(Quiz.class);
        return (Quiz) query.uniqueResult();
    }

}
