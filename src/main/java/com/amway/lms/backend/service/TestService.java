package com.amway.lms.backend.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.Test;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.TestEmailModel;
import com.amway.lms.backend.model.TestModelForCreateEdit;

public interface TestService {
    public ResponseEntity<?> startTest(User userLogin, String encodeValue) throws Exception;
    public ResponseEntity<?> getTestInfo(int userId);
    public ResponseEntity<?> createTestFull(TestModelForCreateEdit testModelForCreateEdit) throws AddObjectException, Exception;
    public ResponseEntity<?> editTestFull(TestModelForCreateEdit testModelForCreateEdit) throws EditObjectException, Exception;
    public ResponseEntity<?> getTestModelFull(int testId) throws ObjectNotFoundException, Exception;
    public ResponseEntity<?> getTestList() throws ObjectNotFoundException, Exception;
    public String genURL(int testId, int testParticipantId) throws ObjectNotFoundException, Exception;;
    public ResponseEntity<?> cloneTest(int testId) throws ObjectNotFoundException, Exception;;
    public ResponseEntity<?> deleteTest(List<Test> tests) throws DeleteObjectException, ObjectNotFoundException ,Exception;
    public File exportTestHorizontal(int testId) throws IOException, Exception;
    public ResponseEntity<?> sendEmail(TestEmailModel testEmailModel) throws Exception;
    public ResponseEntity<?> getTestEntityInfor(int testId) throws Exception;
}
