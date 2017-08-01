package com.amway.lms.backend.common;

public class ErrorCode {

    public static final int CODE_SUCCESS = 0;
    public static final String MSG_SUCCESS = "Success";

    public static final int CODE_AUTHENTICATION_ERROR = 1;
    public static final String MSG_AUTHENTICATION_ERROR = "Authentication error";

    public static final int CODE_ACCESS_DENIED = 2;
    public static final String MSG_ACCESS_DENIED = "Access denied";

    public static final int CODE_LOGIN_ERROR = 3;
    public static final String MSG_LOGIN_ERROR = "Invalid User ID or Password";
    
    public static final int CODE_INVALID_PARAMS = 302;
    public static final String MSG_INVALID_PARAMS = "There was a missing or invalid parameter..";

    public static final int CODE_TOKEN_INVALID = 4;
    public static final String MSG_TOKEN_INVALID = "Invalid token";

    public static final int CODE_SQL_EXCEPTION = 601;
    public static final String MSG_SQL_EXCEPTION = "SQL exception";

    public static final int CODE_ADD_OBJECT_EXCEPTION = 602;
    public static final String MSG_ADD_OBJECT_EXCEPTION = "Object cannot be added";

    public static final int CODE_DELETE_OBJECT_EXCEPTION = 603;
    public static final String MSG_DELETE_OBJECT_EXCEPTION = "Object cannot be deleted";

    public static final int CODE_EDIT_OBJECT_EXCEPTION = 604;
    public static final String MSG_EDIT_OBJECT_EXCEPTION = "Object cannot be edited";

    public static final int CODE_OBJECT_NOT_FOUND_EXCEPTION = 605;
    public static final String MSG_OBJECT_NOT_FOUND_EXCEPTION = "Object could not be found";

    public static final int CODE_SURVEY_NOT_AVAILABLE_EXCEPTION = 606;
    public static final String MSG_SURVEY_NOT_AVAILABLE_EXCEPTION = "Survey not available";
    
    public static final int CODE_QUESTION_EXIST_EXCEPTION = 607;
    public static final String MSG_QUESTION_EXSIT_EXCEPTION = "Quesion is exist";
    
    public static final int CODE_UPLOAD_FILE_EXCEPTION = 608;
    public static final String MSG_UPLOAD_FILE_EXCEPTION = "File cannot upload";
    
    public static final int CODE_QUIZ_NOT_AVAILABLE_EXCEPTION = 609;
    public static final String MSG_QUIZ_NOT_AVAILABLE_EXCEPTION = "Quiz not available";
    
    public static final int CODE_SURVEY_OUT_OF_EXPIRED_DATE_EXCEPTION = 610;
    public static final String MSG_SURVEY_OUT_OF_EXPIRED_DATE_EXCEPTION = "Survey out of Expired date";
    
    public static final int CODE_SURVEY_NOT_START_YET_EXCEPTION = 611;
    public static final String MSG_SURVEY_NOT_START_YET_EXCEPTION = "Survey has not started yet";

    public static final int CODE_SURVEY_CANNOT_IMPORT_EXCEPTION = 612;
    public static final String MSG_SURVEY_CANNOT_IMPORT_EXCEPTION = "Survey cannot import";
    
    public static final int CODE_EXPORT_FILE_EXCEPTION = 613;
    public static final String MSG_EXPORT_FILE_EXCEPTION = "File cannot export";
    
    public static final int CODE_QUIZ_RESULT_EXCEPTION = 614;
    public static final String MSG_QUIZ_RESULT_EXCEPTION = "Quiz cannot calculate result";

    public static final int CODE_PARTICIPANT_CANNOT_IMPORT_EXCEPTION = 615;
    public static final String MSG_PARTICIPANT_CANNOT_IMPORT_EXCEPTION = "Participant can not import";
    
    public static final int CODE_NOT_FOUND = 404;
    public static final String MSG_NOT_FOUND = "404 Not found";

    public static final int CODE_INTERNAL_SERVER_ERROR = 500;
    public static final String MSG_INTERNAL_SERVER_ERROR = "500 Internal server error";

	public static final int DATA_INPUT_INVALID = 606;
	public static final String MSG_DATA_INPUT_INVALID = "606 Data input invalid";
	
	public static final int CODE_POST_TRAINING_SURVEY_PARTICIPANT_EXCEPTION = 615;
    public static final String MSG_POST_TRAINING_SURVEY_PARTICIPANT_EXCEPTION = "Post Training Survey Participant not available";
    
    public static final int CODE_POST_TRAINING_SURVEY_TRAINER_EXCEPTION = 616;
    public static final String MSG_POST_TRAINING_SURVEY_TRAINER_EXCEPTION = "Post Training Survey TRAINER not available";
    
    public static final int CODE_TEST_NOT_AVAILABLE_EXCEPTION = 617;
    public static final String MSG_TEST_NOT_AVAILABLE_EXCEPTION = "Test is not available!";
    
    public static final int CODE_TEST_RESULT_EXCEPTION = 618;
    public static final String MSG_TEST_RESULT_EXCEPTION = "Test cannot calculate result";
    
    public static final int CODE_SEND_EMAIL_EXCEPTION = 619;
    public static final String MSG_SEND_EMAIL_EXCEPTION = "Cannot send email";
    
    public static final int CODE_USER_NOT_FOUND_EXCEPTION = 620; 
    public static final String  MSG_USER_NOT_FOUND_EXCEPTION = "User could not be found!";
    
    public static final int CODE_USER_LOGIN_NOT_CORRECT_EXCEPTION = 621; 
    public static final String MSG_USER_LOGIN_NOT_CORRECT_EXCEPTION = "Log in details are not correct!";
    
    public static final int CODE_TESTING_TIME_PARTICIPANT_LAGER_TESTING_TIME_EXCEPTION = 622;
    public static final String MSG_TESTING_TIME_PARTICIPANT_LAGER_TESTING_TIME_EXCEPTION = "Number testing time of Participant is lager than Test's testing time!";
    
    public static final int CODE_TEST_OUT_OF_EXPIRED_DATE_EXCEPTION = 623;
    public static final String MSG_TEST_OUT_OF_EXPIRED_DATE_EXCEPTION = "Test is out of expired date!";

}