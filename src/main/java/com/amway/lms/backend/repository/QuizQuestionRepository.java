/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.QuizQuestion;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface QuizQuestionRepository {
    public List<QuizQuestion> getQuizQuestionListByQuizId(int quizId);
    public void addQuizQuestion(QuizQuestion quizQuestion);
    public QuizQuestion getQuizQuestionByQuizIdAndQuestionId(int quizId, int questionId);
    public void deleteQuizQuestion(QuizQuestion quizQuestion);
    public void deleteQuizQuestionByQuizId(int quizId);
}
