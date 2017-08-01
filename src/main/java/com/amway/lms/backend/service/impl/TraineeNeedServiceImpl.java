package com.amway.lms.backend.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.TrainingNeed;
import com.amway.lms.backend.repository.TrainingNeedRepository;
import com.amway.lms.backend.service.TraineeNeedService;

@Service
@Transactional
public class TraineeNeedServiceImpl implements TraineeNeedService {

	@Autowired
	private TrainingNeedRepository trainingNeedRepository;

	@Override
	public ResponseEntity<?> createNewTraineeNeed(TrainingNeed trainingNeed) {
		trainingNeedRepository.addTrainingNeed(trainingNeed);
		return Utils.generateSuccessResponseEntity("createNewTraineeNeed Successful");
	}

}
