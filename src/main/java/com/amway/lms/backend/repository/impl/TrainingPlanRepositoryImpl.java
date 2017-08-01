package com.amway.lms.backend.repository.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.entity.TrainingPlan;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.TrainingPlanRepository;

@Repository
@SuppressWarnings("unchecked")
public class TrainingPlanRepositoryImpl extends
        AbstractRepository<Serializable, TrainingPlan> implements
        TrainingPlanRepository {
    private static final Logger logger = LoggerFactory
            .getLogger(TrainingPlanRepositoryImpl.class);

    @Override
    public List<Course> getCourseListByPlanAndStatus(int trainingPlanId,
            int courseStatus) {
        String queryString = "SELECT * FROM courses c "
                + "WHERE c.status=:courseStatus " + "AND c.id in "
                + "(SELECT course_id FROM courses_of_training_plan "
                + "WHERE training_plan_id=:trainingPlanId) ";
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.setParameter("courseStatus", courseStatus);
        query.setParameter("trainingPlanId", trainingPlanId);
        query.addEntity(Course.class);

        return (List<Course>) query.list();
    }

    @Override
    public List<Course> getCourseListByPlan(int trainingPlanId) {
        String queryString = "";
        if (trainingPlanId == 0) {
            queryString = "SELECT DISTINCT c.* FROM courses c INNER JOIN courses_of_training_plan cp WHERE c.id = cp.course_id";
        } else {
            queryString = "SELECT * FROM courses c WHERE c.id in "
                    + "(SELECT course_id FROM courses_of_training_plan "
                    + "WHERE training_plan_id=:trainingPlanId) ";
        }
        SQLQuery query = getSession().createSQLQuery(queryString);
        if (trainingPlanId != 0) {
            query.setParameter("trainingPlanId", trainingPlanId);
        }
        query.addEntity(Course.class);

        return (List<Course>) query.list();
    }

    @Override
    public void addTrainingPlan(TrainingPlan trainingPlan) {
        persist(trainingPlan);
        logger.info("TrainingPlan saved successfully, TrainingPlan Details=" + trainingPlan);
    }

    @Override
    public List<TrainingPlan> getTrainingPlans() {
        Query query = createNamedQuery("getTrainingPlans", -1, -1);
        return (List<TrainingPlan>) query.list();
    }

    @Override
    public List<TrainingPlan> searchTrainingPlans(String planName) {
        planName = "%" + planName + "%";
        Query query = createNamedQuery("searchTrainingPlans", -1, -1, planName);
        return (List<TrainingPlan>) query.list();
    }

    @Override
    public void updateTrainingPlan(TrainingPlan trainingPlan) {
        update(trainingPlan);
        logger.info("TrainingPlan saved successfully, TrainingPlan Details=" + trainingPlan);
    }

    @Override
    public TrainingPlan getTrainingPlanById(int trainingPlanId) {
        return getByKey(trainingPlanId);
    }
    
    @Override
    public void delTrainingPlan(TrainingPlan trainingPlan) {
        delete(trainingPlan);
        logger.info("TrainingPlan deleted successfully, TrainingPlan Details=" + trainingPlan);
    }

    @Override
    public List<TrainingPlan> getTrainingPlanByUserId(int userId) {
        Query query = createNamedQuery("getTrainingPlanByUserId", -1, -1, userId);
        return (List<TrainingPlan>) query.list();
    }

}
