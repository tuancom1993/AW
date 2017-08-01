package com.amway.lms.backend.rest;

import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.exception.SurveyNotAvailableException;
import com.amway.lms.backend.exception.SurveyNotStartYetException;
import com.amway.lms.backend.exception.SurveyOutOfExpiredDateException;

@RestController
@ControllerAdvice
public class ErrorController {

    /**
     * SQLException handler
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> sqlException(Exception ex) {
        return Utils.generateFailureResponseEntity(
                ErrorCode.CODE_SQL_EXCEPTION, ErrorCode.MSG_SQL_EXCEPTION);
    }
    
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> objectNotFoundException(Exception ex) {
        return Utils.generateFailureResponseEntity(
                ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);}

    @ExceptionHandler(SurveyNotAvailableException.class)
    public ResponseEntity<?> surveyNotAvailableException(Exception ex) {
        return Utils.generateFailureResponseEntity(
                ErrorCode.CODE_SURVEY_NOT_AVAILABLE_EXCEPTION, ErrorCode.MSG_SURVEY_NOT_AVAILABLE_EXCEPTION);

    }
    
    @ExceptionHandler(SurveyOutOfExpiredDateException.class)
    public ResponseEntity<?> surveyOutOfExpiredDateException(Exception ex) {
        return Utils.generateFailureResponseEntity(
                ErrorCode.CODE_SURVEY_OUT_OF_EXPIRED_DATE_EXCEPTION, ErrorCode.MSG_SURVEY_OUT_OF_EXPIRED_DATE_EXCEPTION);

    }
    
    @ExceptionHandler(SurveyNotStartYetException.class)
    public ResponseEntity<?> surveyNotStartYet(Exception ex) {
        return Utils.generateFailureResponseEntity(
                ErrorCode.CODE_SURVEY_NOT_START_YET_EXCEPTION, ErrorCode.MSG_SURVEY_NOT_START_YET_EXCEPTION);

    }

    // @ExceptionHandler(org.springframework.security.web.authentication.rememberme.CookieTheftException.class)
    // public String cookieTheftHandler(Exception ex) {
    // return "redirect:/invalid-token.html";
    // }

    /**
     * CookieTheftException (invalid access token) handler
     * 
     * @return
     */
    @RequestMapping(value = "/invalid-token", method = RequestMethod.GET)
    public ResponseEntity<?> invalidTokenGet() {
        return invalidTokenResponse();
    }

    /**
     * CookieTheftException (invalid access token) handler
     * 
     * @return
     */
    @RequestMapping(value = "/invalid-token", method = RequestMethod.POST)
    public ResponseEntity<?> invalidTokenPost() {
        return invalidTokenResponse();
    }

    private ResponseEntity<?> invalidTokenResponse() {
        return Utils.generateFailureResponseEntity(
                ErrorCode.CODE_TOKEN_INVALID, ErrorCode.MSG_TOKEN_INVALID);
    }

    /**
     * 404 Not found error handler
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> notFoundHandler(Exception ex) {
        return Utils.generateFailureResponseEntity(ErrorCode.CODE_NOT_FOUND,
                ErrorCode.MSG_NOT_FOUND);
    }
    
    /**
     * 404 Not found error handler
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class, NumberFormatException.class, IllegalArgumentException.class, Exception.class})
    public ResponseEntity<?> badRequestHandler(Exception ex) {
        ex.printStackTrace();
        return Utils.generateFailureResponseEntity(ErrorCode.CODE_NOT_FOUND,
                ErrorCode.MSG_NOT_FOUND);
    }

    /**
     * 500 Internal server error handler
     * 
     * @return
     */
    @RequestMapping(value = "/internal-error", method = RequestMethod.GET)
    public ResponseEntity<?> internalErrorGet() {
        return internalErrorResponse();
    }

    /**
     * 500 Internal server error handler
     * 
     * @return
     */
    @RequestMapping(value = "/internal-error", method = RequestMethod.POST)
    public ResponseEntity<?> internalErrorPost() {
        return internalErrorResponse();
    }

    private ResponseEntity<?> internalErrorResponse() {
        return Utils.generateFailureResponseEntity(
                ErrorCode.CODE_INTERNAL_SERVER_ERROR,
                ErrorCode.MSG_INTERNAL_SERVER_ERROR);
    }

}
