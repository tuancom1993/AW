package com.amway.lms.backend.repository.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.PreparatoryParticipant;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.PreparatoryParticipantRepository;

@Repository
public class PreparatoryParticipantRepositoryImpl extends AbstractRepository<Serializable, PreparatoryParticipant> implements PreparatoryParticipantRepository {

	private static final Logger logger = LoggerFactory.getLogger(PreparatoryParticipantRepositoryImpl.class);

	@Override
	public void addPreparatoryParticipant(PreparatoryParticipant preparatoryParticipant) {
		persist(preparatoryParticipant);
		logger.info("PreparatoryParticipant saved successfully, PreparatoryParticipant Details=" + preparatoryParticipant);
	}

	@Override
	public List<PreparatoryParticipant> getPreparatoryParticipant(int trainingPlanId, int userId) {
		Query query = createNamedQuery("getPreparatoryParticipant", -1, -1, trainingPlanId, userId);
		return (List<PreparatoryParticipant>) query.list();
	}

	@Override
	public void delPreparatoryParticipant(List<PreparatoryParticipant> preparatoryParticipants) {
		for (PreparatoryParticipant preparatoryParticipant : preparatoryParticipants) {
			delete(preparatoryParticipant);
		}
		logger.info("Deleted PreparatoryParticipant successfully, PreparatoryParticipant Details=" + preparatoryParticipants);
	}

	@Override
	public PreparatoryParticipant getPreparatoryParticipantByCourseIdAndUserId(int courseId, int userId, int sessionId) {
		Query query = createNamedQuery("getPreparatoryParticipantByCourseIdAndUserId", -1, -1, courseId, userId, sessionId);
		return (PreparatoryParticipant) query.uniqueResult();
	}

    @Override
    public List<PreparatoryParticipant> getPreparatoryParticipantByCourse(
            int courseId) {
        Query query = createNamedQuery("getPreparatoryParticipantByCourse", -1, -1, courseId);
        return (List<PreparatoryParticipant>) query.list();
    }

    @Override
    public List<PreparatoryParticipant> getPreparatoryParticipantByTrainingPlanId(
            int trainingPlanId) {
        Query query = createNamedQuery("getPreparatoryParticipantByTrainingPlanId", -1, -1, trainingPlanId);
        return (List<PreparatoryParticipant>) query.list();
    }

}
