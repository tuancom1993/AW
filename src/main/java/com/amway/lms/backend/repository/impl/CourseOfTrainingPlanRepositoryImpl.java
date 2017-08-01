package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.amway.lms.backend.entity.CoursesOfTrainingPlan;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.CourseOfTrainingPlanRepository;

@Repository
@EnableTransactionManagement
@Transactional
@SuppressWarnings("unchecked")
public class CourseOfTrainingPlanRepositoryImpl extends
        AbstractRepository<Integer, CoursesOfTrainingPlan> implements
        CourseOfTrainingPlanRepository {
    private static final Logger logger = LoggerFactory
            .getLogger(CourseOfTrainingPlanRepositoryImpl.class);

    @Override
    public void addCourseOfTrainingPlan(int trainingPlanId, int courseId) {
        CoursesOfTrainingPlan courseOfTrainingPlan = new CoursesOfTrainingPlan();
        courseOfTrainingPlan.setTrainingPlanId(trainingPlanId);
        courseOfTrainingPlan.setCourseId(courseId);
        persist(courseOfTrainingPlan);
        logger.info("CourseOfTrainingPlanRepositoryImpl****addCourseOfTrainingPlan: Add CourseOfTrainingPlan Successful!!!");
    }

    @Override
    public void delCourseOfTrainingPlanByPlainingId(int trainingPlanId) {
        Query query = createNamedQuery("delCourseOfTrainingPlanByPlainingId",
                -1, -1, trainingPlanId);
        query.executeUpdate();
    }

    @Override
    public void removeCoursesOfTrainingPlan(int trainingPlanId, int courseId) {
        Query query = createNamedQuery("removeCoursesOfTrainingPlan",
                -1, -1, trainingPlanId, courseId);
        query.executeUpdate();
    }

    @Override
    public void setMandatoryForCourse(int trainingPlanId, int courseId,
            int isRequired) {
        Query query = createNamedQuery("setMandatoryForCourse",
                -1, -1, isRequired, trainingPlanId, courseId);
        query.executeUpdate();
        
    }

    @Override
    public CoursesOfTrainingPlan getCourseOfTrainingPlan(int trainingPlanId, int courseId) {
        Query query = createNamedQuery("getCourseOfTrainingPlan",
                -1, -1, trainingPlanId, courseId);
        return (CoursesOfTrainingPlan) query.uniqueResult();
    }

    @Override
    public List<CoursesOfTrainingPlan> getCourseOfTrainingPlanByCourseId(
            int courseId) {
        Query query = createNamedQuery("getCourseOfTrainingPlanByCourseId",
                -1, -1, courseId);
        return (List<CoursesOfTrainingPlan>) query.list();
    }

}
