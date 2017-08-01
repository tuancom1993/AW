package com.amway.lms.backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.model.Employee;

public interface ParticipantService {
    public ResponseEntity<?> getInforCourseAndSession(Integer sessionId);

    public ResponseEntity<?> getEmployee(Integer sessionId, Integer departmentId);

    public ResponseEntity<?> getSubmitedList(Integer sessionId);

    public ResponseEntity<?> newParticipant(List<Employee> employees);

}
