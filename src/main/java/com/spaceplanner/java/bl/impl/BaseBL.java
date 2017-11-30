package com.spaceplanner.java.bl.impl;

import com.spaceplanner.java.exception.SpacePlannerResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Ashif.Qureshi
 * Date: 9/19/14
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseBL {


    protected void saveErrorMessage(SpacePlannerResponseStatus status, int code) {
        status.setCode(code);
    }

    protected void saveSuccessMessage(SpacePlannerResponseStatus status, String message) {
        status.setCode(HttpStatus.OK.value());
        status.setMessage(message);
    }

    protected void saveSuccessMessage(SpacePlannerResponseStatus status) {
        status.setCode(HttpStatus.OK.value());
    }
    
}
