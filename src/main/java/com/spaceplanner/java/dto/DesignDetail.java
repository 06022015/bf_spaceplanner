package com.spaceplanner.java.dto;

public class DesignDetail {

    private String brand;
    private String category;
    private String location;
    private Double area;

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

    @Override
    public String toString() {
        return "DesignDetail{" +
                "brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", area=" + area +
                '}';
    }
}
