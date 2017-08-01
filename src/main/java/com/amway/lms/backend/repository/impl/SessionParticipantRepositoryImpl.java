package com.amway.lms.backend.repository.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.common.AmwayEnum;
import com.amway.lms.backend.entity.SessionParticipant;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.SessionParticipantRepository;

/**
 * @author: Hung (Charles) V. PHAM
 * */

@Repository
// @EnableTransactionManagement
// @Transactional
public class SessionParticipantRepositoryImpl extends
        AbstractRepository<Serializable, SessionParticipant> implements
        SessionParticipantRepository {
    private static final Logger logger = LoggerFactory
            .getLogger(SessionParticipantRepositoryImpl.class);

    @Override
    public void addSessionParticipantFromPlan(int sessionId, int userId) {
        Timestamp today = new Timestamp(System.currentTimeMillis());
        SessionParticipant sessionParticipant = new SessionParticipant();
        sessionParticipant.setSessionId(sessionId);
        sessionParticipant.setUserId(userId);
        sessionParticipant
                .setCompletionStatus(AmwayEnum.CompletionStatus.WAITING_FOR_APPROVAL
                        .getValue());
        sessionParticipant.setConfirmationDate(today);
        sessionParticipant.setIsSendingList(1);
        int confirmationStatus = 0;
        sessionParticipant.setConfirmationStatus(confirmationStatus);
        persist(sessionParticipant);// TODO Auto-generated method stub
    }

    @Override
    public void addSessionParticipantFromSession(int sessionId, int userId) {
        Timestamp today = new Timestamp(System.currentTimeMillis());
        SessionParticipant sessionParticipant = new SessionParticipant();
        sessionParticipant.setSessionId(sessionId);
        sessionParticipant.setUserId(userId);
        sessionParticipant
                .setCompletionStatus(AmwayEnum.CompletionStatus.WAITING_FOR_APPROVAL
                        .getValue());
        sessionParticipant.setConfirmationDate(today);
        int confirmationStatus = 0;
        sessionParticipant.setConfirmationStatus(confirmationStatus);
        persist(sessionParticipant);// TODO Auto-generated method stub
    }

    @Override
    public void addSessionParticipantSentMail(int sessionId, int userId) {
        Timestamp today = new Timestamp(System.currentTimeMillis());
        SessionParticipant sessionParticipant = new SessionParticipant();
        sessionParticipant.setSessionId(sessionId);
        sessionParticipant.setUserId(userId);
        sessionParticipant
                .setCompletionStatus(AmwayEnum.CompletionStatus.WAITING_FOR_APPROVAL
                        .getValue());
        sessionParticipant.setConfirmationDate(today);
        int confirmationStatus = 1;
        sessionParticipant.setConfirmationStatus(confirmationStatus);
        persist(sessionParticipant);// TODO Auto-generated method stub
    }

    @Override
    public void delSessionParticipant(int sessionId, int userId) {
        Query query = createNamedQuery("delSessionParticipant", -1, -1,
                sessionId, userId);
        query.executeUpdate();
    }

    @Override
    public SessionParticipant getSessionParticipant(int sessionId, int userId) {
        Query query = createNamedQuery("getSessionParticipant", -1, -1,
                sessionId, userId);
        return (SessionParticipant) query.uniqueResult();
    }

    @Override
    public List<User> getUsersBySessionId(int sessionId,
            Integer confirmationStatus, Integer departmentId) {
        logger.info("SessionParticipantRepositoryImpl******getUsersBySessionId");
        Query query;
        if (confirmationStatus == null && departmentId == null) {
            logger.info("SessionParticipantRepositoryImpl******getUsersBySessionId 1.");
            String queryString = "from User where id in (select userId from SessionParticipant where sessionId=:sessionId)";
            query = getSession().createQuery(queryString);
        } else if (confirmationStatus == null && departmentId != null) {
            logger.info("SessionParticipantRepositoryImpl******getUsersBySessionId 2. departmentId = "
                    + departmentId);
            String queryString = "from User where departmentId= :departmentId and id in (select userId from SessionParticipant where sessionId= :sessionId)";
            query = getSession().createQuery(queryString);
            query.setParameter("departmentId", departmentId);
        } else if (confirmationStatus != null && departmentId == null) {
            logger.info("SessionParticipantRepositoryImpl******getUsersBySessionId 3.");
            String queryString = "from User "
                    + "where id in (select userId from SessionParticipant where sessionId=:sessionId and confirmationStatus=:confirmationStatus and isSendingList=1)";
            query = getSession().createQuery(queryString);
            query.setParameter("confirmationStatus", confirmationStatus);
        } else {
            logger.info("SessionParticipantRepositoryImpl******getUsersBySessionId 4.");
            String queryString = "from User where departmentId=:departmentId and id in (select userId from SessionParticipant where sessionId = :sessionId and confirmationStatus = :confirmationStatus)";
            query = getSession().createQuery(queryString);
            query.setParameter("confirmationStatus", confirmationStatus);
            query.setParameter("departmentId", departmentId);
        }
        query.setParameter("sessionId", sessionId);
        return (List<User>) query.list();
    }

    @Override
    public List<User> getUsersAcceptedBySessionId(int sessionId)
            throws DataAccessException {
        Query query = createNamedQuery("getUsersAccepted", -1, -1, sessionId);
        return query.list();
    }
    
    @Override
    public boolean isExistUserInSessionParticipant(int sessionId, int userId) {
        Query query = createNamedQuery("isExistUserInSessionParticipant", -1,
                -1, sessionId, userId, sessionId, userId);
        User isExist = (User) query.uniqueResult();
        if (isExist != null) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isExistTrainerInSessionParticipant(int sessionId, int userId){
        Query query = createNamedQuery("isExistTrainerInSessionParticipant", -1,
                -1, sessionId, userId, sessionId, userId);
        User isExist = (User) query.uniqueResult();
        if(isExist != null){
            return true;
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<SessionParticipant> getSessionParticipantByUserId(int userId)
            throws DataAccessException {
        Query query = createNamedQuery("getSessionParticipantByUserId", -1, -1,
                userId);
        return query.list();
    }

    @Override
    public SessionParticipant getSessionParticipantById(int id) {
        Query query = createNamedQuery("getSessionParticipantById", -1, -1, id);
        return (SessionParticipant) query.uniqueResult();
    }

    @Override
    public void updateSessionParticipant(SessionParticipant sessionParticipant) {
        update(sessionParticipant);
    }

    @Override
    public void saveSessionParticipant(SessionParticipant sessionParticipant) {
        persist(sessionParticipant);
    }

    @Override
    public List<SessionParticipant> getSessionParticipantBySessionId(
            int sessionId) throws DataAccessException {
        Query query = createNamedQuery("getSessionParticipantBySessionId", -1,
                -1, sessionId);
        return query.list();
    }

    @Override
    public List<SessionParticipant> getSessionParticipantByStatus() {
        Query query = createNamedQuery("getSessionParticipantByStatus", -1, -1,
                AmwayEnum.CompletionStatus.WAITING_FOR_APPROVAL.getValue());
        return query.list();
    }

    @Override
    public List<SessionParticipant> getListByUserTrainingPlanYear(int userId,
            Integer trainingPlanId, Integer year) {
        String queryString = "SELECT * FROM session_participants sp WHERE sp.user_id=:userId AND sp.session_id IN "
                + "(SELECT s.id FROM courses_of_training_plan cp "
                + " INNER JOIN courses c ON c.id = cp.course_id "
                + " INNER JOIN sessions s ON s.course_id = c.id "
                + " WHERE YEAR(s.start_time)=:year AND cp.training_plan_id=:trainingPlanId)";
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.setParameter("userId", userId);
        query.setParameter("trainingPlanId", trainingPlanId);
        query.setParameter("year", year);
        query.addEntity(SessionParticipant.class);
        return (List<SessionParticipant>) query.list();
    }

    @Override
    public List<SessionParticipant> getListByUserTrainingPlan(int userId,
            Integer trainingPlanId) {
        String queryString = "SELECT * FROM session_participants sp WHERE sp.user_id=:userId AND sp.session_id IN "
                + "(SELECT s.id FROM courses_of_training_plan cp "
                + " INNER JOIN courses c ON c.id = cp.course_id "
                + " INNER JOIN sessions s ON s.course_id = c.id "
                + " WHERE cp.training_plan_id=:trainingPlanId)";
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.setParameter("userId", userId);
        query.setParameter("trainingPlanId", trainingPlanId);
        query.addEntity(SessionParticipant.class);
        return (List<SessionParticipant>) query.list();
    }

    @Override
    public List<SessionParticipant> getListByUserYear(int userId, Integer year) {
        String queryString = "SELECT * FROM session_participants sp WHERE sp.user_id=:userId AND sp.session_id IN "
                + " (SELECT s.id FROM sessions s WHERE YEAR(s.start_time)=:year) ";
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.setParameter("userId", userId);
        query.setParameter("year", year);
        query.addEntity(SessionParticipant.class);
        return (List<SessionParticipant>) query.list();
    }

    @Override
    public List<SessionParticipant> getListByUserAndCompletionStatus(
            int userId, int status) {
        Query query = createNamedQuery("getListByUserAndCompletionStatus", -1,
                -1, userId, status);
        return query.list();
    }

    @Override
    public List<SessionParticipant> getListByUserPlanCompletionStatus(
            int userId, int trainingPlanId, int status) {
        String queryString = "SELECT * FROM session_participants sp WHERE sp.user_id=:userId AND sp.completion_status=:status AND sp.session_id IN "
                + "(SELECT s.id FROM courses_of_training_plan cp "
                + " INNER JOIN courses c ON c.id = cp.course_id "
                + " INNER JOIN sessions s ON s.course_id = c.id "
                + " WHERE cp.training_plan_id=:trainingPlanId)";
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.setParameter("userId", userId);
        query.setParameter("trainingPlanId", trainingPlanId);
        query.setParameter("status", status);
        query.addEntity(SessionParticipant.class);
        return (List<SessionParticipant>) query.list();
    }

    @Override
    public List<SessionParticipant> getApprovedListByUserTrainingPlanYear(
            int userId, Integer trainingPlanId, Integer year) {
        String queryString = "SELECT * FROM session_participants sp WHERE sp.user_id=:userId AND sp.manager_action_status=:managerActionStatus AND sp.session_id IN "
                + "(SELECT s.id FROM courses_of_training_plan cp "
                + " INNER JOIN courses c ON c.id = cp.course_id "
                + " INNER JOIN sessions s ON s.course_id = c.id "
                + " WHERE YEAR(s.start_time)=:year AND cp.training_plan_id=:trainingPlanId)";
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.setParameter("userId", userId);
        query.setParameter("managerActionStatus",
                AmwayEnum.ManagerActionStatus.ACCEPTED.getValue());
        query.setParameter("trainingPlanId", trainingPlanId);
        query.setParameter("year", year);
        query.addEntity(SessionParticipant.class);
        return (List<SessionParticipant>) query.list();
    }

    @Override
    public List<SessionParticipant> getApprovedListByUserTrainingPlan(
            int userId, Integer trainingPlanId) {
        String queryString = "SELECT * FROM session_participants sp WHERE sp.user_id=:userId AND sp.manager_action_status=:managerActionStatus AND sp.session_id IN "
                + "(SELECT s.id FROM courses_of_training_plan cp "
                + " INNER JOIN courses c ON c.id = cp.course_id "
                + " INNER JOIN sessions s ON s.course_id = c.id "
                + " WHERE cp.training_plan_id=:trainingPlanId)";
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.setParameter("userId", userId);
        query.setParameter("managerActionStatus",
                AmwayEnum.ManagerActionStatus.ACCEPTED.getValue());
        query.setParameter("trainingPlanId", trainingPlanId);
        query.addEntity(SessionParticipant.class);
        return (List<SessionParticipant>) query.list();
    }

    @Override
    public List<SessionParticipant> getApprovedListByUserYear(int userId,
            Integer year) {
        String queryString = "SELECT * FROM session_participants sp WHERE sp.user_id=:userId AND sp.manager_action_status=:managerActionStatus AND sp.session_id IN "
                + " (SELECT s.id FROM sessions s WHERE YEAR(s.start_time)=:year) ";
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.setParameter("userId", userId);
        query.setParameter("managerActionStatus",
                AmwayEnum.ManagerActionStatus.ACCEPTED.getValue());
        query.setParameter("year", year);
        query.addEntity(SessionParticipant.class);
        return (List<SessionParticipant>) query.list();
    }

    @Override
    public List<SessionParticipant> getApprovedSessionParticipantByUserId(
            int userId) {
        Query query = createNamedQuery("getApprovedSessionParticipantByUserId",
                -1, -1, userId,
                AmwayEnum.ManagerActionStatus.ACCEPTED.getValue());
        return query.list();
    }

}
