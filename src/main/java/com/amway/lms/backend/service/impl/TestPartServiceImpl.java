package com.amway.lms.backend.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amway.lms.backend.entity.TestPart;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.service.TestPartService;

@Service
public class TestPartServiceImpl implements TestPartService{

    @Override
    public ResponseEntity<?> addTestPart(TestPart TestPart) throws AddObjectException, Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<?> deleteTestPart(int surveyId, int TestPartId) throws DeleteObjectException, Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
