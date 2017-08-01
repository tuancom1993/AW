package com.amway.lms.backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.CourseParticipant;
import com.amway.lms.backend.entity.User;

public interface CourseParticipantService{
    public ResponseEntity<?> getCourseParticipantsByCourseId(int courseId);
    public ResponseEntity<?> addCourseParticipantByIds(int courseId, List<Integer> userIdList);
    public ResponseEntity<?> addCoursePaticipant(int courseId, List<User> users);
    public ResponseEntity<?> delCoursePaticipant(int courseId, List<User> users);
    public ResponseEntity<?> getUsersByCourseId(int courseId, Integer confirmationStatus, Integer departmentId);
    public ResponseEntity<?> updateCheckIn(CourseParticipant courseParticipant);
    public ResponseEntity<?> updateCheckOut(CourseParticipant courseParticipant);
    public ResponseEntity<?> addCourseParticipant(CourseParticipant courseParticipant);
}
