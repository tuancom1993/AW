/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.PostTrainingSurveyTrainer;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.PostTrainingSurveyTrainerRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class PostTrainingSurveyTrainerRepositoryImpl extends AbstractRepository<Integer, PostTrainingSurveyTrainer> implements PostTrainingSurveyTrainerRepository{

    @Override
    public void addPostTrainingSurveyTrainer(PostTrainingSurveyTrainer survey) {
        persist(survey);
    }

    @Override
    public List<PostTrainingSurveyTrainer> getPostTrainingSurveyTrainerListBySessionId(int sessionId) {
        Query query = createNamedQuery("getPostTrainingSurveyTrainerListBySessionId", -1, -1, sessionId);
        return query.list();
    }

}
