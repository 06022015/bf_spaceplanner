package com.spaceplanner.java.dto;

public class DesignDetail {

    private String brand;
    private String category;
    private String location;
    private Double area;
    private String lineId;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DesignDetail that = (DesignDetail) o;

        if (brand != null ? !brand.equals(that.brand) : that.brand != null) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        return lineId != null ? lineId.equals(that.lineId) : that.lineId == null;
    }

    @Override
    public int hashCode() {
        int result = brand != null ? brand.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (lineId != null ? lineId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DesignDetail{" +
                "brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", area=" + area +
                ", lineId=" + lineId +
                '}';
    }
}
