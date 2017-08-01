package com.amway.lms.backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.TrainingPlan;
import com.amway.lms.backend.entity.User;


public interface TrainingPlanService {
    public ResponseEntity<?> getCourseListByPlanAndStatus(int trainingPlanId, int courseStatus);
    public ResponseEntity<?> getCoursesStatusList(int trainingPlanId, int sessionStatus);
    public ResponseEntity<?> getCourseListByPlan(int trainingPlanId);
    public ResponseEntity<?> addTrainingPlan(TrainingPlan trainingPlan);
    public ResponseEntity<?> updateTrainingPlan(TrainingPlan trainingPlan);
    public ResponseEntity<?> delTrainingPlans(List<TrainingPlan> trainingPlans);
    public ResponseEntity<?> getTrainingPlans();
    public ResponseEntity<?> getTrainingPlanById(int trainingPlanId);
    public ResponseEntity<?> getUsersOfTrainingPlan(int trainingPlanId, int existed, Integer departmentId);
    public ResponseEntity<?> searchTrainingPlans(String planName);
    public ResponseEntity<?> addUsersToTrainingPlan(int trainingPlanId, List<User> users);
    public ResponseEntity<?> delUsersFromTrainingPlan(int trainingPlanId, List<User> users);
}
