package com.spaceplanner.java.dxf;

import com.spaceplanner.java.dto.DesignDetail;
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
public class DesignParserExt implements DesignApi {

    private DXFDocument dxfDocument = null;
    private List<DesignDetail> designDetails = new ArrayList<DesignDetail>();
    private Map<String,List<DesignDetail>> catMap = new HashMap<String, List<DesignDetail>>();
    private Map<String,List<DesignDetail>> catAreaMap = new HashMap<String, List<DesignDetail>>();
    private boolean readBrand =false;
    private boolean readLocation =false;

    public DesignParserExt(InputStream inputStream, boolean readBrand, boolean readLocation) throws ParseException {
        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse(inputStream, org.kabeja.parser.DXFParser.DEFAULT_ENCODING);
        init(parser, readBrand, readLocation);
    }

    public DesignParserExt(String filePath, boolean readBrand, boolean readLocation) throws ParseException {
        Parser parser = ParserBuilder.createDefaultParser();
        parser.parse(filePath, org.kabeja.parser.DXFParser.DEFAULT_ENCODING);
        init(parser, readBrand, readLocation);
    }

    private void init(Parser parser, boolean readBrand, boolean readLocation){
        this.readBrand = readBrand;
        this.readLocation = readLocation;
        this.dxfDocument = parser.getDocument();
        processDXF();
    }

    public List<DesignDetail> getDesignDetails(){
        return this.designDetails;
    }

    public List<DesignDetail> getCategoryDesignDetails(String category) {
        return catMap.get(category.trim());
    }

    public List<DesignDetail> getCategoryDesignDetails(String category, Double area) {
        return catAreaMap.get(category.toLowerCase()+"-"+area);
    }

    private void processDXF(){
        List<DesignPolyLine> catLines = getLineLayer(CATEGORY_LINE);
        List<DesignMText> catTexts = getDesignMText(CATEGORY_NAME, TextType.CAT_NAME);
        List<DesignPolyLine> brandLines = getBrandLines();
        for(DesignPolyLine catLine : catLines){
            for(DesignMText cat : catTexts){
                if(!cat.isHasCatLine() && catLine.isValid(cat.getDxfPoint())){
                    cat.setHasCatLine(true);
                    catLine.addMText(cat);
                    for(DesignPolyLine brandLine : brandLines){
                        if(null != brandLine.getmTexts() && brandLine.getmTexts().size()>0) {
                            if (!brandLine.isHasPolyLine() && catLine.isValid(brandLine.getmTexts().get(0).getDxfPoint())) {
                                brandLine.setHasPolyLine(true);
                                DesignDetail designDetail = new DesignDetail();
                                designDetail.setCategory(cat.getText());
                                designDetail.setLineId(brandLine.getId());
                                for (DesignMText mText : brandLine.getmTexts()) {
                                    switch (mText.getTextType()) {
                                        case LOCATION:
                                            designDetail.setLocation(mText.getText());
                                            break;
                                        case BRAND_NAME:
                                            designDetail.setBrand(mText.getText());
                                            break;
                                        case BRAND_AREA:
                                            designDetail.setArea(mText.getTextAsArea());
                                            break;
                                    }
                                }
                                designDetails.add(designDetail);
                                updateCatMap(designDetail);
                                updateCatAreaMap(designDetail);
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateCatMap(DesignDetail designDetail){
        List<DesignDetail> catDesignDetails = catMap.get(designDetail.getCategory());
        if (null == catDesignDetails)
            catDesignDetails = new ArrayList<DesignDetail>();
        catDesignDetails.add(designDetail);
        catMap.put(designDetail.getCategory(), catDesignDetails);
    }
    private void updateCatAreaMap(DesignDetail designDetail){
        String key = designDetail.getCategory().toLowerCase()+"-"+designDetail.getArea();
        List<DesignDetail> catAreaDesignDetails = catAreaMap.get(key);
        if (null == catAreaDesignDetails)
            catAreaDesignDetails = new ArrayList<DesignDetail>();
        catAreaDesignDetails.add(designDetail);
        catAreaMap.put(key, catAreaDesignDetails);
    }

    private List<DesignPolyLine> getBrandLines(){
        List<DesignPolyLine> brandLines = getLineLayer(BRAND_LINE);
        List<DesignMText> brandAreas = getBrandAreas();
        List<DesignMText> brandNames = getBrandNames();
        List<DesignMText> locations = getLocations();
        for (DesignPolyLine brandLine : brandLines) {
            for(DesignMText brandArea : brandAreas){
                if(!brandArea.isHasBrandLine() && brandLine.isValid(brandArea.getDxfPoint())){
                    brandArea.setHasBrandLine(true);
                    brandLine.addMText(brandArea);
                    break;
                }
            }
            for(DesignMText brand : brandNames){
                if (!brand.isHasBrandLine() && brandLine.isValid(brand.getDxfPoint())) {
                    brand.setHasBrandLine(true);
                    brandLine.addMText(brand);
                    break;
                }
            }
            for(DesignMText location : locations){
                if (!location.isHasBrandLine() && brandLine.isValid(location.getDxfPoint())) {
                    location.setHasBrandLine(true);
                    brandLine.addMText(location);
                    break;
                }
            }
        }
        return brandLines;
    }

    private List<DesignMText> getBrandNames() {
        List<DesignMText> brandNames;
        try{
            brandNames = getDesignMText(BRAND_NAME, TextType.BRAND_NAME);
        }catch (LayerNotFoundException le){
            if(readBrand)
                throw le;
            else
                brandNames = new ArrayList<DesignMText>();
        }
        return brandNames;
    }

    private List<DesignMText> getLocations() {
        List<DesignMText> locations;
        try{
            locations = getDesignMText(LOCATION, TextType.LOCATION);
        }catch (LayerNotFoundException le){
            if(readLocation)
                throw le;
            else
                locations = new ArrayList<DesignMText>();
        }
        return locations;
    }

    private List<DesignMText> getBrandAreas() {
        return getDesignMText(BRAND_AREA, TextType.BRAND_AREA);
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

    private List<DesignMText> getDesignMText(String layerName, TextType textType){
        DXFLayer layer = getDXFLayer(layerName);
        List<DXFMText> mtextBList = layer.getDXFEntities(MTEXT);
        List<DesignMText> designMTextList = new ArrayList<DesignMText>();
        for (DXFMText dxfmText : mtextBList) {
            DesignMText designMText = new DesignMText(dxfmText,textType);
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

    public static void main(String args[]) throws ParseException {
        //DesignParserExt designParser = new DesignParserExt("/Users/ashqures/project/home-pc/space_planner/prod_issue/SAHARA-GANJ-FF-27-07-16_as.dxf", false, true);
        DesignParserExt designParser = new DesignParserExt("/Users/ashqures/project/home-pc/space_planner/bf/doc/1.dxf", true, false);
        List<DesignDetail> designDetails = designParser.getDesignDetails();
        for(DesignDetail designDetail : designDetails)
            System.out.println(designDetail);
    }
}
