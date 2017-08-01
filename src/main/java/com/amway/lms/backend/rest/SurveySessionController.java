/**
 * 
 */
package com.amway.lms.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.entity.SurveyQuestion;
import com.amway.lms.backend.entity.SurveySession;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.service.SurveySessionService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@RestController
@RequestMapping("/api/v1")
public class SurveySessionController {

    @Autowired
    SurveySessionService surveySessionService;
    
    @RequestMapping(value = "/surveySessions", method = RequestMethod.POST)
    public ResponseEntity<?> addSurveySession(@RequestBody SurveySession surveySession) throws AddObjectException, Exception{
        return surveySessionService.addSurveySession(surveySession);
    }
    
    @RequestMapping(value = "/surveySessions", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSurveySession(@RequestBody SurveyQuestion surveyQuestion) throws AddObjectException, Exception{
        return surveySessionService.deleteSurveySession(surveyQuestion.getSurveyId(), surveyQuestion.getSurveySessionId());
    }
}
