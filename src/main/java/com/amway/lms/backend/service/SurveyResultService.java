/**
 * 
 */
package com.amway.lms.backend.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.SurveyResult;

/**
 * @author acton
 *
 */
public interface SurveyResultService {
    public ResponseEntity<?> addSurveyResult(SurveyResult surveyResult) throws HibernateException, SQLException, Exception;
    public ResponseEntity<?> getSurveyResultBySurveyId(int surveyId) throws Exception;
    public File exportSurveyResult(int surveyId) throws IOException, Exception;
    public File exportSurveyResultHorizontal(int surveyId) throws IOException, Exception;
}
