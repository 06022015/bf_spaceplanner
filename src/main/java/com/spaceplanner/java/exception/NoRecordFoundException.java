package com.spaceplanner.java.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: ashif
 * Date: 9/20/14
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoRecordFoundException extends RuntimeException {

    private static final long serialVersionUID = -2706518848253209769L;
    private Logger logger = LoggerFactory.getLogger(NoRecordFoundException.class);

    private String message;

    public NoRecordFoundException() {
        super();
        logger.error("NoRecordFoundException occurred");
    }

    public NoRecordFoundException(String message) {
        super(message);
        logger.error("NoRecordFoundException occured "+message);
    }

    public NoRecordFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.error("NoRecordFoundException occurred "+message);
        cause.printStackTrace();
    }
}

