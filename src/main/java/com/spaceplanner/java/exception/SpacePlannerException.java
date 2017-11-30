package com.spaceplanner.java.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Ashif.Qureshi
 * Date: 9/18/14
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpacePlannerException extends RuntimeException {

    private static final long serialVersionUID = -2706518848253209769L;
    private Logger logger = LoggerFactory.getLogger(SpacePlannerException.class);

    private int code;
    private String message;

    public SpacePlannerException() {
        super();
        logger.error("SpacePlannerException occurred");
    }

    public SpacePlannerException(int code) {
        super();
        this.code = code;
        this.message = HttpStatus.valueOf(code).toString();
        logger.error("SpacePlannerException occurred code:- "+ code);
    }

    public SpacePlannerException(String message) {
        super(message);
        this.code=HttpStatus.INTERNAL_SERVER_ERROR.value();
        logger.error("SpacePlannerException occured "+message);
    }

    public SpacePlannerException(int code, String message) {
        super(message);
        this.code = code;
        logger.error("SpacePlannerException occurred code "+code+" | Message "+message);
    }

    public SpacePlannerException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code= code;
        logger.error("SpacePlannerException occurred "+message);
        cause.printStackTrace();
    }

    public int getCode() {
        return code;
    }
}
