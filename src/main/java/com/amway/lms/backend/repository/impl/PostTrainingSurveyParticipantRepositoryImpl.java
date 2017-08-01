/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.PostTrainingSurveyParticipant;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.PostTrainingSurveyParticipantRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository
public class PostTrainingSurveyParticipantRepositoryImpl extends AbstractRepository<Integer, PostTrainingSurveyParticipant> implements PostTrainingSurveyParticipantRepository{

    /* (non-Javadoc)
     * @see com.amway.lms.backend.repository.PostTrainingSurveyParticipantRepository#addPostTrainingSurveyParticipant(com.amway.lms.backend.entity.PostTrainingSurveyParticipant)
     */
    @Override
    public void addPostTrainingSurveyParticipant(PostTrainingSurveyParticipant survey) {
        persist(survey);
    }

    @Override
    public List<PostTrainingSurveyParticipant> getPostTrainingSurveyParticipantListBySessionId(int sessionId) {
        Query query = createNamedQuery("getPostTrainingSurveyParticipantListBySessionId", -1, -1, sessionId);
        return query.list();
    }

    @Override
    public List<PostTrainingSurveyParticipant> getPostTrainingSurveyParticipantBySessionIdAndUserId(int sessionId,
            int userId) {
        Query query = createNamedQuery("getPostTrainingSurveyParticipantBySessionIdAndUserId", -1, -1, sessionId, userId);
        return query.list();
    }

}
