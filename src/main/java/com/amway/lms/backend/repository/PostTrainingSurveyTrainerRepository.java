/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.PostTrainingSurveyTrainer;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface PostTrainingSurveyTrainerRepository {
    public void addPostTrainingSurveyTrainer(PostTrainingSurveyTrainer survey);
    public List<PostTrainingSurveyTrainer> getPostTrainingSurveyTrainerListBySessionId(int sessionId);
}
