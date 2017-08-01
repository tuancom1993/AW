/**
 * 
 */
package com.amway.lms.backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.Survey;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.SurveyNotAvailableException;
import com.amway.lms.backend.exception.SurveyNotStartYetException;
import com.amway.lms.backend.exception.SurveyOutOfExpiredDateException;
import com.amway.lms.backend.model.SurveyModelForCreate;

/**
 * @author acton
 *
 */
public interface SurveyService {
    public ResponseEntity<?> getTrainingNeedAssessmentSurvey(String surveyIdEncoded) throws ObjectNotFoundException, SurveyNotAvailableException, SurveyNotStartYetException, SurveyOutOfExpiredDateException ,Exception;
    public ResponseEntity<?> getPreTrainingServeySurvey(String surveyIdEncoded) throws ObjectNotFoundException, SurveyNotAvailableException, SurveyNotStartYetException, SurveyOutOfExpiredDateException ,Exception;
    public ResponseEntity<?> getSurveyList() throws Exception;
    public ResponseEntity<?> addSurvey(Survey survey) throws AddObjectException, Exception;
    public ResponseEntity<?> editServeyInfo(Survey survey) throws ObjectNotFoundException, EditObjectException, Exception;
    public ResponseEntity<?> cloneSurvey(int surveyId) throws Exception;
    public ResponseEntity<?> getSurveyInfor(int surveyId) throws ObjectNotFoundException, Exception;
    public ResponseEntity<?> genURL(int surveyId) throws Exception;
    public ResponseEntity<?> deleteSurvey(int surveyId) throws DeleteObjectException, Exception;
    public ResponseEntity<?> deleteSurvey(List<Survey> surveys) throws DeleteObjectException, Exception;
    public ResponseEntity<?> editServeyFull(Survey survey) throws EditObjectException, Exception;
    public ResponseEntity<?> getSurveyFull(int surveyId) throws ObjectNotFoundException, Exception;
    public ResponseEntity<?> createSurveyFull(SurveyModelForCreate surveyModelForCreate) throws AddObjectException, Exception;
}
