package com.amway.lms.backend.rest;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.entity.QuizResult;
import com.amway.lms.backend.service.QuizResultService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@RestController()
@RequestMapping(value="/api/v1")
public class QuizResultController {
    
    @Autowired
    QuizResultService quizResultService;
    
    @RequestMapping(value="/quizResults", method = RequestMethod.POST)
    public ResponseEntity<?> quizResults(@RequestBody QuizResult quizResult) throws HibernateException, SQLException, Exception{
        return quizResultService.quizResults(quizResult);
    }
    
    @RequestMapping(value="/quizResults/{quizId}", method = RequestMethod.GET)
    public ResponseEntity<?> quizResultListByQuizId(@PathVariable("quizId") int quizId) throws HibernateException, SQLException, Exception{
        return quizResultService.getQuizResults(quizId);
    }
}
