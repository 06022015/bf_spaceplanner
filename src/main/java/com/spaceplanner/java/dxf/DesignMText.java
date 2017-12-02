package com.spaceplanner.java.dxf;

import org.kabeja.dxf.DXFMText;
import org.kabeja.dxf.DXFPoint;
import org.kabeja.dxf.helpers.StyledTextParagraph;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 03/11/15
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DesignMText implements Comparator<DesignMText> {

    private String id;
    private String text="";
    private DXFPoint dxfPoint;
    private Double textAsArea;
    private boolean hasCatLine=false;
    private boolean hasBrandLine=false;
    private TextType textType;

    public DesignMText(DXFMText dxfmText, TextType textType){
        this.id = dxfmText.getID();
        this.dxfPoint=new DXFPoint(dxfmText.getInsertPoint());
        Iterator textIterator = dxfmText.getTextDocument().getStyledParagraphIterator();
        this.textType = textType;
        while(textIterator.hasNext()){
            StyledTextParagraph styledTextParagraph = (StyledTextParagraph)textIterator.next();
            if(null!=styledTextParagraph.getText() && !"".equals(styledTextParagraph.getText())){
                //this.text = text+styledTextParagraph.getText().trim();
                if(isArea() &&  styledTextParagraph.getText().toLowerCase().contains("sft")){
                    try{
                        this.text = text+styledTextParagraph.getText().trim();
                        this.textAsArea = Double.parseDouble(styledTextParagraph.getText().replaceAll("(?i)sft","").trim());
                    }catch (NumberFormatException ne){
                        System.out.println("Unable to parse area:- "+ styledTextParagraph.getText());
                    }
                }else if(!isArea() && !styledTextParagraph.getText().toLowerCase().contains("sft")){
                    this.text = text+styledTextParagraph.getText().trim();
                }
            }
        }
    }

    public boolean isValid(){
        boolean isValid = null!= this.text && !"".equals(text) && null!=this.dxfPoint;
        if(isArea() && isValid){
           isValid = null!= textAsArea;
        }
        return isValid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DXFPoint getDxfPoint() {
        return dxfPoint;
    }

    public void setDxfPoint(DXFPoint dxfPoint) {
        this.dxfPoint = dxfPoint;
    }

    public Double getTextAsArea() {
        return textAsArea;
    }

    public void setTextAsArea(Double textAsArea) {
        this.textAsArea = textAsArea;
    }

    public boolean isHasCatLine() {
        return hasCatLine;
    }

    public void setHasCatLine(boolean hasCatLine) {
        this.hasCatLine = hasCatLine;
    }

    public boolean isHasBrandLine() {
        return hasBrandLine;
    }

    public void setHasBrandLine(boolean hasBrandLine) {
        this.hasBrandLine = hasBrandLine;
    }

    public boolean isArea() {
        return null != textType && textType.equals(TextType.BRAND_AREA);
    }

    public TextType getTextType() {
        return textType;
    }

    public void setTextType(TextType textType) {
        this.textType = textType;
    }

    public int compare(DesignMText o1, DesignMText o2) {
        return o1.getText().compareTo(o2.getText());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DesignMText that = (DesignMText) o;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return true;
    }

    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }

    public String toString() {
        return "DesignMText{" +
                "text='" + text + '\'' +
                ", dxfPoint: X= " + dxfPoint.getX()+",   Y= "+dxfPoint.getY() +
                '}';
    }
}
