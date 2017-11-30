package com.spaceplanner.java.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 31/10/15
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class LayerNotFoundException extends java.lang.RuntimeException{


    private static final long serialVersionUID = -2706518848253209769L;
    private Logger logger = LoggerFactory.getLogger(LayerNotFoundException.class);

    private String message;

    public LayerNotFoundException() {
        super();
        logger.error("Layer not found");
    }

    public LayerNotFoundException(String message) {
        super(message);
        logger.error("Layer not found exception: " + message);
    }

    public LayerNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.error("Layer not found exception: " + message);
        cause.printStackTrace();
    }
}
