package com.amway.lms.backend.exception;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public class SurveyOutOfExpiredDateException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = 4885563729553534440L;

    public SurveyOutOfExpiredDateException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public SurveyOutOfExpiredDateException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public SurveyOutOfExpiredDateException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public SurveyOutOfExpiredDateException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public SurveyOutOfExpiredDateException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
