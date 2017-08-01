package com.amway.lms.backend.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the training_plans database table.
 * 
 */
@Entity
@Table(name = "training_plans")
@NamedQueries({ 
    @NamedQuery(name = "getTrainingPlans", query = "SELECT t FROM TrainingPlan t Order By t.id Desc"),
    @NamedQuery(name = "getTrainingPlanByUserId", query = "SELECT t FROM TrainingPlan t WHERE t.id IN (SELECT pp.trainingPlanId FROM PreparatoryParticipant pp WHERE pp.userId=?) Order By t.id Desc"),
    @NamedQuery(name = "searchTrainingPlans", query = "SELECT t FROM TrainingPlan t WHERE t.planName LIKE ? Order By t.id Desc")
    
})

public class TrainingPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name = "number_of_courses")
    private int numberOfCourses;

    @Column(name = "plan_name")
    private String planName;

    @Column(name = "trainer_id")
    private int trainerId;

    @Column(name = "is_measure_by_ajpv")
    private int isMeasureByAJPV;
    

    public TrainingPlan() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfCourses() {
        return this.numberOfCourses;
    }

    public void setNumberOfCourses(int numberOfCourses) {
        this.numberOfCourses = numberOfCourses;
    }

    public String getPlanName() {
        return this.planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getTrainerId() {
        return this.trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public int getIsMeasureByAJPV() {
        return isMeasureByAJPV;
    }

    public void setIsMeasureByAJPV(int isMeasureByAJPV) {
        this.isMeasureByAJPV = isMeasureByAJPV;
    }

    
}