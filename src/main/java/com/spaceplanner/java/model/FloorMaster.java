package com.spaceplanner.java.model;

import javax.persistence.Embeddable;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 23/07/15
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Embeddable
public class FloorMaster{

    private String floorNumber;
    private String floorCode;
    private Double totalArea;
    private Double chargeableArea;
    private Double carpetArea;
    private Double masterRetailArea;
    private UserEntity masterCreatedBy;
    private UserEntity masterUpdateBy;

}
