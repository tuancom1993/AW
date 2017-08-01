package com.amway.lms.backend.exception;

import com.amway.lms.backend.common.ErrorCode;

public class UserNotFoundException extends Exception{
    
    public static final int CODE = ErrorCode.CODE_USER_NOT_FOUND_EXCEPTION;
    public static final String MESSAGE = ErrorCode.MSG_USER_NOT_FOUND_EXCEPTION;
    
    public UserNotFoundException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public UserNotFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public UserNotFoundException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
