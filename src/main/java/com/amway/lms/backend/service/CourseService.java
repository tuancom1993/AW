package com.amway.lms.backend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.model.EmailInfor;

public interface CourseService {    
    public ResponseEntity<?> updateCoursesStatus(List<Course> courses, int isActive);
    public ResponseEntity<?> updateCoursesAJPV(Course course);
    public ResponseEntity<?> getCoursesByNameOrCode(String searchString, int isActive);
    public ResponseEntity<?> getCoursesByUser(String searchString);
    public ResponseEntity<?> getCoursesList(int isActive, Integer trainingPlanId);
    public ResponseEntity<?> getCoursesToAddTrainingPlan(int trainingPlan);
    public ResponseEntity<?> getCoursesOfTrainingPlan(int trainingPlanId);
    public ResponseEntity<?> getCoursesListByStatus(int firstItem, int maxItem, int isActive);
    public ResponseEntity<?> getCoursesInProgressList(int firstItem, int maxItem);
    public ResponseEntity<?> addCourse(Course course);
    public ResponseEntity<?> getCoursesById(int courseId);
    public ResponseEntity<?> getSessionsByCourseId(int courseId);
    public ResponseEntity<?> getCoursesApprovedList(int firstItem, int maxItem);
    public ResponseEntity<?> sendingEmailToPrticipants(int courseId,EmailInfor emailInfor) throws IOException;
    public ResponseEntity<?> getCoursesListNotHaveQuiz() throws Exception;
    public List<Course> getCoursesApprovedListExport(int firstItem, int maxItem);
    public ResponseEntity<?> getCoursesStartByToday(String courseName);
    public ResponseEntity<?> getCoursesListForPostTrainingSurvey() throws Exception;
}
