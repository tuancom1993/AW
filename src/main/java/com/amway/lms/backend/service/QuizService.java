/**
 * 
 */
package com.amway.lms.backend.service;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.Quiz;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.QuizNotAvailableException;

/**
 * @author acton
 *
 */
public interface QuizService {
    public ResponseEntity<?> getQuizList() throws Exception;
    public ResponseEntity<?> getQuizInfor(int quizId) throws ObjectNotFoundException ,Exception;
    public ResponseEntity<?> addQuiz(Quiz quiz) throws AddObjectException, Exception;
    public ResponseEntity<?> editQuiz(Quiz quiz) throws EditObjectException, Exception;
    public ResponseEntity<?> startQuiz(int sessionId, int userId) throws QuizNotAvailableException, Exception;
    public ResponseEntity<?> startQuiz(int sessionParticipantId, String cookieValue) throws QuizNotAvailableException, Exception;
    public ResponseEntity<?> deleteQuiz(int quizId) throws DeleteObjectException, Exception;
    public ResponseEntity<?> searchQuizByName(String searchString) throws Exception;
    public ResponseEntity<?> addQuizFull(Quiz quiz) throws AddObjectException, Exception;
    public ResponseEntity<?> editQuizFull(Quiz quiz) throws EditObjectException, Exception;
}
