package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.entity.TrainingPlan;

public interface TrainingPlanRepository {
    public List<Course> getCourseListByPlanAndStatus(int trainingPlanId, int courseStatus);
    public List<Course> getCourseListByPlan(int trainingPlanId);
    public void addTrainingPlan(TrainingPlan trainingPlan);
    public void updateTrainingPlan(TrainingPlan trainingPlan);
    public List<TrainingPlan> getTrainingPlans();
    public TrainingPlan getTrainingPlanById(int trainingPlanId);
    public List<TrainingPlan> searchTrainingPlans(String planName);
    public List<TrainingPlan> getTrainingPlanByUserId(int userId);
    public void delTrainingPlan(TrainingPlan trainingPlan);
}
