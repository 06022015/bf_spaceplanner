package com.spaceplanner.java.model;

import com.spaceplanner.java.model.type.Status;

import javax.persistence.Embeddable;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 23/07/15
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Embeddable
public class FloorDesign{

    private String autoCADFileName;
    private String pdfFileName;
    private int noOfLocation;
    private Double designArea;
    private int version;
    private Status status=Status.ACTIVE;
    private UserEntity designCreatedBy;
    private UserEntity designUpdateBy;
}
