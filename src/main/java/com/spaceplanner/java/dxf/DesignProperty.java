package com.spaceplanner.java.dxf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DesignProperty {

    @Value("${layer.brand.line}")
    private String brandLine;
    @Value("${layer.brand.name}")
    private String brandName;
    @Value("${layer.brand.area}")
    private String brandArea;
    @Value("${layer.category.line}")
    private String categoryLine;
    @Value("${layer.category.name}")
    private String categoryName;
    @Value("${layer.location.code}")
    private String locationCode;
    @Value("${layer.lwpolyline}")
    private String lWPolyLine;
    @Value("${layer.text.mtext}")
    private String mText;
    @Value("${layer.text.text}")
    private String text;

    public String getBrandLine() {
        return brandLine;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getBrandArea() {
        return brandArea;
    }

    public String getCategoryLine() {
        return categoryLine;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public String getlWPolyLine() {
        return lWPolyLine;
    }

    public String getmText() {
        return mText;
    }

    public String getText() {
        return text;
    }
}
