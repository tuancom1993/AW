package com.amway.lms.backend.service;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.TrainingNeed;

public interface TraineeNeedService {

	public ResponseEntity<?> createNewTraineeNeed(TrainingNeed trainingNeed);
}
