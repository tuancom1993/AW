package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.amway.lms.backend.entity.CourseParticipant;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.CourseParticipantRepository;

@Repository
@EnableTransactionManagement
@Transactional
@SuppressWarnings("unchecked")
public class CourseParticipantRepositoryImpl extends
        AbstractRepository<Integer, CourseParticipant> implements
        CourseParticipantRepository {
    private static final Logger logger = LoggerFactory
            .getLogger(CourseParticipantRepositoryImpl.class);


    @Override
    public void addCourseParticipant(CourseParticipant courseParticipant) {
        persist(courseParticipant);
        logger.info("CourseParticipant saved successfully, CourseParticipant Details="
                + courseParticipant);
    }

    @Override
    public void addCourseParticipant(int courseId, int userId) {
        CourseParticipant courseParticipant = new CourseParticipant();
        courseParticipant.setCourseId(courseId);
        courseParticipant.setUserId(userId);
        int confirmationStatus = 0;
        courseParticipant.setConfirmationStatus(confirmationStatus);
        persist(courseParticipant);
    }

    @Override
    public void delCourseParticipant(int courseId, int userId) {
        Query query = createNamedQuery("delCourseParticipant", -1, -1,
                courseId, userId);
        query.executeUpdate();

    }

    @Override
    public void updateCheckIn(CourseParticipant courseParticipant) {
        Query query = createNamedQuery("updateCheckIn", -1, -1,
                courseParticipant.getCheckinAt(), courseParticipant.getCourseId(),
                courseParticipant.getUserId());
        query.executeUpdate();
    }

    @Override
    public void updateCheckOut(CourseParticipant courseParticipant) {
        Query query = createNamedQuery("updateCheckOut", -1, -1,
                courseParticipant.getCheckoutAt(), courseParticipant.getCourseId(),
                courseParticipant.getUserId());
        query.executeUpdate();
    }

    @Override
    public CourseParticipant getCourseParticipant(int courseId, int userId) {
        Query query = createNamedQuery("getCourseParticipant", -1, -1,
                courseId, userId);
        return (CourseParticipant) query.uniqueResult();
    }

    @Override
    public List<CourseParticipant> getListCourseParticipantByUserId(int userId)
            throws DataAccessException {
        Query query = createNamedQuery("getListCourseParticipantByUserId", -1,
                -1, userId);
        return query.list();
    }

    @Override
    public CourseParticipant getCourseParticipantById(int id)
            throws DataAccessException {
        Query query = createNamedQuery("getCourseParticipantById", -1, -1, id);
        return (CourseParticipant) query.uniqueResult();
    }

    @Override
    public void updateCourseParticipant(CourseParticipant courseParticipant) {
        update(courseParticipant);
    }

    @Override
    public CourseParticipant getCourseParticipantCourseIdandUserId(
            int courseId, int userId) {
        Query query = createNamedQuery("getCourseParticipantCourseIdandUserId",
                -1, -1, courseId, userId);
        return (CourseParticipant) query.uniqueResult();
    }
    
    
    @Override
    public List<User> getUsersByCourseId(int courseId, Integer confirmationStatus, Integer departmentId){
        logger.info("CourserParticipantRepositoryImpl******getUsersByCourseId");
        Query query;
        if (confirmationStatus == null && departmentId == null){
            logger.info("CourserParticipantRepositoryImpl******getUsersByCourseId 1.");
            String queryString = "from User where id in (select userId from CourseParticipant where courseId=:courseId)";
            query = getSession().createQuery(queryString);
        } else if (confirmationStatus == null && departmentId != null){
            logger.info("CourserParticipantRepositoryImpl******getUsersByCourseId 2. departmentId = " + departmentId);
            String queryString = "from User where departmentId= :departmentId and id in (select userId from CourseParticipant where courseId= :courseId)";
            query = getSession().createQuery(queryString);
            query.setParameter("departmentId", departmentId);
        } else if (confirmationStatus !=null && departmentId == null){
            logger.info("CourserParticipantRepositoryImpl******getUsersByCourseId 3.");
            String queryString = "from User where id in (select userId from CourseParticipant where courseId=:courseId and confirmationStatus=:confirmationStatus)";
            query = getSession().createQuery(queryString);
            query.setParameter("confirmationStatus", confirmationStatus);
        } else {
            logger.info("CourserParticipantRepositoryImpl******getUsersByCourseId 4.");
            String queryString = "from User where departmentId=:departmentId and id in (select userId from CourseParticipant where courseId = :courseId and confirmationStatus = :confirmationStatus)";
            query = getSession().createQuery(queryString);
            query.setParameter("confirmationStatus", confirmationStatus);
            query.setParameter("departmentId", departmentId);
        }  
        query.setParameter("courseId", courseId);
        return (List<User>) query.list();
    }

    @Override
    public List<CourseParticipant> getCourseParticipantsByCourseId(int courseId) {
        Query query = createNamedQuery("getCourseParticipantsByCourseId", -1,
                -1, courseId);
        return query.list();
    }

}
