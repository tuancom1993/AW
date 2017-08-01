/**
 * 
 */
package com.amway.lms.backend.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.configuration.CustomUserDetail;
import com.amway.lms.backend.entity.PostTrainingSurveyTrainer;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.service.PostTrainingSurveyTrainerService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@RestController
@RequestMapping("/api/v1")
public class PostTrainingSurveyTrainerController {
    
    @Autowired
    PostTrainingSurveyTrainerService postTrainingSurveyTrainerService;
    
    @RequestMapping(value = "/postTrainingSurveyTrainers", method=RequestMethod.POST)
    public ResponseEntity<?> addPostTrainingSurveyTrainer(@RequestBody PostTrainingSurveyTrainer survey) throws AddObjectException, Exception{
        return postTrainingSurveyTrainerService.addPostTrainingSurveyTrainer(survey);
    }
    
    @RequestMapping(value="postTrainingSurveyTrainers/export/{sessionId}", method=RequestMethod.GET)
    public void exportPostTrainingSurveyTrainer(HttpServletResponse response ,@PathVariable("sessionId") int sessionId) throws AddObjectException, Exception{
        try {
            File exportFile = postTrainingSurveyTrainerService.exportPostTrainingSurveyTrainer(sessionId);
            if(exportFile == null) throw new Exception("Cannot export file");
            String fileName = Common.FILE_NAME_POST_TRAINING_SURVEY_TRAINER_EXPORT;
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            IOUtils.copy(new FileInputStream(exportFile), response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().printf(Utils.generateFailureResponseString(ErrorCode.CODE_EXPORT_FILE_EXCEPTION, ErrorCode.MSG_EXPORT_FILE_EXCEPTION)).flush();
        } 
    }
    @RequestMapping(value="postTrainingSurveyTrainers/encode/{sessionId}/{userId}", method=RequestMethod.GET)
    public ResponseEntity<?> encodePostTrainingSurvey(@PathVariable("sessionId") int sessionId, @PathVariable("userId") int userId) throws JsonGenerationException, JsonMappingException, IOException, Exception{
        return new ResponseEntity<>(postTrainingSurveyTrainerService.encodePostSurveyTrainer(sessionId, userId), HttpStatus.ACCEPTED);
    }
    
    @RequestMapping(value="postTrainingSurveyTrainers/getInfor/{encodedValue}", method=RequestMethod.GET)
    public ResponseEntity<?> getSessionInformationByEncodedValue(@PathVariable("encodedValue") String encodedValue) throws JsonGenerationException, JsonMappingException, IOException, Exception{
        User userLogin = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail) {
            CustomUserDetail customUserDetails = (CustomUserDetail) principal;
            userLogin = customUserDetails.getUser();
        }

        return postTrainingSurveyTrainerService.getSessionInformationByEncodedValue(encodedValue, userLogin);
    }
}
