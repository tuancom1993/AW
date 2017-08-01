package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.Quiz;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface QuizRepository {
    public List<Quiz> getQuizList();
    public Quiz getQuizById(int quizId);
    public void addQuiz(Quiz quiz);
    public void editQuiz(Quiz quiz);
    public Quiz getQuizByCourseId(int courseId);
    public void deleteQuiz(Quiz quiz);
    public List<Quiz> searchQuizByName(String searchString);
    public Quiz getQuizBySessionParticipantId(int sessionParticipantId);
}
