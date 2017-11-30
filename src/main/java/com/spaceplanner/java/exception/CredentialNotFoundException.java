package com.spaceplanner.java.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: ashif
 * Date: 9/25/14
 * Time: 10:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class CredentialNotFoundException extends java.lang.RuntimeException {


    private static final long serialVersionUID = -2706518848253209769L;
    private Logger logger = LoggerFactory.getLogger(CredentialNotFoundException.class);

    private int code;
    private String message;

    public CredentialNotFoundException() {
        super();
        logger.error("CredentialNotFoundException occurred");
    }

    public CredentialNotFoundException(String message) {
        super(message);
        logger.error("CredentialNotFoundException occurred " + message);
    }

    public CredentialNotFoundException(int code, String message) {
        super(message);
        this.code = code;
        logger.error("CredentialNotFoundException occurred code " + code + " | Message " + message);
    }

    public CredentialNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.error("CredentialNotFoundException occurred " + message);
        cause.printStackTrace();
    }

}
