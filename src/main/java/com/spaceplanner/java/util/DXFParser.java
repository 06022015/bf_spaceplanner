package com.spaceplanner.java.util;

import com.spaceplanner.java.dto.DesignMText;
import com.spaceplanner.java.dto.DesignPolyLine;
import com.spaceplanner.java.exception.LayerNotFoundException;
import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFLWPolyline;
import org.kabeja.dxf.DXFLayer;
import org.kabeja.dxf.DXFMText;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;

import java.io.InputStream;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 03/11/15
 * Time: 11:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class DXFParser {

    private static String BRAND_LINE = "Brand_lines";
    //private static String BRAND_AREA = "BRAND AREA";
    //private static String BRAND_AREA = "AREA-BRAND";
    private static String BRAND_AREA = "Brand_Areas";
    private static String DEPARTMENT = "Location code";
    private static String LWPOLYLINE = "LWPOLYLINE";
    private static String MTEXT = "MTEXT";
    private static String CAT_HATCH = "Cat Hatch";
    private static String CATEGORY_NAME = "Text Brands";

    private DXFDocument dxfDocument = null;

    private List<DesignPolyLine> brandLineList=null;

    private List<DesignMText> departmentList=null;

    private List<DesignPolyLine> categoryLineList =null;
    
    private List<String> departments = new ArrayList<String>();


    public DXFParser(InputStream inputStream) throws ParseException {
        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse(inputStream, org.kabeja.parser.DXFParser.DEFAULT_ENCODING);
        dxfDocument = parser.getDocument();
    }

    public DXFParser(String filePath) throws ParseException {
        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse(filePath, org.kabeja.parser.DXFParser.DEFAULT_ENCODING);
        this.dxfDocument = parser.getDocument();
    }


    public DXFDocument getDxfDocument() {
        return dxfDocument;
    }

    private List<DesignPolyLine> getBrandLineList() {
        if(null==brandLineList){
            this.brandLineList = getLineLayer(BRAND_LINE);
        }
        return brandLineList;
    }

    private List<DesignMText> getDepartmentList() {
        if(null==departmentList){
            this.departmentList = getDepartment();
        }
        return departmentList;
    }

    public List<String> getDepartments() {
        if(null==departments){
            getDepartment();
        }
        return departments;
    }

    private List<DesignPolyLine> getCategoryLineList() {
        if(null==categoryLineList){
            this.categoryLineList = getLineLayer(CAT_HATCH);
        }
        return categoryLineList;
    }


    private List<DesignPolyLine> getLineLayer(String layerName) {
        DXFLayer layer = getDXFLayer(layerName);
        List<DXFLWPolyline> lwPolyLineList = layer.getDXFEntities(LWPOLYLINE);
        List<DesignPolyLine> designPolyLineList = new ArrayList<DesignPolyLine>();
        for (DXFLWPolyline dxflwPolyline : lwPolyLineList) {
            DesignPolyLine designPolyLine = new DesignPolyLine(dxflwPolyline);
            if(!designPolyLineList.contains(designPolyLine))
            designPolyLineList.add(new DesignPolyLine(dxflwPolyline));
        }
        return designPolyLineList;
    }

    private List<DesignMText> getDepartment() {
        DXFLayer layer = getDXFLayer(DEPARTMENT);
        List<DXFMText> mTextList = layer.getDXFEntities(MTEXT);
        List<DesignMText> departmentList = new ArrayList<DesignMText>();
        for (DXFMText dxfmText : mTextList) {
            DesignMText department = new DesignMText(dxfmText, false);
            if (department.isValid() && !departmentList.contains(department)) {
                departmentList.add(department);
                departments.add(department.getText());
            }
        }
        Collections.sort(departmentList, new DesignMText());
        Collections.sort(departments);
        return departmentList;
    }


    private List<DesignMText> getBrandArea(){
        DXFLayer layer = getDXFLayer(BRAND_AREA);
        List<DXFMText> mtextBList = layer.getDXFEntities(MTEXT);
        List<DesignMText> brandAreaList = new ArrayList<DesignMText>();
        for (DXFMText dxfmText : mtextBList) {
            DesignMText brandArea = new DesignMText(dxfmText, true);
            if (brandArea.isValid())
                brandAreaList.add(brandArea);
        }
        return brandAreaList;
    }

    private List<DesignMText> getBrandName(){
        DXFLayer layer = getDXFLayer(BRAND_AREA);
        List<DXFMText> mtextBList = layer.getDXFEntities(MTEXT);
        List<DesignMText> brandNameList = new ArrayList<DesignMText>();
        for (DXFMText dxfmText : mtextBList) {
            DesignMText brandCodeName = new DesignMText(dxfmText,false);
            if (brandCodeName.isValid())
                brandNameList.add(brandCodeName);
        }
        return brandNameList;
    }

    private List<DesignMText> getDesignMText(String layerName){
        DXFLayer layer = getDXFLayer(layerName);
        List<DXFMText> mtextBList = layer.getDXFEntities(MTEXT);
        List<DesignMText> designMTextList = new ArrayList<DesignMText>();
        for (DXFMText dxfmText : mtextBList) {
            DesignMText designMText = new DesignMText(dxfmText,false);
            if (designMText.isValid())
                designMTextList.add(designMText);
        }
        return designMTextList;
    }

    private List<DesignMText> getCategory(){
        DXFLayer layer = getDXFLayer(CATEGORY_NAME);
        List<DXFMText> mtextBList = layer.getDXFEntities(MTEXT);
        List<DesignMText> categoryList = new ArrayList<DesignMText>();
        for (DXFMText dxfmText : mtextBList) {
            DesignMText category = new DesignMText(dxfmText, false);
            if (category.isValid())
                categoryList.add(category);
        }
        return categoryList;
    }


    private DXFLayer getDXFLayer(String layerName){
        DXFLayer layer=null;
        if(dxfDocument.containsDXFLayer(layerName.toUpperCase())){
            layer=dxfDocument.getDXFLayer(layerName.toUpperCase());
        }else if(dxfDocument.containsDXFLayer(layerName.toLowerCase())){
            layer = dxfDocument.getDXFLayer(layerName.toLowerCase());
        }else {
            Iterator layerIterator = dxfDocument.getDXFLayerIterator();
            boolean isFound=false;
            while(layerIterator.hasNext()){
                layer = (DXFLayer)layerIterator.next();
                if(layerName.equalsIgnoreCase(layer.getName())){
                    isFound=!isFound;
                    break;
                }
            }
            if(!isFound){
                throw new LayerNotFoundException("Layer \""+layerName+"\" does not exist");
            }
        }
        return layer;
    }

    public Map<String,Double> getLocationAreaMap(){
        Map<String, Double> locationAreaMap = new HashMap<String, Double>();
        List<DesignPolyLine> brandLines = getBrandLineList();
        List<DesignMText> departmentList = getDepartmentList();
        List<DesignMText> brandAreaList = getBrandArea();
        for (DesignMText department : departmentList) {
            boolean isValid=false;
            for (DesignMText brandArea : brandAreaList) {
                if (!brandArea.isHasDepartment()) {
                    for (DesignPolyLine designPolyLine : brandLines) {
                        //if (designPolyLine.validatePoint(department.getDxfPoint(), brandArea.getDxfPoint())) {
                        if (designPolyLine.isValid(department.getDxfPoint()) && designPolyLine.isValid(brandArea.getDxfPoint())) {
                            locationAreaMap.put(department.getText(), brandArea.getTextAsArea());
                            brandArea.setHasDepartment(true);
                            isValid=true;
                            break;
                        }
                    }
                }
                if(isValid)
                    break;
            }
        }
        return locationAreaMap;
    }

    public Map<String, String>  getLocationBrandNameMap(){
        Map<String, String> locationBrandNameMap = new HashMap<String, String>();
        List<DesignPolyLine> brandLines = getBrandLineList();
        List<DesignMText> departmentList = getDepartmentList();
        List<DesignMText> brandCodeNameList = getBrandName();
        for (DesignMText department : departmentList) {
            boolean isValid=false;
            for (DesignMText brandName : brandCodeNameList) {
                if (!brandName.isHasDepartment()) {
                    for (DesignPolyLine designPolyLine : brandLines) {
                        //if (designPolyLine.validatePoint(department.getDxfPoint(), brandName.getDxfPoint())) {
                        if (designPolyLine.isValid(department.getDxfPoint()) && designPolyLine.isValid(brandName.getDxfPoint())) {
                            locationBrandNameMap.put(department.getText(), brandName.getText());
                            brandName.setHasDepartment(true);
                            isValid=true;
                            break;
                        }
                    }
                }
                if(isValid)
                    break;
            }
        }
        return locationBrandNameMap;
    }

    public Map<String, String> getLocationCategory(){
        Map<String, String> locationCategoryMap = new HashMap<String, String>();
        List<DesignPolyLine> categoryLines = getCategoryLineList();
        List<DesignMText> departmentList = getDepartmentList();
        List<DesignMText> categoryList = getCategory();
        for (DesignMText department : departmentList) {
            boolean isValid=false;
            for (DesignMText category : categoryList) {
                for (DesignPolyLine designPolyLine : categoryLines) {
                    if (designPolyLine.isValid(department.getDxfPoint()) && designPolyLine.isValid(category.getDxfPoint())) {
                        locationCategoryMap.put(department.getText(), category.getText());
                        isValid=true;
                        break;
                    }
                }
                if(isValid)
                    break;
            }
        }
        return locationCategoryMap;
    }

    
    private List<DesignPolyLine> getCategoryLineText(){
        List<DesignPolyLine> designPolyLineList = getCategoryLineList();
        Collections.sort(designPolyLineList, new DesignPolyLine());
        List<DesignMText> categoryTextList = getCategory();
        for(DesignPolyLine designPolyLine :  designPolyLineList){
            for(DesignMText designMText : categoryTextList){
                if(designPolyLine.isValid(designMText.getDxfPoint())){
                    designPolyLine.setText(designMText.getText());
                }
            }
        }

        return designPolyLineList;
    }
    
    /*private List<DesignPolyLine> defineLineLevel(List<DesignPolyLine> designPolyLineList){
        for(DesignPolyLine designPolyLine : designPolyLineList){
           for(DesignPolyLine line :  designPolyLineList){
               if(!line.equals(designPolyLine) && line.isValid(designPolyLine.getX(), designPolyLine.getY())){
                 designPolyLine.setLevel(line.getLevel()+1);
               }
           }
        }
        Collections.sort(designPolyLineList, Collections.reverseOrder(new DesignPolyLine.LevelComparator()));
        return designPolyLineList;
    }*/

    public static void main(String args[]) throws ParseException {
        //DXFParser dxfParser = new DXFParser("/Users/ashifqureshi/project/home-pc/space_planner/doc/autocad file.dxf");
        //DXFParser dxfParser = new DXFParser("/Users/ashifqureshi/config/Desktop/CENTRAL-ROHINI.dxf");
        //DXFParser dxfParser = new DXFParser("/Users/ashifqureshi/project/home-pc/space_planner/doc/Central/ROHINI-DXF/ROHINI-CENTRAL-GROUND-FLOOR.dxf");
        //DXFParser dxfParser = new DXFParser("/Users/ashifqureshi/project/home-pc/space_planner/doc/indore/CNT-BC4.dxf");

        //DXFParser dxfParser = new DXFParser("/Users/ashqures/project/home-pc/space_planner/doc/design/CNT-BC4-GF_171115.dxf");
        DXFParser dxfParser = new DXFParser("/Users/ashqures/project/home-pc/space_planner/doc/design/CNT-BC4-GF-30-12-15.dxf");
        //DXFParser dxfParser = new DXFParser("/Users/ashifqureshi/project/home-pc/space_planner/doc/Nagpur/NAGPURCNT-2F.dxf");
        Map<String, Double> locationAreaMap = dxfParser.getLocationAreaMap();
        Map<String, String> locationBrandNameMap = dxfParser.getLocationBrandNameMap();
        Map<String, String> locationCategoryMap = dxfParser.getLocationCategory();

        int dCount1 = 1;
        List<DesignMText> departments = dxfParser.getDepartmentList();
        Collections.sort(departments, new DesignMText());
        for(DesignMText department : departments){
            //System.out.println(dCount1 + "\t Location:- " + department.getText());
            dCount1++;
        }
        System.out.println("\n\n\n\n\n");
        int dCount2 = 1;
        for(DesignMText department : departments){
            System.out.println(dCount2 + "\t Location:- " + department.getText()+" and Area:- "+ locationAreaMap.get(department.getText()));
            dCount2++;
        }
        System.out.println("\n\n\n\n\n");
        int dCount3 = 1;
        for (DesignMText department : departments) {
            System.out.println(dCount3 + "\t Location:- " + department.getText()+" and Brand Name:- "+ locationBrandNameMap.get(department.getText()));
            dCount3++;
        }
        System.out.println("\n\n\n\n\n");
        int dCount4 = 1;
        for (DesignMText department : departments) {
            System.out.println(dCount4 + "\t Location:- " + department.getText()+" and Category:- "+ locationCategoryMap.get(department.getText()));
            dCount4++;
        }
        List<DesignPolyLine> categoryLineTextList = dxfParser.getCategoryLineText();
        for(DesignPolyLine designPolyLine : categoryLineTextList){
           // System.out.println(categoryLineTextList.indexOf(designPolyLine)+"       "+designPolyLine.toString());
        }
        
        List<DesignPolyLine> brandLineList = dxfParser.getBrandLineList();
        List<DesignMText> brandAreaList = dxfParser.getBrandArea();
        for(DesignPolyLine designPolyLine : brandLineList){
            for(DesignMText designMText : brandAreaList){
                if(designPolyLine.isValid(designMText.getDxfPoint())){
                    designPolyLine.setText(designMText.getText());
                    break;
                }
            }
        }
        for(DesignMText designMText : brandAreaList){
            //System.out.println(brandAreaList.indexOf(designMText)+"     "+designMText.toString());
        }
        System.out.println("\n\n\n\n\n");
        for(DesignPolyLine designPolyLine : brandLineList){
            //System.out.println(brandLineList.indexOf(designPolyLine)+"       "+designPolyLine.toString());
        }

        List<DesignMText> categoryText = dxfParser.getCategory();
        for(DesignMText designText : categoryText){
            //System.out.println(categoryText.indexOf(designText)+"       "+designText.toString());
        }

        for(DesignPolyLine designPolyLine : dxfParser.getCategoryLineList()){
            //System.out.println(dxfParser.getCategoryLineList().indexOf(designPolyLine)+"       "+designPolyLine.toString());
        }

    }
}
