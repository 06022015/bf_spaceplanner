package com.spaceplanner.java.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Ashif.Qureshi
 * Date: 9/19/14
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class DuplicateObjectException extends java.lang.RuntimeException {

    private static final long serialVersionUID = -2706518848253209769L;
    private Logger logger = LoggerFactory.getLogger(DuplicateObjectException.class);

    private String message;

    public DuplicateObjectException() {
        super();
        logger.error("DuplicateObjectException occurred");
    }

    public DuplicateObjectException(String message) {
        super(message);
        logger.error("DuplicateObjectException occured " + message);
    }

    public DuplicateObjectException(String message, Throwable cause) {
        super(message, cause);
        logger.error("DuplicateObjectException occurred " + message);
        cause.printStackTrace();
    }
}
