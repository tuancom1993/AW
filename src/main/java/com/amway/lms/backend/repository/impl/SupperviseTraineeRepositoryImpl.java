package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.SupperviseTrainee;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.SupperviseTraineeRepository;

@Repository
public class SupperviseTraineeRepositoryImpl extends AbstractRepository<Integer, SupperviseTrainee> implements SupperviseTraineeRepository {

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> findSupperviseTraineeBysupperviseId(int supperviseId) {
		Query query = createNamedQuery("findSupperviseTraineeBysupperviseId", -1, -1, supperviseId);
		return query.list();
	}

}
