/**
 * 
 */
package com.amway.lms.backend.service;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.Question;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;

/**
 * @author acton
 *
 */
public interface QuizQuestionService {
    public ResponseEntity<?> addQuizQuestionWithNewQuestion(int quizId, Question question) throws AddObjectException, Exception;
    public ResponseEntity<?> addQuizQuestionWithOldQuestion(int quizId, Question question) throws AddObjectException, Exception;
    public ResponseEntity<?> deleteQuizQuestion(int quizId, int questionId) throws ObjectNotFoundException, DeleteObjectException, Exception;
}
