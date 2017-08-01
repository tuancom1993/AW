/**
 * 
 */
package com.amway.lms.backend.service;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.SurveySession;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;

/**
 * @author acton
 *
 */
public interface SurveySessionService {
    public ResponseEntity<?> addSurveySession(SurveySession surveySession) throws AddObjectException, Exception;
    public ResponseEntity<?> deleteSurveySession(int surveyId, int surveySessionId) throws DeleteObjectException, Exception;
}
