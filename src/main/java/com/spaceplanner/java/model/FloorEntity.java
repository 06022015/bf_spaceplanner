package com.spaceplanner.java.model;

import com.spaceplanner.java.model.type.DesignStatus;
import com.spaceplanner.java.model.type.Status;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 13/09/15
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name = "floor")
public class FloorEntity extends BaseEntity{


    private Long id;
    private String floorNumber;
    private String floorCode;
    /*private Double chargeableArea;*/
    private Double carpetArea;
    private Double retailArea;
    private UserEntity createdBy;
    private UserEntity updateBy;
    private String autoCADFileName;
    private String pdfFileName;
    private int noOfLocation;
    private Double designArea;
    private int version=1;
    private UserEntity designCreatedBy;
    private UserEntity designUpdateBy;
    private StoreEntity store;
    private Status status = Status.ACTIVE;
    private DesignStatus designStatus = DesignStatus.Master_Created;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne()
    public StoreEntity getStore() {
        return store;
    }

    public void setStore(StoreEntity store) {
        this.store = store;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getFloorCode() {
        return floorCode;
    }

    public void setFloorCode(String floorCode) {
        this.floorCode = floorCode;
    }

    /*public Double getChargeableArea() {
        return chargeableArea;
    }

    public void setChargeableArea(Double chargeableArea) {
        this.chargeableArea = chargeableArea;
    }*/

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

    @ManyToOne(fetch = FetchType.LAZY)
    public UserEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserEntity createdBy) {
        this.createdBy = createdBy;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public UserEntity getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(UserEntity updateBy) {
        this.updateBy = updateBy;
    }

    public String getAutoCADFileName() {
        return autoCADFileName;
    }

    public void setAutoCADFileName(String autoCADFileName) {
        this.autoCADFileName = autoCADFileName;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public int getNoOfLocation() {
        return noOfLocation;
    }

    public void setNoOfLocation(int noOfLocation) {
        this.noOfLocation = noOfLocation;
    }

    public Double getDesignArea() {
        return designArea;
    }

    public void setDesignArea(Double designArea) {
        this.designArea = designArea;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public UserEntity getDesignCreatedBy() {
        return designCreatedBy;
    }

    public void setDesignCreatedBy(UserEntity designCreatedBy) {
        this.designCreatedBy = designCreatedBy;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public UserEntity getDesignUpdateBy() {
        return designUpdateBy;
    }

    public void setDesignUpdateBy(UserEntity designUpdateBy) {
        this.designUpdateBy = designUpdateBy;
    }

    @Enumerated
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Enumerated
    public DesignStatus getDesignStatus() {
        return designStatus;
    }

    public void setDesignStatus(DesignStatus designStatus) {
        this.designStatus = designStatus;
    }


    public void update(FloorEntity floor){
        setFloorNumber(floor.getFloorNumber());
        setCarpetArea(floor.getCarpetArea());
        /*setChargeableArea(floor.getChargeableArea());*/
        setRetailArea(floor.getRetailArea());
        this.setUpdatedAt(new Date());
    }

    public void copyTo(FloorEntity floorEntity){
        floorEntity.setFloorCode(getFloorCode());
        floorEntity.setFloorNumber(getFloorNumber());
        floorEntity.setCarpetArea(getCarpetArea());
        /*floorEntity.setChargeableArea(getChargeableArea());*/
        floorEntity.setRetailArea(getRetailArea());
        floorEntity.setCreatedBy(getCreatedBy());
        floorEntity.setCreatedAt(getCreatedAt());
        floorEntity.setUpdatedAt(new Date());
        floorEntity.setAutoCADFileName(getAutoCADFileName());
        floorEntity.setDesignArea(getDesignArea());
        floorEntity.setNoOfLocation(getNoOfLocation());
        floorEntity.setDesignCreatedBy(getDesignCreatedBy());
        floorEntity.setDesignUpdateBy(getDesignUpdateBy());
        floorEntity.setPdfFileName(getPdfFileName());
        floorEntity.setStatus(Status.ACTIVE);
        floorEntity.setStore(getStore());
        floorEntity.setVersion(getVersion() + 1);
        floorEntity.setDesignStatus(DesignStatus.Brand_Master_Published);
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FloorEntity that = (FloorEntity) o;

        if (noOfLocation != that.noOfLocation) return false;
        if (version != that.version) return false;
        if (autoCADFileName != null ? !autoCADFileName.equals(that.autoCADFileName) : that.autoCADFileName != null)
            return false;
        if (carpetArea != null ? !carpetArea.equals(that.carpetArea) : that.carpetArea != null) return false;
        /*if (chargeableArea != null ? !chargeableArea.equals(that.chargeableArea) : that.chargeableArea != null)
            return false;*/
        if (createdBy != null ? !createdBy.equals(that.createdBy) : that.createdBy != null) return false;
        if (designArea != null ? !designArea.equals(that.designArea) : that.designArea != null) return false;
        if (designCreatedBy != null ? !designCreatedBy.equals(that.designCreatedBy) : that.designCreatedBy != null)
            return false;
        if (designStatus != that.designStatus) return false;
        if (designUpdateBy != null ? !designUpdateBy.equals(that.designUpdateBy) : that.designUpdateBy != null)
            return false;
        if (floorCode != null ? !floorCode.equals(that.floorCode) : that.floorCode != null) return false;
        if (floorNumber != null ? !floorNumber.equals(that.floorNumber) : that.floorNumber != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (pdfFileName != null ? !pdfFileName.equals(that.pdfFileName) : that.pdfFileName != null) return false;
        if (retailArea != null ? !retailArea.equals(that.retailArea) : that.retailArea != null) return false;
        if (status != that.status) return false;
        if (store != null ? !store.equals(that.store) : that.store != null) return false;
        if (updateBy != null ? !updateBy.equals(that.updateBy) : that.updateBy != null) return false;

        return true;
    }


    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (floorNumber != null ? floorNumber.hashCode() : 0);
        result = 31 * result + (floorCode != null ? floorCode.hashCode() : 0);
        /*result = 31 * result + (chargeableArea != null ? chargeableArea.hashCode() : 0);*/
        result = 31 * result + (carpetArea != null ? carpetArea.hashCode() : 0);
        result = 31 * result + (retailArea != null ? retailArea.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (updateBy != null ? updateBy.hashCode() : 0);
        result = 31 * result + (autoCADFileName != null ? autoCADFileName.hashCode() : 0);
        result = 31 * result + (pdfFileName != null ? pdfFileName.hashCode() : 0);
        result = 31 * result + noOfLocation;
        result = 31 * result + (designArea != null ? designArea.hashCode() : 0);
        result = 31 * result + version;
        result = 31 * result + (designCreatedBy != null ? designCreatedBy.hashCode() : 0);
        result = 31 * result + (designUpdateBy != null ? designUpdateBy.hashCode() : 0);
        result = 31 * result + (store != null ? store.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (designStatus != null ? designStatus.hashCode() : 0);
        return result;
    }
}
