package com.amway.lms.backend.service;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.model.TestResultModel;

public interface TestResultService {
    public ResponseEntity<?> testResult(TestResultModel testResultModel);
    public ResponseEntity<?> viewTestResult(int testId, String user);
}
