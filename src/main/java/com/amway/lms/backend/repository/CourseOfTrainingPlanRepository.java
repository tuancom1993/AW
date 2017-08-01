package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.CoursesOfTrainingPlan;


public interface CourseOfTrainingPlanRepository {
    public void addCourseOfTrainingPlan(int trainingPlanId,int courseId);
    public CoursesOfTrainingPlan getCourseOfTrainingPlan(int trainingPlanId,int courseId);
    public List<CoursesOfTrainingPlan> getCourseOfTrainingPlanByCourseId(int courseId);
    public void removeCoursesOfTrainingPlan(int trainingPlanId,int courseId);
    public void delCourseOfTrainingPlanByPlainingId(int trainingPlanId);
    public void setMandatoryForCourse(int trainingPlanId, int courseId, int isRequired);
}
