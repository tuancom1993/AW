package com.amway.lms.backend.service;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.TestQuestion;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;

public interface TestQuestionService {
    public ResponseEntity<?> addTestQuestionWithNewQuestion(TestQuestion testQuestion) throws AddObjectException, Exception;
    public ResponseEntity<?> addTestQuestionWithOldQuestion(TestQuestion testQuestion) throws AddObjectException, Exception;
    public ResponseEntity<?> deleteTestQuestion(int testId, int questionId) throws ObjectNotFoundException, DeleteObjectException, Exception;
}
