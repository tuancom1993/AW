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
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.service.SurveyQuestionService;

/**
 * @author acton
 * @email acton@enclave.vn
 */

@RestController()
@RequestMapping(value="/api/v1")
public class SurveyQuestionController {
    
    @Autowired
    SurveyQuestionService surveyQuestionService;
    
    @RequestMapping(value="/surveyQuestions", method=RequestMethod.POST)
    public ResponseEntity<?> addSurveyQuestion(@RequestBody SurveyQuestion surveyQuestion) throws AddObjectException, Exception{
        if(surveyQuestion.getQuestion().getId() == 0) 
            return surveyQuestionService.addSurveyQuestionWithNewQuestion(surveyQuestion);
        else
            return surveyQuestionService.addSurveyQuestionWithOldQuestion(surveyQuestion); 
    }
    
    @RequestMapping(value="/surveyQuestions", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteSurveyQuestion(@RequestBody SurveyQuestion surveyQuestion) throws ObjectNotFoundException, DeleteObjectException, Exception{
        return surveyQuestionService.deleteSurveyQuestion(surveyQuestion.getSurveyId(), surveyQuestion.getQuestionId());
    }
}
