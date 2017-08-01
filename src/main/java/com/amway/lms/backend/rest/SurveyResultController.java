/**
 * 
 */
package com.amway.lms.backend.rest;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.SurveyResult;
import com.amway.lms.backend.service.SurveyResultService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@RestController()
@RequestMapping(value="/api/v1")
public class SurveyResultController {
    
    @Autowired
    ServletContext context;
    
    @Autowired
    SurveyResultService surveyResultService;
    
    @RequestMapping(value="/surveyResults", method = RequestMethod.POST)
    public ResponseEntity<?> addSurveyResult(@RequestBody SurveyResult surveyResult) throws HibernateException, SQLException, Exception{
        return surveyResultService.addSurveyResult(surveyResult);
    }

    @RequestMapping(value="/surveyResults/export/{surveyId}/vet", method = RequestMethod.GET)
    public void exportSurveyResult(HttpServletResponse response, @PathVariable("surveyId") int surveyId) throws HibernateException, SQLException, Exception{
        try {
            File exportFile = surveyResultService.exportSurveyResult(surveyId);
            if(exportFile == null) throw new Exception("Cannot export file");
            String fileName = Common.FILE_NAME_SURVEY_EXPORT;
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            IOUtils.copy(new FileInputStream(exportFile), response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().printf(Utils.generateFailureResponseString(ErrorCode.CODE_EXPORT_FILE_EXCEPTION, ErrorCode.MSG_EXPORT_FILE_EXCEPTION)).flush();
        }
    }
    
    @RequestMapping(value="/surveyResults/export/{surveyId}", method = RequestMethod.GET)
    public void exportSurveyResultFollowHorizontal(HttpServletResponse response, @PathVariable("surveyId") int surveyId) throws HibernateException, SQLException, Exception{
        try {
            File exportFile = surveyResultService.exportSurveyResultHorizontal(surveyId);
            if(exportFile == null) throw new Exception("Cannot export file");
            String fileName = exportFile.getName();
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            IOUtils.copy(new FileInputStream(exportFile), response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().printf(Utils.generateFailureResponseString(ErrorCode.CODE_EXPORT_FILE_EXCEPTION, ErrorCode.MSG_EXPORT_FILE_EXCEPTION)).flush();
        }
    }
}
