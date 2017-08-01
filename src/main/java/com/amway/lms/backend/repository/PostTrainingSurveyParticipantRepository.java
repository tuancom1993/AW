/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.PostTrainingSurveyParticipant;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public interface PostTrainingSurveyParticipantRepository {
    public void addPostTrainingSurveyParticipant(PostTrainingSurveyParticipant survey);
    public List<PostTrainingSurveyParticipant> getPostTrainingSurveyParticipantListBySessionId(int sessionId);
    public List<PostTrainingSurveyParticipant> getPostTrainingSurveyParticipantBySessionIdAndUserId(int sessionId, int userId);
}
