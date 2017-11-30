package com.spaceplanner.java.model.type;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 14/08/15
 * Time: 12:21 AM
 * To change this template use File | Settings | File Templates.
 */
public enum DesignStatus {

   Master_Created, Master_Published, Space_Design_Uploaded,  Space_Design_Published,
    Brand_Master_Uploaded, Brand_Master_Published, Brand_Design_Uploaded, Brand_Design_published,
   /* Enrichment_Uploaded,*/ Published,CICO, BIBO, Archived,;


    private int index;

    public int getIndex() {
        this.index = this.ordinal();
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /*NO_DESIGN, MASTER_PUBLISHED, SPACE_DESIGN_UPLOAD, SPACE_DESIGN_REJECTED, SPACE_DESIGN_PUBLISHED,
   BRAND_MASTER_UPLOADED, BRAND_DESIGN_UPLOADED, BRAND_DESIGN_REJECTED, BRAND_DESIGN_PUBLISHED,ENRICHMENT_UPLOADED,
   FINAL_PUBLISHED, ARCHIVED*/
}
