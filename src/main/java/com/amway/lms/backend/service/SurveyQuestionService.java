/**
 * 
 */
package com.amway.lms.backend.service;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.SurveyQuestion;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;

/**
 * @author acton
 *
 */
public interface SurveyQuestionService {
    public ResponseEntity<?> addSurveyQuestionWithNewQuestion(SurveyQuestion surveyQuestion) throws AddObjectException, Exception;
    public ResponseEntity<?> addSurveyQuestionWithOldQuestion(SurveyQuestion surveyQuestion) throws AddObjectException, Exception;
    public ResponseEntity<?> deleteSurveyQuestion(int surveyId, int questionId) throws ObjectNotFoundException, DeleteObjectException, Exception;
}
