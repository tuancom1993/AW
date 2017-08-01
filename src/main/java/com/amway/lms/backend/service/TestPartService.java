package com.amway.lms.backend.service;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.TestPart;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;

public interface TestPartService {
    public ResponseEntity<?> addTestPart(TestPart TestPart) throws AddObjectException, Exception;
    public ResponseEntity<?> deleteTestPart(int surveyId, int TestPartId) throws DeleteObjectException, Exception;
}
