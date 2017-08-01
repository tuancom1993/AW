package com.amway.lms.backend.exception;

import com.amway.lms.backend.common.ErrorCode;

public class OutOfExpiredDateException extends Exception{
    
    public static final int CODE = ErrorCode.CODE_TEST_OUT_OF_EXPIRED_DATE_EXCEPTION;
    public static final String MESSAGE = ErrorCode.MSG_TEST_OUT_OF_EXPIRED_DATE_EXCEPTION;
    
    public OutOfExpiredDateException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public OutOfExpiredDateException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public OutOfExpiredDateException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public OutOfExpiredDateException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public OutOfExpiredDateException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
