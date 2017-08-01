/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.TrainingNeed;

/**
 * @author acton
 *
 */
public interface TrainingNeedRepository{
    public List<TrainingNeed> getListTraningNeedByUserId(int userId);

    /**
     * @param id
     * @return
     */
    public boolean exists(int id);

    void addTrainingNeed(TrainingNeed trainingNeed);

    TrainingNeed getTrainingNeedById(int id);
}
