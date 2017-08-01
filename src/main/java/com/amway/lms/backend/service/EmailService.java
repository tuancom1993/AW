package com.amway.lms.backend.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.Test;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.EmailInfor;
import com.amway.lms.backend.model.Employee;

public interface EmailService {
    public ResponseEntity<?> getEmailTemplate(String emailTemplate) throws IOException;

    public ResponseEntity<?> sendEmailForSurvey(EmailInfor emailInfor) throws IOException;

    public void sendEmail(EmailInfor emailInfor) throws IOException, MessagingException;

    public ResponseEntity<?> acceptDenyByEmail(List<Employee> employees, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException, Exception;

    public ResponseEntity<?> accepetCourseByEmail(String encrtyptStr) throws Exception;

    public ResponseEntity<?> denyCourseByEmail(String encrtyptStr) throws Exception;

    public void sendEmailToTrainee(Employee employee, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException;

    public void sendEmailToTrainer(Employee employee, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException;

    public void sendEmailCoordinator(Employee employee, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException;

    public void sendEmailChangeInfoSession(Employee employee, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException;
    
    public void sendEmailChangeInfoSession(String[] emails, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException;

    public void sendEmailToTraineeForQuiz(Employee employee, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException;

    public void sendEmailToTrainerForPostSurvey(Employee trainer, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException;

    public void sendEmailToTraineeForPostSurvey(Employee trainee, Integer sessionId)
            throws MessagingException, IOException, ObjectNotFoundException;

    public ResponseEntity<?> traineeAcceptCourse(String encrtyptStr) throws Exception;

    public ResponseEntity<?> traineeDenyCourse(String encrtyptStr) throws Exception;

    public void sendEmailForTest(String[] to, Test test, String urlEncode) throws Exception;

}
