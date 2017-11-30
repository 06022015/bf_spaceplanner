package com.spaceplanner.java.model;

import com.spaceplanner.java.model.master.BrandEntity;
import com.spaceplanner.java.model.master.CategoryDivision;
import com.spaceplanner.java.model.type.DesignStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 14/08/15
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name = "floor_design_details")
public class FloorDesignDetailsEntity extends BaseEntity{

    private Long id;
    private String division;
    private CategoryDivision categoryDivision;
    private String category;
    private String designRunningFtWall;
    private String sisDetails;
    private String locationCode;
    private String designBrandName;
    /*private String designBrandCode;*/
    private Double designArea;
    /*private String masterBrandName;
    private String masterBrandCode;*/
    private String MG;
    private String PSFPD;
    private String sales;
    private String GMV;
    private String agreementMargin;
    private String vistexMargin;
    private String GMROF;
    private Double securityDeposit;
    private Double sdAmount;
    private String remarks;
    private FloorEntity floor;
    private boolean valid=true;
    private BrandEntity brand;
    private Date startDate = new Date();
    private boolean requested = false;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    @ManyToOne()
    public CategoryDivision getCategoryDivision() {
        return categoryDivision;
    }

    public void setCategoryDivision(CategoryDivision categoryDivision) {
        this.categoryDivision = categoryDivision;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesignRunningFtWall() {
        return designRunningFtWall;
    }

    public void setDesignRunningFtWall(String designRunningFtWall) {
        this.designRunningFtWall = designRunningFtWall;
    }

    public String getSisDetails() {
        return sisDetails;
    }

    public void setSisDetails(String sisDetails) {
        this.sisDetails = sisDetails;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getDesignBrandName() {
        return designBrandName;
    }

    public void setDesignBrandName(String designBrandName) {
        this.designBrandName = designBrandName;
    }

    /*public String getDesignBrandCode() {
        return designBrandCode;
    }

    public void setDesignBrandCode(String designBrandCode) {
        this.designBrandCode = designBrandCode;
    }*/

    public Double getDesignArea() {
        return designArea;
    }

    public void setDesignArea(Double designArea) {
        this.designArea = designArea;
    }

    /*public String getMasterBrandName() {
        return masterBrandName;
    }

    public void setMasterBrandName(String masterBrandName) {
        this.masterBrandName = masterBrandName;
    }

    public String getMasterBrandCode() {
        return masterBrandCode;
    }

    public void setMasterBrandCode(String masterBrandCode) {
        this.masterBrandCode = masterBrandCode;
    }*/

    public String getMG() {
        return MG;
    }

    public void setMG(String MG) {
        this.MG = MG;
    }

    public String getPSFPD() {
        return PSFPD;
    }

    public void setPSFPD(String PSFPD) {
        this.PSFPD = PSFPD;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getGMV() {
        return GMV;
    }

    public void setGMV(String GMV) {
        this.GMV = GMV;
    }

    public String getAgreementMargin() {
        return agreementMargin;
    }

    public void setAgreementMargin(String agreementMargin) {
        this.agreementMargin = agreementMargin;
    }

    public String getVistexMargin() {
        return vistexMargin;
    }

    public void setVistexMargin(String vistexMargin) {
        this.vistexMargin = vistexMargin;
    }

    public String getGMROF() {
        return GMROF;
    }

    public void setGMROF(String GMROF) {
        this.GMROF = GMROF;
    }

    public Double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(Double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public Double getSdAmount() {
        return sdAmount;
    }

    public void setSdAmount(Double sdAmount) {
        this.sdAmount = sdAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    @ManyToOne()
    public FloorEntity getFloor() {
        return floor;
    }

    public void setFloor(FloorEntity floor) {
        this.floor = floor;
    }


    @ManyToOne(optional = true)
    public BrandEntity getBrand() {
        return brand;
    }

    public void setBrand(BrandEntity brand) {
        this.brand = brand;
    }

    @Column(name = "start_date")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(name = "is_requested")
    public boolean isRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    @Transient
    public boolean isValid() {
        if(getFloor().getDesignStatus().equals(DesignStatus.Brand_Design_Uploaded)){
            this.valid = null==getBrand() && (null==getDesignBrandName() || "".equals(getDesignBrandName()));
            if(!this.valid){
                this.valid=null!=getBrand() && null!=getDesignBrandName() && !"".equals(getDesignBrandName())
                        && getDesignBrandName().equals(getBrand().getName());
            }
        }
        return valid;
    }

}
