package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.Course;

public interface CourseRepository{
    public void updateCoursesStatus(Course course);
    public List<Course> getCoursesByNameOrCode(String searchString, int isActive); 
    public List<Course> getCoursesByUser(String searchString);
    public List<Course> getCoursesList(int isActive, Integer trainingPlanId);    
    public List<Course> getCoursesToAddTrainingPlan(int trainingPlanId);
    public List<Course> getCoursesStartByToday(String courseName);
    public List<Course> getCoursesOfTrainingPlan(int trainingPlanId);
    public List<Course> getCoursesListByStatus(int firstItem, int maxItem, int isActive);
    public void addCourse(Course course);
    public void editCourse(Course course);
    public Course getCourseById(int id);
    public List<Course> getCoursesApprovedList(int firstItem, int maxItem);
    public List<Course> getCoursesImprogressList(int firstItem, int maxItem);
    
    
	public Object[] getCourseInformation(int courseId);

	public List<Course> searchCourseByStaffCode(int userId);

	public List<Course> getSuggestCourse(int userId);
	
	public void updateCourse(Course course);
    public List<Course> getCourseListNotHaveQuiz();
    public List<Course> getCourseListForPostTrainingSurvey();
}
