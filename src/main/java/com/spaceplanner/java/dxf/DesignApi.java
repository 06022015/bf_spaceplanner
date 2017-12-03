package com.spaceplanner.java.dxf;

import com.spaceplanner.java.dto.DesignDetail;

import java.util.List;

public interface DesignApi {

    static String BRAND_LINE = "I-BRAND_LINE";
    //static String BRAND_NAME = "I-BRAND_NAME";
    static String BRAND_NAME = "I-BRAND_TEXT";
    static String BRAND_AREA = "I-BRAND_AREA";
    static String LOCATION = "I-LOCATION CODE";
    static String CATEGORY_LINE = "I-CAT LINE";
    static String CATEGORY_NAME = "I-CAT TEXT";
    static String LWPOLYLINE = "LWPOLYLINE";
    static String MTEXT = "MTEXT";

    List<DesignDetail> getDesignDetails();

    List<DesignDetail> getCategoryDesignDetails(String category);
}
