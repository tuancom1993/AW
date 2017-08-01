package com.amway.lms.backend.repository;

import com.amway.lms.backend.entity.TrainingAction;

public interface TrainingActionRepository {

	public void saveTrainingAction(TrainingAction trainingAction);
	
	public TrainingAction findTrainingActionBySessionParticipantId(int id);
	
	public TrainingAction getTrainingActionById(int id);
	
	public void updateTrainingAction(TrainingAction trainingAction);
}
