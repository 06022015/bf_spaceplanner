package com.spaceplanner.java.model;

import com.spaceplanner.java.model.type.Status;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 23/07/15
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name = "store")
public class StoreEntity extends BaseEntity{

    private Long id;
    private String name;
    private String code;
    private int  noOfFloor;
    /*private Double totalArea;*/
    private Double chargeableArea;
    private Double carpetArea;
    private Double retailArea;
    private String apparelGMROF;
    private String lifeStyleGMROF;
    private String storeGMROF;
    private String projectedStoreSale;
    private Address address= new Address();
    private Status status= Status.ACTIVE;
    private UserEntity createBy;


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNoOfFloor() {
        return noOfFloor;
    }

    public void setNoOfFloor(int noOfFloor) {
        this.noOfFloor = noOfFloor;
    }

    /*public Double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(Double totalArea) {
        this.totalArea = totalArea;
    }*/

    public Double getChargeableArea() {
        return chargeableArea;
    }

    public void setChargeableArea(Double chargeableArea) {
        this.chargeableArea = chargeableArea;
    }

    public Double getCarpetArea() {
        return carpetArea;
    }

    public void setCarpetArea(Double carpetArea) {
        this.carpetArea = carpetArea;
    }

    public Double getRetailArea() {
        return retailArea;
    }

    public void setRetailArea(Double retailArea) {
        this.retailArea = retailArea;
    }

    public String getApparelGMROF() {
        return apparelGMROF;
    }

    public void setApparelGMROF(String apparelGMROF) {
        this.apparelGMROF = apparelGMROF;
    }

    public String getLifeStyleGMROF() {
        return lifeStyleGMROF;
    }

    public void setLifeStyleGMROF(String lifeStyleGMROF) {
        this.lifeStyleGMROF = lifeStyleGMROF;
    }

    public String getStoreGMROF() {
        return storeGMROF;
    }

    public void setStoreGMROF(String storeGMROF) {
        this.storeGMROF = storeGMROF;
    }

    public String getProjectedStoreSale() {
        return projectedStoreSale;
    }

    public void setProjectedStoreSale(String projectedStoreSale) {
        this.projectedStoreSale = projectedStoreSale;
    }

    @Embedded
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Enumerated
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public UserEntity getCreateBy() {
        return createBy;
    }

    public void setCreateBy(UserEntity createBy) {
        this.createBy = createBy;
    }

    public void update(StoreEntity store){
        this.code=store.getCode();
        this.name=store.getName();
        this.noOfFloor=store.getNoOfFloor();
        /*this.totalArea=store.getTotalArea();*/
        this.chargeableArea=store.getChargeableArea();
        this.carpetArea=store.getCarpetArea();
        this.retailArea=store.getRetailArea();
        this.apparelGMROF=store.getApparelGMROF();
        this.lifeStyleGMROF=store.getLifeStyleGMROF();
        this.storeGMROF=store.getStoreGMROF();
        this.projectedStoreSale=store.getProjectedStoreSale();
        this.address=store.getAddress();
        this.setUpdatedAt(new Date());
    }
}
