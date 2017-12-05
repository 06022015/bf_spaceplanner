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
    private Double area;
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

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpaceMasterDTO that = (SpaceMasterDTO) o;

        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (brandCode != null ? !brandCode.equals(that.brandCode) : that.brandCode != null) return false;
        return brandName != null ? brandName.equals(that.brandName) : that.brandName == null;
    }

    @Override
    public int hashCode() {
        int result = category != null ? category.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (brandCode != null ? brandCode.hashCode() : 0);
        result = 31 * result + (brandName != null ? brandName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SpaceMasterDTO{" +
                "division='" + division + '\'' +
                ", category='" + category + '\'' +
                ", runningFtWall='" + runningFtWall + '\'' +
                ", sisDetails='" + sisDetails + '\'' +
                ", location='" + location + '\'' +
                ", brandCode='" + brandCode + '\'' +
                ", brandName='" + brandName + '\'' +
                ", area='" + area + '\'' +
                ", MG='" + MG + '\'' +
                ", PSFPD='" + PSFPD + '\'' +
                ", sales='" + sales + '\'' +
                ", GMV='" + GMV + '\'' +
                ", agreementMargin='" + agreementMargin + '\'' +
                ", vistexMargin='" + vistexMargin + '\'' +
                ", GMROF='" + GMROF + '\'' +
                ", securityDeposit=" + securityDeposit +
                ", sdAmount=" + sdAmount +
                '}';
    }
}
