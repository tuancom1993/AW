package com.amway.lms.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.entity.SurveyQuestion;
import com.amway.lms.backend.entity.TestQuestion;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.service.TestQuestionService;

@RestController()
@RequestMapping(value = "/api/v1")
public class TestQuestionController {

    @Autowired
    TestQuestionService testQuestionService;

    @RequestMapping(value = "/testQuestions", method = RequestMethod.POST)
    public ResponseEntity<?> addTestQuestion(@RequestBody TestQuestion testQuestion)
            throws AddObjectException, Exception {
        if (testQuestion.getQuestion().getId() == 0)
            return testQuestionService.addTestQuestionWithNewQuestion(testQuestion);
        else
            return testQuestionService.addTestQuestionWithOldQuestion(testQuestion);
    }
    
    @RequestMapping(value = "/testQuestions", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTestQuestion(@RequestBody TestQuestion testQuestion) throws ObjectNotFoundException, DeleteObjectException, Exception{
        return testQuestionService.deleteTestQuestion(testQuestion.getTestId(), testQuestion.getQuestionId());
    }
}
