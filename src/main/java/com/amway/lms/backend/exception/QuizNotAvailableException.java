/**
 * 
 */
package com.amway.lms.backend.exception;

/**
 * @author acton
 * @email acton@enclave.vn
 */
public class QuizNotAvailableException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = 5705495126922428094L;

    public QuizNotAvailableException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public QuizNotAvailableException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public QuizNotAvailableException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public QuizNotAvailableException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public QuizNotAvailableException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
