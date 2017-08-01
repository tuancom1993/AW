package com.amway.lms.backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.Course;

public interface CourseOfTrainingPlanService {
    public ResponseEntity<?> addCourseOfTrainingPlan(int trainingPlanId, List<Course> courses);
    public ResponseEntity<?> removeCoursesOfTrainingPlan(int trainingPlanId, List<Course> courses);
    public ResponseEntity<?> setMandatoryForCourses(int trainingPlanId, List<Course> courses, int isRequired);
}
