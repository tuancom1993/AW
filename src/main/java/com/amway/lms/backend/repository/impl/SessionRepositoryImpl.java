package com.amway.lms.backend.repository.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.SessionRepository;

@Repository
@SuppressWarnings("unchecked")
public class SessionRepositoryImpl extends
        AbstractRepository<Serializable, Session> implements SessionRepository {

    private static final Logger logger = LoggerFactory
            .getLogger(SessionRepositoryImpl.class);

    @Override
    public List<Session> getSessionsByCourseId(int courseId) {
        Query query = createNamedQuery("getSessionsByCourseId", -1, -1,
                courseId);
        return (List<Session>) query.list();
    }

    @Override
    public List<Session> getSessionsByCourseIdAndStatus(int courseId,
            int sessionStatus) {
        logger.info("SessionRepositoryImpl******getSessionsByCourseIdAndStatus");
        Query query;
        
        switch (sessionStatus) {
            case 1:
                logger.info("Get sessions have not started");
                query = createNamedQuery("getSessionsNotStarted", -1, -1, courseId);
                break;
            case 2:
                logger.info("Get sessions inprogress");
                query = createNamedQuery("getSessionsInProgress", -1, -1, courseId);
                break;
            case 3:
                logger.info("Get sessions completed");
                query = createNamedQuery("getSessionsCompleted", -1, -1, courseId);
                break;
            case 4:
                logger.info("Get sessions inactive");                
                query = createNamedQuery("getSessionsInactive", -1, -1, courseId);
                break;
            default:
                return getSessionsByCourseId(courseId);
        }        
        return (List<Session>) query.list();
    }

    @Override
    public Timestamp findSoonestTime(List<Session> sessionList) {

        return null;
    }

    @Override
    public void addSession(Session session) {
        persist(session);
        logger.info("Session saved successfully, Session Details=" + session);
    }
    

    @Override
    public long countSessionInPass(int courseId) {
        Query query = createNamedQuery("countSessionInPass", -1, -1, courseId);
        return (long) query.uniqueResult();
    }

	@Override
	public void updateSession(Session session) {
		update(session);
		logger.info("Session saved successfully, Session Details=" + session);
	}

	@Override
	public void deleteSession(Session session) {
		delete(session);
		logger.info("Session deleted successfully, Session Details=" + session);
	}

	@Override
	public Session getSessionById(int id) {
		Query query = createNamedQuery("getSessionById", -1, -1, id);
		return (Session) query.uniqueResult();
	}

	@Override
	public List<Session> getListCourseSuggest(int userId) {
		Query query = createNamedQuery("getListCourseSuggest", -1, -1, userId);
		return query.list();

	}

    @Override
    public List<Session> getSessionsStartToday(int courseId) {
        Query query = createNamedQuery("getSessionsStartToday", -1, -1, courseId);
        return query.list();
    }

    @Override
    public List<Session> getSessionsNotSentEmailToTrainer() {
        Query query = createNamedQuery("getSessionsNotSentEmailToTrainer", -1, -1);
        return (List<Session>) query.list();
    }


}
