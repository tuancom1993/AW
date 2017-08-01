package com.amway.lms.backend.exception;

import com.amway.lms.backend.common.ErrorCode;

public class TestingTimeException extends Exception{
    
    public static final int CODE = ErrorCode.CODE_TESTING_TIME_PARTICIPANT_LAGER_TESTING_TIME_EXCEPTION;
    public static final String MESSAGE = ErrorCode.MSG_TESTING_TIME_PARTICIPANT_LAGER_TESTING_TIME_EXCEPTION;
    
    public TestingTimeException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TestingTimeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public TestingTimeException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public TestingTimeException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public TestingTimeException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
