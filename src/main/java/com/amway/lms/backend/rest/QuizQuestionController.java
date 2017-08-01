package com.amway.lms.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.entity.QuizQuestion;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.service.QuizQuestionService;
import com.amway.lms.backend.service.QuizService;

/**
 * @author acton
 * @email acton@enclave.vn
 */

@RestController()
@RequestMapping(value="/api/v1")
public class QuizQuestionController {
    
    @Autowired
    QuizService quizService;
    
    @Autowired
    QuizQuestionService quizQuestionService;
    
    @RequestMapping(value = "/quizQuestions", method = RequestMethod.POST)
    public ResponseEntity<?> addQuizQuestion(@RequestBody QuizQuestion quizQuestion) throws AddObjectException, Exception{
        if(quizQuestion.getQuestion().getId() == 0)
            return quizQuestionService.addQuizQuestionWithNewQuestion(quizQuestion.getQuizId(), quizQuestion.getQuestion());
        else
            return quizQuestionService.addQuizQuestionWithOldQuestion(quizQuestion.getQuizId(), quizQuestion.getQuestion());
    }
    
    @RequestMapping(value="/quizQuestions", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteQuizQuestion(@RequestBody QuizQuestion quizQuestion) throws ObjectNotFoundException, DeleteObjectException, Exception{
        return quizQuestionService.deleteQuizQuestion(quizQuestion.getQuizId(), quizQuestion.getQuestionId());
    }
}
