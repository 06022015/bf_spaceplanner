package com.spaceplanner.java.dxf;

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
 * User: ashqures
 * Date: 5/17/16
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DesignParser {

    private static String BRAND_LINE = "I-BRAND_LINE";
    private static String BRAND_NAME = "I-BRAND_NAME";
    private static String BRAND_AREA = "I-BRAND_AREA";
    private static String LOCATION = "I-LOCATION CODE";
    private static String CATEGORY_LINE = "I-CAT LINE";
    private static String CATEGORY_NAME = "I-CAT TEXT";
    private static String LWPOLYLINE = "LWPOLYLINE";
    private static String MTEXT = "MTEXT";



    private DXFDocument dxfDocument = null;

    private List<DesignPolyLine> brandLineList=null;

    private List<DesignMText> departmentList=null;

    private List<DesignPolyLine> categoryLineList =null;

    private List<String> departments = new ArrayList<String>();


    public DesignParser(InputStream inputStream) throws ParseException {
        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse(inputStream, org.kabeja.parser.DXFParser.DEFAULT_ENCODING);
        dxfDocument = parser.getDocument();
    }

    public DesignParser(String filePath) throws ParseException {
        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse(filePath, org.kabeja.parser.DXFParser.DEFAULT_ENCODING);
        this.dxfDocument = parser.getDocument();
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
        if(null==departments || departments.size()<=0){
            this.departmentList = getDepartment();
        }
        return departments;
    }

    private List<DesignPolyLine> getCategoryLineList() {
        if(null==categoryLineList){
            this.categoryLineList = getLineLayer(CATEGORY_LINE);
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
                designPolyLineList.add(designPolyLine);
        }
        return designPolyLineList;
    }

    private List<DesignMText> getDepartment() {
        DXFLayer layer = getDXFLayer(LOCATION);
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


    private List<DesignMText> getDesignMText(String layerName, boolean isArea){
        DXFLayer layer = getDXFLayer(layerName);
        List<DXFMText> mtextBList = layer.getDXFEntities(MTEXT);
        List<DesignMText> designMTextList = new ArrayList<DesignMText>();
        for (DXFMText dxfmText : mtextBList) {
            DesignMText designMText = new DesignMText(dxfmText,isArea);
            if (designMText.isValid())
                designMTextList.add(designMText);
        }
        return designMTextList;
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
        List<DesignMText> brandAreaList = getDesignMText(BRAND_AREA, true);
        for (DesignMText department : departmentList) {
            boolean isValid=false;
            for (DesignMText designMText : brandAreaList) {
                if (!designMText.isHasDepartment()) {
                    for (DesignPolyLine designPolyLine : brandLines) {
                        if (designPolyLine.isValid(department.getDxfPoint()) && designPolyLine.isValid(designMText.getDxfPoint())) {
                            locationAreaMap.put(department.getText(), designMText.getTextAsArea());
                            designMText.setHasDepartment(true);
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


    public Map<String,String> getLocationBrandMap(){
        Map<String, String> locationBrandMap = new HashMap<String, String>();
        List<DesignPolyLine> brandLines = getBrandLineList();
        List<DesignMText> departmentList = getDepartmentList();
        List<DesignMText> brandNameList = getDesignMText(BRAND_NAME, false);
        for (DesignMText department : departmentList) {
            boolean isValid=false;
            for (DesignMText designMText : brandNameList) {
                if (!designMText.isHasDepartment()) {
                    for (DesignPolyLine designPolyLine : brandLines) {
                        if (designPolyLine.isValid(department.getDxfPoint()) && designPolyLine.isValid(designMText.getDxfPoint())) {
                            locationBrandMap.put(department.getText(), designMText.getText());
                            designMText.setHasDepartment(true);
                            isValid=true;
                            break;
                        }
                    }
                }
                if(isValid)
                    break;
            }
        }
        return locationBrandMap;
    }

    public Map<String,String> getLocationCategoryMap(){
        Map<String, String> locationCategoryMap = new HashMap<String, String>();
        List<DesignPolyLine> categoryLines = getCategoryLineList();
        List<DesignMText> departmentList = getDepartmentList();
        List<DesignMText> categoryList = getDesignMText(CATEGORY_NAME, false);
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
        List<DesignMText> categoryTextList = getDesignMText(CATEGORY_NAME, false);
        for(DesignPolyLine designPolyLine :  designPolyLineList){
            for(DesignMText designMText : categoryTextList){
                if(designPolyLine.isValid(designMText.getDxfPoint())){
                    designPolyLine.setText(designMText.getText());
                }
            }
        }

        return designPolyLineList;
    }

    public static void main(String args[]) throws ParseException {
        //DesignParser designParser = new DesignParser("/Users/ashqures/project/home-pc/space_planner/prod_issue/SAHARA-GANJ-FF-27-07-16_as.dxf");
        DesignParser designParser = new DesignParser("/Users/ashqures/project/home-pc/space_planner/design/bf/BF_KROME_MALL_R9  FIRST_FLOOR.dxf");
        //DesignParser designParser = new DesignParser("/Users/ashqures/project/home-pc/space_planner/design/1/SAHARA-GANJ-FF.dxf");
        List<String> locationList = null;//designParser.getDepartments();
        Map<String, Double> locationAreaMap = designParser.getLocationAreaMap();
        Map<String, String> locationBrandNameMap = designParser.getLocationBrandMap();
        Map<String, String> locationCategoryMap = designParser.getLocationCategoryMap();
        //System.out.println("\n\n\n\n\n\n\n");
        for(String location : locationList){
            //System.out.println(location);
        }


        System.out.println("\n\n\n\n\n\n\n");
        System.out.println("Location Area");
        for(String location : locationList){      /*+" >> Category:- "+ locationCategoryMap.get(location)*/
            System.out.println(location+"  >> Area:- "+locationAreaMap.get(location)+"  >>  Name:- "+ locationBrandNameMap.get(location));
           // System.out.println(locationAreaMap.get(location));
        }

        //System.out.println("\n\n\n\n\n\n\n");
        //System.out.println("Location Brand");
        for(String location : locationList){
            //System.out.println(location+"  >> "+locationBrandNameMap.get(location));
            //System.out.println(locationBrandNameMap.get(location));
        }

       // System.out.println("\n\n\n\n\n\n\n");
        //System.out.println("Location Category");
        for(String location : locationList){
            //System.out.println(location+"  >> "+locationCategoryMap.get(location));
            //System.out.println(locationCategoryMap.get(location));
        }

        /*for(DesignPolyLine designPolyLine : designParser.getCategoryLineText()){
            System.out.println(designPolyLine.toString());
        }*/

    }

}
