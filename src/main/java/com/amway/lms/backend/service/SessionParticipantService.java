package com.amway.lms.backend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.amway.lms.backend.entity.SessionParticipant;
import com.amway.lms.backend.entity.User;

public interface SessionParticipantService {
    public ResponseEntity<?> addSessionPaticipant(int sessionId,
            List<User> users);
    
    public ResponseEntity<?> importParticipant(MultipartFile surveyFile, int sessionId) throws IOException, Exception;
    
    public ResponseEntity<?> delSessionPaticipant(int sessionId,
            List<User> users);

    public ResponseEntity<?> getUsersBySessionId(int courseId,
            Integer confirmationStatus, Integer departmentId);
    public ResponseEntity<?> getUsersAcceptedBySessionId(int sessionId);
    public ResponseEntity<?> getUsersCheckInOutBySessionId(int sessionId, String name, int completionStatus);
    public ResponseEntity<?> getUsersCheckinCheckout(int sessionId, String name);
    
    public ResponseEntity<?> userCheckIn(SessionParticipant sessionParticipant);
    public ResponseEntity<?> userCheckOut(SessionParticipant sessionParticipant);
    public void setAcceptForTraineeCronJob();
    public ResponseEntity<?> getStatusList(int userId, int trainingPlanId, int year);
}
