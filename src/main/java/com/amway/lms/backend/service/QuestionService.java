/**
 * 
 */
package com.amway.lms.backend.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author acton
 *
 */
public interface QuestionService {
    //public ResponseEntity<?> getQuestionListOfSurvey() throws Exception;
    public ResponseEntity<?> getQuestionListNotInSurvey(int surveyId) throws Exception;
    //public ResponseEntity<?> getQuestionListOfQuiz() throws Exception;
    public ResponseEntity<?> getQuestionListNotInQuiz(int quizId) throws Exception;
    public ResponseEntity<?> getQuestionListBySurveyId(int surveyId) throws Exception;
    public ResponseEntity<?> uploadFileForQuestion(MultipartFile file, String rootPath) throws IOException, Exception;
    public ResponseEntity<?> getQuestionListByQuizId(int quizId) throws Exception;
    public ResponseEntity<?> getQuestionListNotInTest(int testId) throws Exception;
}
