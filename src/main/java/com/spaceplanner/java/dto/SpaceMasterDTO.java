package com.spaceplanner.java.dto;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 29/09/15
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpaceMasterDTO {
    
    private String division;
    private String category;
    private String runningFtWall;
    private String sisDetails;
    private String location;
    private String brandCode;
    private String brandName;
    private String area;
    private String MG;
    private String PSFPD;
    private String sales;
    private String GMV;
    private String agreementMargin;
    private String vistexMargin;
    private String GMROF;
    private Double securityDeposit;
    private Double sdAmount;

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getRunningFtWall() {
        return runningFtWall;
    }

    public void setRunningFtWall(String runningFtWall) {
        this.runningFtWall = runningFtWall;
    }

    public String getSisDetails() {
        return sisDetails;
    }

    public void setSisDetails(String sisDetails) {
        this.sisDetails = sisDetails;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

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


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpaceMasterDTO that = (SpaceMasterDTO) o;

        if (location != null ? !location.equals(that.location) : that.location != null) return false;

        return true;
    }


    public int hashCode() {
        return location != null ? location.hashCode() : 0;
    }
}
