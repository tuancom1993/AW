/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.Question;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface QuestionRepository {
    public Question getQuestionById(int questionId);
    public List<Question> getQuestionListBySurveyIdOrderBySurveySessionId(int surveyId);
    public List<Question> getQuestionListBySurveyIdAndQuestionIdLager6(int surveyId);
    public void addQuestion(Question question);
    public List<Question> getQuestionListNotInSurvey(int surveyId);
    public List<Question> getQuestionListNotInQuiz(int quizId);
    public List<Question> getQuestionListBySurveyId(int surveyId);
    public List<Question> getQuestionListBySurveySessionId(int surveySessionId);
    public List<Question> getQuestionListByQuizId(int quizId);
    public List<Question> getQuestionListNotInTest(int testId);
    public List<Question> getQuestionListByTestPartId(int testPartId);
}
