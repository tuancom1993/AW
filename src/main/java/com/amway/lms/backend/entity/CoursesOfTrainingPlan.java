package com.amway.lms.backend.entity;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the courses_of_training_plan database table.
 * 
 */
@Entity
@Table(name = "courses_of_training_plan")
@NamedQueries({
        @NamedQuery(name = "getCourseOfTrainingPlan", query = "SELECT c FROM CoursesOfTrainingPlan c WHERE c.trainingPlanId = ? AND c.courseId = ?"),
        @NamedQuery(name = "getCourseOfTrainingPlanByCourseId", query = "SELECT c FROM CoursesOfTrainingPlan c WHERE c.courseId = ?"),
        @NamedQuery(name = "delCourseOfTrainingPlanByPlainingId", query = "DELETE CoursesOfTrainingPlan c WHERE c.trainingPlanId = ?"),        
        @NamedQuery(name = "removeCoursesOfTrainingPlan", query = "DELETE CoursesOfTrainingPlan c WHERE c.trainingPlanId = ? AND c.courseId = ?"),
        @NamedQuery(name = "setMandatoryForCourse", query = "UPDATE CoursesOfTrainingPlan c SET c.isCourseRequired = ? WHERE c.trainingPlanId = ? AND c.courseId = ?")})
public class CoursesOfTrainingPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "is_course_required")
    private int isCourseRequired;

    @Column(name = "training_plan_id")
    private int trainingPlanId;

    public CoursesOfTrainingPlan() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getIsCourseRequired() {
        return this.isCourseRequired;
    }

    public void setIsCourseRequired(int isCourseRequired) {
        this.isCourseRequired = isCourseRequired;
    }

    public int getTrainingPlanId() {
        return this.trainingPlanId;
    }

    public void setTrainingPlanId(int trainingPlanId) {
        this.trainingPlanId = trainingPlanId;
    }

}