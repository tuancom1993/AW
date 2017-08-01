/**
 * 
 */
package com.amway.lms.backend.service;

import java.io.File;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.PostTrainingSurveyTrainer;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.AddObjectException;

/**
 * @author acton
 *
 */
public interface PostTrainingSurveyTrainerService {
    public ResponseEntity<?> addPostTrainingSurveyTrainer(PostTrainingSurveyTrainer survey) throws AddObjectException, Exception;
    public File exportPostTrainingSurveyTrainer(int sessionId);
    public String encodePostSurveyTrainer(int sessionId, int userId);
    public ResponseEntity<?> getSessionInformationByEncodedValue(String encodedValue, User userLogin) throws Exception;
}
