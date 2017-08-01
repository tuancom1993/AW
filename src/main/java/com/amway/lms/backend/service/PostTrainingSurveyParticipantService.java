/**
 * 
 */
package com.amway.lms.backend.service;

import java.io.File;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.PostTrainingSurveyParticipant;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.AddObjectException;

/**
 * @author acton
 *
 */
public interface PostTrainingSurveyParticipantService {
    public ResponseEntity<?> addPostTrainingSurveyParticipant(PostTrainingSurveyParticipant survey) throws AddObjectException, Exception;
    public File exportPostTrainingSurveyParticipant(int sessionId);
    public String encodePostSurveyParticipant(int sessionId, int userId);
    public ResponseEntity<?> getSessionInformationByEncodedValue(String encodedValue, User userLogin) throws Exception;
}
