package com.amway.lms.backend.service;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.QuizResult;

public interface QuizResultService {
    public ResponseEntity<?> quizResults(QuizResult quizResult) throws Exception;
    public ResponseEntity<?> getQuizResults(int quizId) throws Exception;
}
