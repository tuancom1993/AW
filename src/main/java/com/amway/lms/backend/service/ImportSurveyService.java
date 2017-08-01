package com.amway.lms.backend.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.amway.lms.backend.exception.ImportSurveyException;

public interface ImportSurveyService {
    public ResponseEntity<?> importSurvey(MultipartFile surveyFile) throws IOException, ImportSurveyException, Exception;
}
