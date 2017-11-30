package com.spaceplanner.java.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Ashif.Qureshi
 * Date: 9/18/14
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationException extends java.lang.RuntimeException {

    private static final long serialVersionUID = -2706518848253209769L;
    private Logger logger = LoggerFactory.getLogger(AuthenticationException.class);

    private int code;
    private String message;

    public AuthenticationException() {
        super();
        logger.error("AuthenticationException occurred");
    }

    public AuthenticationException(String message) {
        super(message);
        logger.error("AuthenticationException occurred "+message);
    }

    public AuthenticationException(int code, String message) {
        super(message);
        this.code = code;
        logger.error("AuthenticationException occurred code "+code+" | Message "+message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        logger.error("AuthenticationException occurred "+message);
        cause.printStackTrace();
    }
}
