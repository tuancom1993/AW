/**
 * 
 */
package com.amway.lms.backend.rest;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.service.QuestionService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@RestController
@RequestMapping("/api/v1")
public class QuestionController {
    
    public static String rootPath = null;
    
    @Autowired
    ServletContext context;
    
    @Autowired
    QuestionService questionService;
    
    @PostConstruct
    public void init(){
        //rootPath = context.getRealPath("/");
        //rootPath = System.getProperty("user.home") + Common.PATH_FOLDER_AMWAY_RESOURCES_EXTERNAL;
        rootPath = Common.PATH_FOLDER_AMWAY_RESOURCES_EXTERNAL;
        System.err.println("RootPath: "+rootPath);
    }
    
    @RequestMapping(value="/questions/survey/{surveyId}", method=RequestMethod.GET)
    public ResponseEntity<?> getQuestionListNotInSurvey(@PathVariable("surveyId") int surveyId) throws Exception{
        return questionService.getQuestionListNotInSurvey(surveyId);
    }   
    
    @RequestMapping(value="/questions/quiz/{quizId}", method=RequestMethod.GET)
    public ResponseEntity<?> getQuestionListNotInQuiz(@PathVariable("quizId") int quizId) throws Exception{
        return questionService.getQuestionListNotInQuiz(quizId);
    }
    
    @RequestMapping(value="/questions/test/{testId}", method=RequestMethod.GET)
    public ResponseEntity<?> getQuestionListNotInTest(@PathVariable("testId") int testId) throws Exception{
        return questionService.getQuestionListNotInTest(testId);
    }
    
    @RequestMapping(value="/questions/file", headers = "content-type=multipart/*" ,method=RequestMethod.POST)
    public ResponseEntity<?> uploadFileForQuestion(@RequestParam("file-question") MultipartFile file) throws IOException, Exception{
        return questionService.uploadFileForQuestion(file, rootPath);
    }
}
