package com.amway.lms.backend.exception;

import com.amway.lms.backend.common.ErrorCode;

public class TestNotAvailableException extends Exception{

    private static final long serialVersionUID = -8194078486064959936L;
    
    public static final int CODE = ErrorCode.CODE_TEST_NOT_AVAILABLE_EXCEPTION;
    public static final String MESSAGE = ErrorCode.MSG_TEST_NOT_AVAILABLE_EXCEPTION;

    public TestNotAvailableException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TestNotAvailableException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public TestNotAvailableException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public TestNotAvailableException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public TestNotAvailableException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
}
