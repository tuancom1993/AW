/**
 * 
 */
package com.amway.lms.backend.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.SurveyQuestion;
import com.amway.lms.backend.entity.SurveySession;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.repository.ResultRepository;
import com.amway.lms.backend.repository.SurveyQuestionRepository;
import com.amway.lms.backend.repository.SurveySessionRepository;
import com.amway.lms.backend.service.SurveySessionService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Service
@Transactional
public class SurveySessionServiceImpl implements SurveySessionService {

    @Autowired
    SurveySessionRepository surveySessionRepository;

    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    ResultRepository resultRepository;

    @Override
    public ResponseEntity<?> addSurveySession(SurveySession surveySession) throws AddObjectException, Exception {
        try {
            surveySessionRepository.addSurveySession(surveySession);
            return Utils.generateSuccessResponseEntity(surveySession);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> deleteSurveySession(int surveyId, int surveySessionId)
            throws DeleteObjectException, Exception {
        try {
            SurveySession surveySession = surveySessionRepository.getSurveySessionById(surveySessionId);
            if (surveySession == null)
                throw new DeleteObjectException(
                        "Cannot delete SurveySession. Cause not found surveySession with id: " + surveySessionId);

            surveySessionRepository.deleteSurveySession(surveySession);

            List<SurveyQuestion> surveyQuestions = surveyQuestionRepository
                    .getSurveyQuestionListBySurveyIdAndSurveySessionId(surveyId, surveySessionId);

            for (SurveyQuestion surveyQuestion : surveyQuestions) {
                surveyQuestionRepository.deleteSurveyQuestion(surveyQuestion);
                resultRepository.deleteResultBySurveyIdAndQuestionId(surveyId, surveyQuestion.getQuestionId());
            }

            return Utils.generateSuccessResponseEntity(surveySession);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }

    }

}
