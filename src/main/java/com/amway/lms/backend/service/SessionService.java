package com.amway.lms.backend.service;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.exception.ObjectNotFoundException;

public interface SessionService {
    public ResponseEntity<?> getSessionDetail(int sessionId);

    public ResponseEntity<?> addNewSession(Session session);

    public ResponseEntity<?> updateSession(Session session);

    public ResponseEntity<?> deleteSession(int sessionId);

    public void sendEmailToTrainerForPostSurveyCronJob()
            throws MessagingException, IOException, ObjectNotFoundException;

}
