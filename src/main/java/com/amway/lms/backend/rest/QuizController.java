/**
 * 
 */
package com.amway.lms.backend.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.entity.Quiz;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.service.QuestionService;
import com.amway.lms.backend.service.QuizService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@RestController
@RequestMapping("/api/v1")
public class QuizController {
    
    @Value("${param.access_token}")
    private String PARAM_TOKEN_HEADER;
    
    @Autowired
    QuizService quizService;
    
    @Autowired
    QuestionService questionService;
    
    @RequestMapping(value = "/quizzes", method=RequestMethod.GET)
    public ResponseEntity<?> getQuizList() throws Exception{
        return quizService.getQuizList();
    }
    
    @RequestMapping(value = "/quizzes", method=RequestMethod.POST)
    public ResponseEntity<?> addQuiz(@RequestBody Quiz quiz) throws AddObjectException, Exception{
        return quizService.addQuiz(quiz);
    }
    
    @RequestMapping(value = "/quizzes/full", method=RequestMethod.POST)
    public ResponseEntity<?> addQuizFull(@RequestBody Quiz quiz) throws AddObjectException, Exception{
        return quizService.addQuizFull(quiz);
    }
    
    @RequestMapping(value = "/quizzes", method=RequestMethod.PUT)
    public ResponseEntity<?> editQuizInfomation(@RequestBody Quiz quiz) throws AddObjectException, Exception{
        return quizService.editQuiz(quiz); 
    }
    
    @RequestMapping(value = "/quizzes/full", method=RequestMethod.PUT)
    public ResponseEntity<?> editQuizFull(@RequestBody Quiz quiz) throws AddObjectException, Exception{
        return quizService.editQuizFull(quiz); 
    }
    
    @RequestMapping(value = "/quizzes/{quizId}", method=RequestMethod.GET)
    public ResponseEntity<?> getQuizInfor(@PathVariable("quizId") int quizId) throws ObjectNotFoundException, Exception{
        return quizService.getQuizInfor(quizId);
    }
    
    @RequestMapping(value = "/quizzes/{quizId}/questions", method=RequestMethod.GET)
    public ResponseEntity<?> getQuestionListOfQuizByQuizId(@PathVariable("quizId") int quizId) throws ObjectNotFoundException, Exception{
        return questionService.getQuestionListByQuizId(quizId);
    }
    
    @RequestMapping(value = "/quizzes", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteQuiz(@RequestBody Quiz quiz) throws AddObjectException, Exception{
        return quizService.deleteQuiz(quiz.getId()); 
    }
    
    @RequestMapping(value = "quizzes/search", method=RequestMethod.GET)
    public ResponseEntity<?> searchQuiz(@RequestParam("name") String searchString) throws Exception{
        return quizService.searchQuizByName(searchString);
    }
    
//    @RequestMapping(value = "quizzes/start/{sessionId}/{userId}", method=RequestMethod.GET)
//    public ResponseEntity<?> startQuiz(@PathVariable("sessionId") int sessionId, @PathVariable("userId") int userId) throws Exception{
//        return quizService.startQuiz(sessionId, userId);
//    }
    
    @RequestMapping(value = "quizzes/start/{sessionParticipantId}", method=RequestMethod.GET)
    public ResponseEntity<?> startQuizBySessionParticipant(HttpServletRequest request,@PathVariable("sessionParticipantId") int sessionParticipantId) throws Exception{
        return quizService.startQuiz(sessionParticipantId, request.getHeader(PARAM_TOKEN_HEADER));
    }
}
