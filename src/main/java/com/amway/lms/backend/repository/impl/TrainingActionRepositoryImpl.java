package com.amway.lms.backend.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.TrainingAction;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.TrainingActionRepository;

@Repository
public class TrainingActionRepositoryImpl extends AbstractRepository<Integer, TrainingAction> implements TrainingActionRepository {

	@Override
	public void saveTrainingAction(TrainingAction trainingAction) {
		persist(trainingAction);

	}

	@Override
	public TrainingAction findTrainingActionBySessionParticipantId(int id) {
		Query query=createNamedQuery("findTrainingActionBySessionParticipantId", -1, -1, id);
		return (TrainingAction) query.uniqueResult();
	}

	@Override
	public void updateTrainingAction(TrainingAction trainingAction) {
		update(trainingAction);
	}

    @Override
    public TrainingAction getTrainingActionById(int id) {
        Query query=createNamedQuery("getTrainingActionById", -1, -1, id);
        return (TrainingAction) query.uniqueResult();
    }


}
