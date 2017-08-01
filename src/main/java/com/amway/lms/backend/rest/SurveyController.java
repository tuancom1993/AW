/**
 * 
 */
package com.amway.lms.backend.rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Survey;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.SurveyNotAvailableException;
import com.amway.lms.backend.exception.SurveyOutOfExpiredDateException;
import com.amway.lms.backend.model.SurveyModelForCreate;
import com.amway.lms.backend.service.ImportSurveyService;
import com.amway.lms.backend.service.QuestionService;
import com.amway.lms.backend.service.SurveyQuestionService;
import com.amway.lms.backend.service.SurveyResultService;
import com.amway.lms.backend.service.SurveyService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@RestController
@RequestMapping("/api/v1")
public class SurveyController {
    @Autowired
    SurveyService surveyService;

    @Autowired
    SurveyQuestionService surveyQuestionService;

    @Autowired
    QuestionService questionService;

    @Autowired
    SurveyResultService surveyResultService;

    @Autowired
    ImportSurveyService importSurveyService;

    @RequestMapping(value = "/surveys/start/trainingNeed/{surveyIdEncoded}")
    public ResponseEntity<?> startTrainingNeedAssessmentSurvey(@PathVariable("surveyIdEncoded") String surveyIdEncoded)
            throws ObjectNotFoundException, SurveyNotAvailableException, SurveyOutOfExpiredDateException,
            SurveyNotAvailableException, Exception {
        return surveyService.getTrainingNeedAssessmentSurvey(surveyIdEncoded);
    }

    @RequestMapping(value = "/surveys/{surveyId}/encode")
    public ResponseEntity<?> encodeIdSurvey(@PathVariable("surveyId") int surveyId)
            throws ObjectNotFoundException, Exception {
        return new ResponseEntity<>(Utils.encode(System.currentTimeMillis() + "_" + surveyId), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/surveys/start/preTraining/{surveyIdEncoded}")
    public ResponseEntity<?> startPreTrainingSurvey(@PathVariable("surveyIdEncoded") String surveyIdEncoded)
            throws ObjectNotFoundException, SurveyNotAvailableException, SurveyOutOfExpiredDateException,
            SurveyNotAvailableException, Exception {
        return surveyService.getPreTrainingServeySurvey(surveyIdEncoded);
    }

    @RequestMapping(value = "/surveys", method = RequestMethod.GET)
    public ResponseEntity<?> getSurveyList() throws Exception {
        return surveyService.getSurveyList();
    }

    @RequestMapping(value = "/surveys", method = RequestMethod.POST)
    public ResponseEntity<?> addSurvey(@RequestBody Survey survey) throws Exception {
        return surveyService.addSurvey(survey);
    }

    @RequestMapping(value = "/surveys", method = RequestMethod.PUT)
    public ResponseEntity<?> editSurveyInfo(@RequestBody Survey survey) throws Exception {
        return surveyService.editServeyInfo(survey);
    }

    @RequestMapping(value = "/surveys/clone", method = RequestMethod.PUT)
    public ResponseEntity<?> cloneSurvey(@RequestBody Survey survey) throws Exception {
        return surveyService.cloneSurvey(survey.getId());
    }

    @RequestMapping(value = "/surveys/{surveyId}", method = RequestMethod.GET)
    public ResponseEntity<?> getSurveyInformation(@PathVariable("surveyId") int surveyId)
            throws ObjectNotFoundException, Exception {
        return surveyService.getSurveyInfor(surveyId);
    }

    @RequestMapping(value = "/surveys/{surveyId}/questions", method = RequestMethod.GET)
    public ResponseEntity<?> getQuestionListBySurveyId(@PathVariable("surveyId") int surveyId)
            throws ObjectNotFoundException, Exception {
        return questionService.getQuestionListBySurveyId(surveyId);
    }

    @RequestMapping(value = "surveys/{surveyId}/genURL", method = RequestMethod.GET)
    public ResponseEntity<?> getURL(@PathVariable("surveyId") int surveyId) throws ObjectNotFoundException, Exception {
        return surveyService.genURL(surveyId);
    }

    @RequestMapping(value = "/surveys/{surveyId}/surveyResults", method = RequestMethod.GET)
    public ResponseEntity<?> getSurveyResult(@PathVariable("surveyId") int surveyId)
            throws HibernateException, SQLException, Exception {
        return surveyResultService.getSurveyResultBySurveyId(surveyId);
    }

    @RequestMapping(value = "/surveys", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSurvey(@RequestBody Survey survey) throws Exception {
        return surveyService.deleteSurvey(survey.getId());
    }

    @RequestMapping(value = "/surveys/list", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSurvey(@RequestBody List<Survey> surveys) throws Exception {
        return surveyService.deleteSurvey(surveys);
    }

    @RequestMapping(value = "/surveys/full", method = RequestMethod.PUT)
    public ResponseEntity<?> editSurveyFull(@RequestBody Survey survey) throws Exception {
        return surveyService.editServeyFull(survey);
    }

    @RequestMapping(value = "/surveys/{surveyId}/full", method = RequestMethod.GET)
    public ResponseEntity<?> getSurveyFull(@PathVariable("surveyId") int surveyId) throws Exception {
        return surveyService.getSurveyFull(surveyId);
    }

    @RequestMapping(value = "/surveys/import", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
    public ResponseEntity<?> importSurvey(@RequestParam("file-survey") MultipartFile surveyFile)
            throws IOException, Exception {
        return importSurveyService.importSurvey(surveyFile);
    }

    @RequestMapping(value = "/surveys/full", method = RequestMethod.POST)
    public ResponseEntity<?> addSurveyFull(@RequestBody SurveyModelForCreate surveyModelForCreate) throws Exception {
        return surveyService.createSurveyFull(surveyModelForCreate);
    }
}
