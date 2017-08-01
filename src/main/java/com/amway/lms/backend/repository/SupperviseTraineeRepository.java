package com.amway.lms.backend.repository;

import java.util.List;

public interface SupperviseTraineeRepository {
	public List<Integer> findSupperviseTraineeBysupperviseId(int supperviseId);

}
