package com.spaceplanner.java.util;

import com.spaceplanner.java.dto.SpaceMasterDTO;
import com.spaceplanner.java.model.FloorDesignDetailsEntity;
import com.spaceplanner.java.model.FloorEntity;
import com.spaceplanner.java.model.StoreEntity;
import com.spaceplanner.java.model.master.BrandEntity;
import com.spaceplanner.java.model.type.DesignStatus;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 08/06/15
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExcelUtil implements Constants {
    
    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    
    public static void write(List<FloorEntity> floorList, StoreEntity store, OutputStream outputStream)throws IOException{
        try{
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(store.getName());
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeight(16);
            XSSFRow headerRow = sheet.createRow(0);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(font);
            List<String> columnNameList = getStoreColumnNames();
            for (int i = 0; i < columnNameList.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnNameList.get(i));
                cell.setCellStyle(headerCellStyle);
                sheet.autoSizeColumn(i);
            }
            XSSFRow row = sheet.createRow(1);
            Map<String,Object> columnNameValueMap = getStore(store);
            for (int i = 0; i < columnNameList.size(); i++) {
                Cell cell = row.createCell(i);
                Object value = columnNameValueMap.get(columnNameList.get(i));
                if(null!= value){
                    if(value instanceof Double){
                        cell.setCellValue((Double) value);
                    }else{
                        cell.setCellValue(value+"");
                    }
                }
                sheet.autoSizeColumn(i);
            }
            XSSFRow floorHeaderRow = sheet.createRow(5);
            List<String> floorColumnNameList = getFloorColumnNames();
            for (int i = 0; i < floorColumnNameList.size(); i++) {
                Cell cell = floorHeaderRow.createCell(i);
                cell.setCellValue(floorColumnNameList.get(i));
                cell.setCellStyle(headerCellStyle);
                sheet.autoSizeColumn(i);
            }
            int rowCount = 1;
            for (FloorEntity floor : floorList) {
                Map<String, Object> floorMap = getFloor(floor);
                XSSFRow rowF = sheet.createRow(rowCount+5);
                for (int i = 0; i < floorColumnNameList.size(); i++) {
                    Cell cell = rowF.createCell(i);
                    Object value = floorMap.get(floorColumnNameList.get(i));
                    if (null != value) {
                        if (value instanceof Double)
                            cell.setCellValue((Double)value);
                        else
                            cell.setCellValue(value + "");
                    }
                }
                rowCount++;
            }
            workbook.write(outputStream);
            logger.info("Excel done");
        } catch (Exception e){
            logger.error("Unable to write file \n" + e);
        }finally {
            if (null != outputStream) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    public static void write(List<FloorDesignDetailsEntity> floorDesignDetails, FloorEntity floor,List<BrandEntity> brandList, OutputStream outputStream) throws IOException {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(floor.getFloorNumber());
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setFontHeight(16);
            XSSFRow headerRow = sheet.createRow(0);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(font);
            List<String> columnNameList = getColumnNameList(floor.getDesignStatus());
            int brandColIndex = columnNameList.indexOf(COLUMN_BRAND);
            prepareDataValidationForBrand(workbook,sheet,brandList,floorDesignDetails.size(),brandColIndex);
            for (int i = 0; i < columnNameList.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnNameList.get(i));
                cell.setCellStyle(headerCellStyle);
                sheet.autoSizeColumn(i);
            }
            CellStyle doubleCellStyle = workbook.createCellStyle();
            doubleCellStyle.setDataFormat(workbook.createDataFormat().getFormat("0.0"));
            CellStyle mismatchCellStyle = workbook.createCellStyle();
            mismatchCellStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
            mismatchCellStyle.setFillPattern(CellStyle.FINE_DOTS);
            CellStyle matchCellStyle = workbook.createCellStyle();
            matchCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
            matchCellStyle.setFillPattern(CellStyle.FINE_DOTS);
            int rowCount = 1;
            for (FloorDesignDetailsEntity floorDesignDetail : floorDesignDetails) {
                Map<String, Object> floorDetailMap = getFloorDetails(floorDesignDetail);
                XSSFRow row = sheet.createRow(rowCount);
                int index=0;
                if (floor.getDesignStatus().equals(DesignStatus.Brand_Design_Uploaded)){
                    row.createCell(index).setCellValue("");
                    if (floorDesignDetail.isValid())
                        row.getCell(index).setCellStyle(matchCellStyle);
                    else
                        row.getCell(index).setCellStyle(mismatchCellStyle);
                    index=index+1;
                }
                for (int i = index; i < columnNameList.size(); i++) {
                    Cell cell = row.createCell(i);
                    Object value = floorDetailMap.get(columnNameList.get(i));
                    if (null != value) {
                        if (value instanceof Double){
                            cell.setCellStyle(doubleCellStyle);
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(new Double(value.toString()));
                            //cell.setCellValue(112.45);
                        }
                        else
                            cell.setCellValue(value + "");
                    }
                   /* if (!floorDesignDetail.isValid()) {
                        cell.setCellStyle(mismatchCellStyle);
                    }*/
                    sheet.autoSizeColumn(i);
                }
                rowCount++;
            }
            workbook.write(outputStream);
            logger.info("Excel done");
        } catch (Exception e) {
            logger.error("Unable to write file \n" + e);
        } finally {
            if (null != outputStream) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    public static void read(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(filePath));
        read(inputStream);
    }
    
    public static List<SpaceMasterDTO> readBrandMaster(InputStream inputStream)throws IOException{
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(sheet.getFirstRowNum());
            Iterator<Cell> headerCells = headerRow.cellIterator();
            Set<String> columnNameSet = getBrandMasterColumnNames();
            Map<String, Integer> columnIndexMap = new HashMap<String, Integer>();
            while (headerCells.hasNext()) {
                Cell cell = headerCells.next();
                String cellValue = cell.getStringCellValue().trim();
                if (columnNameSet.contains(cellValue)) {
                    columnIndexMap.put(cellValue, cell.getColumnIndex());
                }
            }
            List<SpaceMasterDTO> dataList = new ArrayList<SpaceMasterDTO>();
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() > sheet.getFirstRowNum()) {
                    SpaceMasterDTO spaceMasterDTO = new SpaceMasterDTO();
                    spaceMasterDTO.setBrandCode(getCellValue(row, columnIndexMap.get(COLUMN_BRAND_CODE)));
                    spaceMasterDTO.setBrandName(getCellValue(row, columnIndexMap.get(COLUMN_BRAND_NAME)));
                    dataList.add(spaceMasterDTO);
                }
            }
            return dataList;
        } catch (FileNotFoundException e) {
            logger.error("File not found "+e);
        } finally {
            if (null != inputStream)
                inputStream.close();
        }
        return null;
    }
    

    public static Map<String, SpaceMasterDTO> read(InputStream inputStream) throws IOException, NumberFormatException {

        try {
            Map<String, SpaceMasterDTO> dataMap = new HashMap<String, SpaceMasterDTO>();
            Map<String, Integer> columnIndexMap = new HashMap<String, Integer>();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(sheet.getFirstRowNum());
            Iterator<Cell> headerCells = headerRow.cellIterator();
            Set<String> columnNameSet = getColumnNames();
            while (headerCells.hasNext()) {
                Cell cell = headerCells.next();
                String cellValue = cell.getStringCellValue().trim();
                if (columnNameSet.contains(cellValue)) {
                    columnIndexMap.put(cellValue, cell.getColumnIndex());
                }
            }
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() > sheet.getFirstRowNum()) {
                    SpaceMasterDTO spaceMasterDTO = new SpaceMasterDTO();
                    spaceMasterDTO.setDivision(getCellValue(row, columnIndexMap.get(COLUMN_DIVISION)));
                    spaceMasterDTO.setCategory(getCellValue(row, columnIndexMap.get(COLUMN_CATEGORY)));
                    /*spaceMasterDTO.setRunningFtWall(getCellValue(row, columnIndexMap.get(COLUMN_RUNNING_FT_WALL)));*/
                    spaceMasterDTO.setSisDetails(getCellValue(row, columnIndexMap.get(COLUMN_SIS_DETAILS)));
                    spaceMasterDTO.setLocation(getCellValue(row, columnIndexMap.get(COLUMN_LOCATION)));
                    String brand = getCellValue(row, columnIndexMap.get(COLUMN_BRAND));
                    if(StringUtil.isNotNullOrEmpty(brand)){
                        String[] brandCodeAndName = brand.split("-");
                        spaceMasterDTO.setBrandCode(brandCodeAndName[0]);
                        spaceMasterDTO.setBrandName(brandCodeAndName[1]);
                    }
                    //spaceMasterDTO.setBrandCode(getCellValue(row, columnIndexMap.get(COLUMN_BRAND_CODE)));
                    //spaceMasterDTO.setBrandName(getCellValue(row, columnIndexMap.get(COLUMN_BRAND_NAME)));
                    spaceMasterDTO.setMG(getCellValue(row, columnIndexMap.get(COLUMN_MG)));
                    spaceMasterDTO.setPSFPD(getCellValue(row, columnIndexMap.get(COLUMN_PSFPD)));
                    spaceMasterDTO.setSales(getCellValue(row, columnIndexMap.get(COLUMN_SALES)));
                    spaceMasterDTO.setGMV(getCellValue(row, columnIndexMap.get(COLUMN_GMV)));
                    spaceMasterDTO.setAgreementMargin(getCellValue(row, columnIndexMap.get(COLUMN_AGREEMENT_MARGIN)));
                    spaceMasterDTO.setVistexMargin(getCellValue(row, columnIndexMap.get(COLUMN_VISTEX_MARGIN)));
                    spaceMasterDTO.setGMROF(getCellValue(row, columnIndexMap.get(COLUMN_GMROF)));
                    spaceMasterDTO.setSecurityDeposit(getCellValueAsDouble(getCellValue(row, columnIndexMap.get(COLUMN_SECURITY_DEPOSIT))));
                    spaceMasterDTO.setSdAmount(getCellValueAsDouble(getCellValue(row, columnIndexMap.get(COLUMN_SD_AMOUNT))));
                    dataMap.put(spaceMasterDTO.getLocation(), spaceMasterDTO);
                }
            }
            return dataMap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream)
                inputStream.close();
        }
        return null;
    }
    
    private static Double getCellValueAsDouble(String value){
        Double dValue=null;
        try{
            dValue = null!=value && !"".equals(value)?Double.parseDouble(value):null;
        }catch (NumberFormatException e){
            logger.error("Unable tp parse excel, Invalid data "+e);
           throw e;
        }
        return dValue;
    }

    private static String getCellValue(Row row, Integer cellIndex) {
        if (null == cellIndex || null == row.getCell(cellIndex))
            return null;
        Cell cell = row.getCell(cellIndex);
        //System.out.println(cell.getCellStyle().getDataFormatString());
        String value = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (cell.getCellStyle().getDataFormatString().contains("%"))
                    value = Math.round(cell.getNumericCellValue() * 100 *100.0)/100.0+ "";
                else
                    value = Math.round(cell.getNumericCellValue()*100.0)/100.0 + "";
                break;
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                if(null!=value)
                    value=value.trim();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue() + "";
                break;
        }
        return value;
    }

    private static Set<String> getColumnNames() {
        Set<String> nameSet = new HashSet<String>();
        nameSet.add(Constants.COLUMN_DIVISION);
        nameSet.add(Constants.COLUMN_CATEGORY);
        nameSet.add(Constants.COLUMN_RUNNING_FT_WALL);
        nameSet.add(Constants.COLUMN_SIS_DETAILS);
        nameSet.add(Constants.COLUMN_LOCATION);
        nameSet.add(Constants.COLUMN_BRAND);
        nameSet.add(Constants.COLUMN_BRAND_CODE);
        nameSet.add(Constants.COLUMN_BRAND_NAME);
        nameSet.add(Constants.COLUMN_AREA);
        nameSet.add(Constants.COLUMN_MG);
        nameSet.add(Constants.COLUMN_PSFPD);
        nameSet.add(Constants.COLUMN_SALES);
        nameSet.add(Constants.COLUMN_GMV);
        nameSet.add(Constants.COLUMN_AGREEMENT_MARGIN);
        nameSet.add(Constants.COLUMN_VISTEX_MARGIN);
        nameSet.add(Constants.COLUMN_GMROF);
        nameSet.add(Constants.COLUMN_SECURITY_DEPOSIT);
        nameSet.add(Constants.COLUMN_SD_AMOUNT);
        return nameSet;
    }


    private static Set<String> getBrandMasterColumnNames(){
        Set<String> nameSet = new HashSet<String>();
        nameSet.add(COLUMN_BRAND_CODE);
        nameSet.add(COLUMN_BRAND_NAME);
        return nameSet;
    }

    private static Map<String, Object> getFloorDetails(FloorDesignDetailsEntity floorDesignDetail) {
        Map<String, Object> columnValueMap = new HashMap<String, Object>();
        if (null != floorDesignDetail.getCategoryDivision())
            columnValueMap.put(COLUMN_DIVISION, floorDesignDetail.getCategoryDivision().getDivision());
        columnValueMap.put(COLUMN_CATEGORY, floorDesignDetail.getCategory());
        columnValueMap.put(COLUMN_RUNNING_FT_WALL, floorDesignDetail.getDesignRunningFtWall());
        columnValueMap.put(COLUMN_SIS_DETAILS, floorDesignDetail.getSisDetails());
        columnValueMap.put(COLUMN_LOCATION, floorDesignDetail.getLocationCode());
        if (null != floorDesignDetail.getBrand()) {
            columnValueMap.put(COLUMN_BRAND, floorDesignDetail.getBrand().getBrand());
            /*columnValueMap.put(COLUMN_BRAND_CODE, floorDesignDetail.getBrand().getCode());
            columnValueMap.put(COLUMN_BRAND_NAME, floorDesignDetail.getBrand().getName());*/
        }
        columnValueMap.put(COLUMN_DESIGN_BRAND_NAME, floorDesignDetail.getDesignBrandName());
        columnValueMap.put(COLUMN_AREA, floorDesignDetail.getDesignArea());
        columnValueMap.put(COLUMN_MG, floorDesignDetail.getMG());
        columnValueMap.put(COLUMN_PSFPD, floorDesignDetail.getPSFPD());
        columnValueMap.put(COLUMN_SALES, floorDesignDetail.getSales());
        columnValueMap.put(COLUMN_GMV, floorDesignDetail.getGMV());
        columnValueMap.put(COLUMN_AGREEMENT_MARGIN, floorDesignDetail.getAgreementMargin());
        columnValueMap.put(COLUMN_VISTEX_MARGIN, floorDesignDetail.getVistexMargin());
        columnValueMap.put(COLUMN_GMROF, floorDesignDetail.getGMROF());
        columnValueMap.put(COLUMN_SECURITY_DEPOSIT, floorDesignDetail.getSecurityDeposit());
        columnValueMap.put(COLUMN_SD_AMOUNT, floorDesignDetail.getSdAmount());
        return columnValueMap;
    }


    public static List<String> getColumnNameList(DesignStatus designStatus) {
        List<String> columnNameList = new ArrayList<String>();
        if (designStatus.equals(DesignStatus.Brand_Design_Uploaded))
            columnNameList.add(COLUMN_STATUS);
        columnNameList.add(COLUMN_DIVISION);
        columnNameList.add(COLUMN_CATEGORY);
        /*columnNameList.add(COLUMN_RUNNING_FT_WALL);*/
        columnNameList.add(COLUMN_SIS_DETAILS);
        columnNameList.add(COLUMN_LOCATION);
        columnNameList.add(COLUMN_AREA);
        columnNameList.add(COLUMN_BRAND);
        /*columnNameList.add(COLUMN_BRAND_CODE);
        columnNameList.add(COLUMN_BRAND_NAME);*/
        if (designStatus.equals(DesignStatus.Brand_Design_Uploaded)) {
            /*columnNameList.add(COLUMN_DESIGN_BRAND_CODE);*/
            columnNameList.add(COLUMN_DESIGN_BRAND_NAME);
        }
        columnNameList.add(COLUMN_MG);
        columnNameList.add(COLUMN_PSFPD);
        columnNameList.add(COLUMN_SALES);
        columnNameList.add(COLUMN_GMV);
        columnNameList.add(COLUMN_AGREEMENT_MARGIN);
        columnNameList.add(COLUMN_VISTEX_MARGIN);
        columnNameList.add(COLUMN_GMROF);
        columnNameList.add(COLUMN_SECURITY_DEPOSIT);
        columnNameList.add(COLUMN_SD_AMOUNT);
        return columnNameList;
    }

    /*public static List<String> getColumnNameList(DesignStatus designStatus) {
        List<String> columnNameList = new ArrayList<String>();
        columnNameList.add(COLUMN_SL_NO);
        columnNameList.add(COLUMN_DIVISION);
        columnNameList.add(COLUMN_CATEGORY);
        *//*columnNameList.add(COLUMN_RUNNING_FT_WALL);*//*
        columnNameList.add(COLUMN_SIS_DETAILS);
        columnNameList.add(COLUMN_LOCATION);
        columnNameList.add(COLUMN_AREA);
        if (designStatus.equals(DesignStatus.Brand_Master_Uploaded)
                || designStatus.equals(DesignStatus.Brand_Master_Published)
                || designStatus.equals(DesignStatus.Brand_Design_published)) {
            columnNameList.add(COLUMN_BRAND_CODE);
            columnNameList.add(COLUMN_BRAND_NAME);
        } else if (designStatus.equals(DesignStatus.Brand_Design_Uploaded)) {
            columnNameList.add(COLUMN_BRAND_CODE);
            columnNameList.add(COLUMN_BRAND_NAME);
            columnNameList.add(COLUMN_DESIGN_BRAND_CODE);
            columnNameList.add(COLUMN_DESIGN_BRAND_NAME);
        } else if (designStatus.equals(DesignStatus.Enrichment_Uploaded)) {
            columnNameList.add(COLUMN_BRAND_CODE);
            columnNameList.add(COLUMN_BRAND_NAME);
            columnNameList.add(COLUMN_MG);
            columnNameList.add(COLUMN_PSFPD);
            columnNameList.add(COLUMN_SALES);
            columnNameList.add(COLUMN_GMV);
            columnNameList.add(COLUMN_AGREEMENT_MARGIN);
            columnNameList.add(COLUMN_VISTEX_MARGIN);
            columnNameList.add(COLUMN_GMROF);
            columnNameList.add(COLUMN_SECURITY_DEPOSIT);
            columnNameList.add(COLUMN_SD_AMOUNT);
        }
        return columnNameList;
    }*/
    
    
    private static List<String> getStoreColumnNames(){
        List<String> columnNames = new ArrayList<String>() ;
        columnNames.add(COLUMN_STORE_NAME);
        columnNames.add(COLUMN_NO_OF_FLOOR);
        /*columnNames.add(COLUMN_CHARGEABLE_AREA);*/
        columnNames.add(COLUMN_CARPET_AREA);
        columnNames.add(COLUMN_RETAIL_AREA);
        columnNames.add(COLUMN_APPAREL_GMROF);
        columnNames.add(COLUMN_LIFE_STYLE_GMROF);
        columnNames.add(COLUMN_STORE_GMROF);
        columnNames.add(COLUMN_PROJECTED_STORE_SALE);
        return columnNames;
    }
    
    private static Map<String, Object> getStore(StoreEntity store){
        Map<String, Object> columnValueMap = new HashMap<String, Object>();
        columnValueMap.put(COLUMN_STORE_NAME, store.getName());
        columnValueMap.put(COLUMN_NO_OF_FLOOR, store.getNoOfFloor());
        /*columnValueMap.put(COLUMN_CHARGEABLE_AREA, store.getChargeableArea());*/
        columnValueMap.put(COLUMN_CARPET_AREA, store.getCarpetArea());
        columnValueMap.put(COLUMN_RETAIL_AREA, store.getRetailArea());
        columnValueMap.put(COLUMN_APPAREL_GMROF, store.getApparelGMROF());
        columnValueMap.put(COLUMN_LIFE_STYLE_GMROF, store.getLifeStyleGMROF());
        columnValueMap.put(COLUMN_STORE_GMROF, store.getStoreGMROF());
        columnValueMap.put(COLUMN_PROJECTED_STORE_SALE, store.getProjectedStoreSale());
        return columnValueMap;
    }

    private static List<String> getFloorColumnNames(){
        List<String> columnNames = new ArrayList<String>() ;
        columnNames.add(COLUMN_FLOOR_NUMBER);
        /*columnNames.add(COLUMN_CHARGEABLE_AREA);*/
        columnNames.add(COLUMN_CARPET_AREA);
        columnNames.add(COLUMN_RETAIL_AREA);
        columnNames.add(COLUMN_DESIGN_STATUS);
        return columnNames;
    }

    private static Map<String, Object> getFloor(FloorEntity floor){
        Map<String, Object> columnValueMap = new HashMap<String, Object>();
        columnValueMap.put(COLUMN_FLOOR_NUMBER, floor.getFloorNumber());
        /*columnValueMap.put(COLUMN_CHARGEABLE_AREA, floor.getChargeableArea());*/
        columnValueMap.put(COLUMN_CARPET_AREA, floor.getCarpetArea());
        columnValueMap.put(COLUMN_RETAIL_AREA, floor.getRetailArea());
        columnValueMap.put(COLUMN_DESIGN_STATUS, floor.getDesignStatus().toString().replace("_"," "));
        return columnValueMap;
    }
    
    private static String[] getBrandArray(List<BrandEntity> brandList){
        String brandArray[] = new String[brandList.size()];
        for(int i=0;i<brandList.size();i++){
            brandArray[i]=brandList.get(i).getBrand();
            //System.out.println(brandList.get(i).getBrand());
        }
        return brandArray;
    }
    
    /*private static void prepareDataValidationForBrand(XSSFSheet sheet, List<BrandEntity> brandList, int endRowIndex, int colIndex){
        DataValidation dataValidation = null;
        DataValidationConstraint constraint = null;
        DataValidationHelper validationHelper = null;
        validationHelper=new XSSFDataValidationHelper(sheet);
        CellRangeAddressList addressList = new  CellRangeAddressList(1,endRowIndex+1,colIndex,colIndex);
        constraint =validationHelper.createExplicitListConstraint(getBrandArray(brandList));
        dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);
        dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        dataValidation.setSuppressDropDownArrow(true);
        sheet.addValidationData(dataValidation);
    }*/

    private static void prepareDataValidationForBrand(XSSFWorkbook workbook, XSSFSheet sheet, List<BrandEntity> brandList, int endRowIndex, int colIndex){
        String[] brandNameArray = getBrandArray(brandList);//getCountries();
        XSSFSheet hidden = workbook.createSheet("hidden");
        for (int i = 0, length= brandNameArray.length; i < length; i++) {
            String name = brandNameArray[i];
            XSSFRow row = hidden.createRow(i+1);
            XSSFCell cell = row.createCell(colIndex);
            cell.setCellValue(name);
        }
        Name namedCell = workbook.createName();
        namedCell.setNameName("hidden");
        String colName  = CellReference.convertNumToColString(colIndex);
        namedCell.setRefersToFormula("hidden!$"+colName+"$1:$"+colName+"$" + brandNameArray.length);
        XSSFDataValidationHelper validationHelper = new XSSFDataValidationHelper(sheet);
        CellRangeAddressList addressList = new  CellRangeAddressList(1,endRowIndex,colIndex,colIndex);
        DataValidationConstraint constraint =validationHelper.createFormulaListConstraint("hidden");
        DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);
        dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        dataValidation.setSuppressDropDownArrow(true);
        workbook.setSheetHidden(1, true);
        sheet.addValidationData(dataValidation);
    }
    
    
    public static void main(String args[]) throws IOException {

        String []countryName = getCountries();
        /*HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet realSheet = workbook.createSheet("Sheet xls");
        HSSFSheet hidden = workbook.createSheet("hidden");
        for (int i = 0, length= countryName.length; i < length; i++) {
            String name = countryName[i];
            HSSFRow row = hidden.createRow(i);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(name);
        }
        Name namedCell = workbook.createName();
        namedCell.setNameName("hidden");
        namedCell.setRefersToFormula("hidden!$A$1:$A$" + countryName.length);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint("hidden");
        CellRangeAddressList addressList = new CellRangeAddressList(0, 0, 0, 0);
        HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint);
        workbook.setSheetHidden(1, true);
        realSheet.addValidationData(validation);
        FileOutputStream stream = new FileOutputStream("/Users/ashqures/range.xls");
        workbook.write(stream);
        stream.close();*/


        DataValidation dataValidation = null;
        DataValidationConstraint constraint = null;
        XSSFDataValidationHelper validationHelper = null;

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet1=(XSSFSheet) wb.createSheet("sheet1");
        XSSFSheet hidden = wb.createSheet("hidden");
        for (int i = 0, length= countryName.length; i < length; i++) {
            String name = countryName[i];
            XSSFRow row = hidden.createRow(i);
            XSSFCell cell = row.createCell(5);
            cell.setCellValue(name);
        }
        //CellStyle editableStyle = wb.createCellStyle();
       // editableStyle.setLocked(false);
        //sheet1.setDefaultColumnStyle(5, editableStyle);

        Name namedCell = wb.createName();
        namedCell.setNameName("hidden");
        String colName  = CellReference.convertNumToColString(5);
        namedCell.setRefersToFormula("hidden!$"+colName+"$1:$"+colName+"$" + countryName.length);
        //namedCell.setRefersToFormula("hidden!$A$1:$A$" + countryName.length);

        validationHelper=new XSSFDataValidationHelper(sheet1);
        CellRangeAddressList addressList = new  CellRangeAddressList(1,20,5,5);
        constraint =validationHelper.createFormulaListConstraint("hidden");
        dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setShowErrorBox(true);
        dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        dataValidation.setSuppressDropDownArrow(true);
        wb.setSheetHidden(1, true);
        sheet1.addValidationData(dataValidation);
        Row rowh= sheet1.createRow(0);
        Cell cellh =rowh.createCell(5);
        cellh.setCellValue("Count");
        Row row= sheet1.createRow(2);
        Cell cell =row.createCell(5);
        cell.setCellValue(50);

        FileOutputStream fileOut = new FileOutputStream("/Users/ashqures/test.xls");
        wb.write(fileOut);
        fileOut.close();






        /*XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet("new sheet");

        DataValidationHelper validationHelper = new XSSFDataValidationHelper(sheet);

        DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(getCountries());

        CellRangeAddressList addressList = new CellRangeAddressList(0, 0, 0, 0);

        DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);

        dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);

        dataValidation.setSuppressDropDownArrow(true);

        sheet.addValidationData(dataValidation);

        try {
            FileOutputStream fileOut = new FileOutputStream("/Users/ashqures/test.xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/
    }
    
    public static String [] getCountries(){
        String[] countries = new String[10000];
        for(int i=0;i<10000;i++){
            countries[i]=i+"abcd"+i;
        }
        return countries;
    }


}
