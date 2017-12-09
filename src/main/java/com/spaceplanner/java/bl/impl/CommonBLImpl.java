package com.spaceplanner.java.bl.impl;

import com.spaceplanner.java.bl.CommonBL;
import com.spaceplanner.java.dao.CommonDao;
import com.spaceplanner.java.dao.UserDao;
import com.spaceplanner.java.dto.DesignDetail;
import com.spaceplanner.java.dto.DesignUploadForm;
import com.spaceplanner.java.dto.FloorUploadForm;
import com.spaceplanner.java.dto.SpaceMasterDTO;
import com.spaceplanner.java.exception.SpacePlannerResponseStatus;
import com.spaceplanner.java.model.*;
import com.spaceplanner.java.model.master.BrandEntity;
import com.spaceplanner.java.model.master.CategoryDivision;
import com.spaceplanner.java.model.master.Role;
import com.spaceplanner.java.model.type.ChangeRequestType;
import com.spaceplanner.java.model.type.DesignStatus;
import com.spaceplanner.java.model.type.Status;
import com.spaceplanner.java.security.PasswordEncoder;
import com.spaceplanner.java.task.NotifyUserTask;
import com.spaceplanner.java.task.SpacePlannerBeans;
import com.spaceplanner.java.util.*;
import com.spaceplanner.java.dxf.*;
import org.apache.velocity.app.VelocityEngine;
import org.kabeja.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 13/07/15
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("commonBL")
@Transactional
public class CommonBLImpl extends BaseBL implements CommonBL, Constants {

    private Logger logger = LoggerFactory.getLogger(CommonBLImpl.class);

    @Autowired
    private ValidatorUtil validatorUtil;

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private SpacePlannerBeans spacePlannerBeans;



    public void executeSQLQueryUpdate(String sqlQuery) {
        commonDao.executeSQLQueryUpdate(sqlQuery);
    }


    public StoreEntity getStore(Long id) {
        return (StoreEntity) commonDao.get(StoreEntity.class, id);
    }


    public List<StoreEntity> getActiveStores() {
        List<StoreEntity> stores = commonDao.getStores(Status.ACTIVE);
        return stores;
    }


    public StoreEntity save(StoreEntity store, SpacePlannerResponseStatus status) {
        if (null != store.getId()) {
            StoreEntity storeEntity = (StoreEntity) commonDao.get(StoreEntity.class, store.getId());
            storeEntity.update(store);
            commonDao.save(storeEntity);
            saveSuccessMessage(status, commonUtil.getText("success.update", status.getLocale()));
            store = storeEntity;
        } else {
            commonDao.save(store);
            saveSuccessMessage(status, commonUtil.getText("success.save", status.getLocale()));
        }
        return store;
    }


    public void deleteStore(Long id, SpacePlannerResponseStatus status) {
        StoreEntity storeEntity = (StoreEntity) commonDao.get(StoreEntity.class, id);
        storeEntity.setStatus(Status.DELETED);
        storeEntity.setUpdatedAt(new Date());
        commonDao.save(storeEntity);
        saveSuccessMessage(status, commonUtil.getText("success.delete", status.getLocale()));
    }


    public void archiveStore(Long storeId, SpacePlannerResponseStatus status) {
        StoreEntity storeEntity = (StoreEntity) commonDao.get(StoreEntity.class, storeId);
        storeEntity.setStatus(Status.ARCHIVED);
        storeEntity.setUpdatedAt(new Date());
        commonDao.save(storeEntity);
        saveSuccessMessage(status, commonUtil.getText("success.archive", status.getLocale()));
    }


    public FloorEntity getFloor(Long id) {
        return (FloorEntity) commonDao.get(FloorEntity.class, id);
    }


    public List<FloorEntity> getActiveFloors(Long storeId) {
        return commonDao.getFloors(storeId, Status.ACTIVE);
    }


    public List<FloorEntity> getFloors() {
        int length = Integer.parseInt(CommonUtil.getProperty("dashboard.floor.length"));
        return commonDao.getRecentFloors(length, Status.ACTIVE);
    }



    public FloorEntity save(FloorEntity floor, SpacePlannerResponseStatus status) {
        if (null != floor.getId()) {
            FloorEntity floorEntity = (FloorEntity) commonDao.get(FloorEntity.class, floor.getId());
            if(!floorEntity.getDesignStatus().equals(DesignStatus.Master_Created) && !floor.getUpdateBy().isUserInRole(Role.ROLE_ADMIN)){
                throw new AccessDeniedException("Only admin has access to modify master");
            }
            floorEntity.update(floor);
            commonDao.save(floorEntity);
            saveSuccessMessage(status, commonUtil.getText("success.update", status.getLocale()));
            floor = floorEntity;
        } else {
            commonDao.save(floor);
            saveSuccessMessage(status, commonUtil.getText("success.save", status.getLocale()));
        }
        return floor;
    }


    public void deleteFloor(Long id, SpacePlannerResponseStatus status) {
        FloorEntity floorEntity = (FloorEntity) commonDao.get(FloorEntity.class, id);
        floorEntity.setStatus(Status.DELETED);
        floorEntity.setUpdatedAt(new Date());
        commonDao.save(floorEntity);
        saveSuccessMessage(status, commonUtil.getText("success.delete", status.getLocale()));
    }


    public void archiveFloor(Long floorId, SpacePlannerResponseStatus status) {
        FloorEntity floorEntity = (FloorEntity) commonDao.get(FloorEntity.class, floorId);
        floorEntity.setStatus(Status.ARCHIVED);
        floorEntity.setUpdatedAt(new Date());
        commonDao.save(floorEntity);
        saveSuccessMessage(status, commonUtil.getText("success.archive", status.getLocale()));
    }



    public void save(DesignUploadForm designUploadForm, SpacePlannerResponseStatus status) throws IOException, ParseException {
        logger.info("Validating request to updload design...");
        validatorUtil.validate(designUploadForm, status);
        if (status.hasError()) {
            saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
            return;
        }
        FloorEntity floorEntity = (FloorEntity) commonDao.get(FloorEntity.class, designUploadForm.getFloorId());
        List<DesignStatus> designStatusList = getValidDesignStatusForDesignUpload();
        if (!designStatusList.contains(floorEntity.getDesignStatus())) {
            logger.info("Operation not allowed "+ floorEntity.getDesignStatus().name());
            status.setMessage(commonUtil.getText("error.operation.no.allowed", floorEntity.getFloorNumber(), status.getLocale()));
            saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
            return;
        }
        try {
            processDesignDXF(floorEntity, designUploadForm, status);
            if(status.hasError()){
                status.setMessage(commonUtil.getText("error.failed.to.persist.floor.design.details", status.getLocale()));
                saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
            }else{
                processDesignPDF(floorEntity, designUploadForm);
                floorEntity.setDesignUpdateBy(designUploadForm.getUserEntity());
                floorEntity.setUpdatedAt(new Date());
                commonDao.save(floorEntity);
                saveSuccessMessage(status, commonUtil.getText("success.save", status.getLocale()));
            }
        } catch (Exception e) {
            logger.error("Unable to persist floor details:-" + e.getMessage());
            status.setMessage(commonUtil.getText("error.failed.to.persist.floor.design.details", status.getLocale()));
            status.addError("Error:"+e.getMessage());
            saveErrorMessage(status, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


    private void processDesignDXF(FloorEntity floorEntity, DesignUploadForm designUploadForm, SpacePlannerResponseStatus status) throws Exception {
        logger.info("Processing design...");
        if (null == designUploadForm.getDesignDXFFile() || designUploadForm.getDesignDXFFile().getSize() <= 0) {
            logger.error("DXF file size 0");
            return;
        }
        if (floorEntity.getDesignStatus().equals(DesignStatus.Master_Published) || floorEntity.getDesignStatus().equals(DesignStatus.Space_Design_Uploaded)) {
            processSpaceDesign(floorEntity, designUploadForm.getDesignDXFFile(), status);
            if(status.hasError()){
                return;
            }
            floorEntity.setDesignCreatedBy(designUploadForm.getUserEntity());
        } else {
            processBrandDesign(floorEntity, designUploadForm.getDesignDXFFile());
        }
        String fileNme = getFIleName(floorEntity, FILE_TYPE_DXF);
        String filePathForDXF = SpacePlannerUtil.getFilePath(CommonUtil.getProperty("file.location"), floorEntity.getStore().getId(), fileNme);
        FileUtil.write(filePathForDXF, designUploadForm.getDesignDXFFile().getInputStream());
        floorEntity.setAutoCADFileName(fileNme);
    }

    private List<DesignStatus> getValidDesignStatusForDesignUpload() {
        List<DesignStatus> designStatusList = new ArrayList<DesignStatus>();
        designStatusList.add(DesignStatus.Master_Published);
        designStatusList.add(DesignStatus.Space_Design_Uploaded);
        designStatusList.add(DesignStatus.Brand_Master_Published);
        designStatusList.add(DesignStatus.Brand_Design_Uploaded);
        return designStatusList;
    }

    private void processSpaceDesign(FloorEntity floorEntity, MultipartFile designDXFFile, SpacePlannerResponseStatus status) throws IOException, ParseException {
        logger.info("Processing space design...");
        boolean readLocation = CommonUtil.getProperty("dxf.design.read.location").equalsIgnoreCase("true");
        DesignApi designApi = new DesignParserExt(designDXFFile.getInputStream(), false, readLocation);
        List<DesignDetail> designDetails = designApi.getDesignDetails();
        List<FloorDesignDetailsEntity> floorDesignDetailList = new ArrayList<FloorDesignDetailsEntity>();
        Map<String, CategoryDivision> categoryDivisionMap = commonDao.getCategoryDivision();
        double designRetailArea = 0.00;
        for (DesignDetail designDetail : designDetails) {
            FloorDesignDetailsEntity floorDesignDetail = new FloorDesignDetailsEntity();
            floorDesignDetail.setFloor(floorEntity);
            floorDesignDetail.setLocationCode(designDetail.getLocation());
            floorDesignDetail.setDesignArea(designDetail.getArea());
            boolean isNullOrInvalidCategory=false;
            if(StringUtil.isNotNullOrEmpty(designDetail.getCategory())){
                String categoryName =  designDetail.getCategory().replaceAll("\\s","");
                CategoryDivision categoryDivision  =  categoryDivisionMap.get(categoryName.toLowerCase());
                if(null!= categoryDivision){
                    floorDesignDetail.setCategory(categoryDivision.getCategory());
                    floorDesignDetail.setCategoryDivision(categoryDivision);
                }else {
                    isNullOrInvalidCategory=true;
                }
            }
            if(isNullOrInvalidCategory){
                logger.error(commonUtil.getText("error.null.or.invalid.category.name",designDetail.getCategory()));
                status.addError(commonUtil.getText("error.null.or.invalid.category.name",designDetail.getCategory()));
                return;
            }
            if (null != floorDesignDetail.getDesignArea())
                designRetailArea = designRetailArea + floorDesignDetail.getDesignArea();
            floorDesignDetailList.add(floorDesignDetail);
        }

        if (floorEntity.getDesignStatus().equals(DesignStatus.Space_Design_Uploaded)) {
            commonDao.deleteFloorDetails(floorEntity.getId(), floorEntity.getVersion());
        }
        commonDao.saveAll(floorDesignDetailList);
        floorEntity.setNoOfLocation(designDetails.size());
        floorEntity.setDesignStatus(DesignStatus.Space_Design_Uploaded);
        floorEntity.setDesignArea(designRetailArea);
    }

    private void processBrandDesign(FloorEntity floorEntity, MultipartFile designDXFFile) throws IOException, ParseException {
        DesignApi designApi = new DesignParserExt(designDXFFile.getInputStream(), true, false);
        List<FloorDesignDetailsEntity> floorDesignDetailList = commonDao.getFloorDesignDetails(floorEntity.getId());
        Set<DesignDetail> processed = new HashSet<DesignDetail>();
        for (FloorDesignDetailsEntity floorDesignDetail : floorDesignDetailList) {
            List<DesignDetail> designDetails = designApi.getCategoryDesignDetails(floorDesignDetail.getCategory(), floorDesignDetail.getDesignArea());
            if(null != designDetails && designDetails.size()>0){
                DesignDetail selected = null;
                for(DesignDetail designDetail : designDetails){
                    if(!processed.contains(designDetail) && null != floorDesignDetail.getBrand()
                            && StringUtil.isNotNullOrEmpty(designDetail.getBrand())
                            && floorDesignDetail.getBrand().getName().equals(designDetail.getBrand())){
                        selected = designDetail;
                        break;
                    }
                }
                if(null == selected)
                    selected = designDetails.get(0);
                floorDesignDetail.setDesignBrandName(selected.getBrand());
                processed.add(selected);
            }
            //floorDesignDetail.setDesignBrandName(locationBrandNameMap.get(floorDesignDetail.getLocationCode()));
        }
        commonDao.saveAll(floorDesignDetailList);
        floorEntity.setDesignStatus(DesignStatus.Brand_Design_Uploaded);
    }

    private void processDesignPDF(FloorEntity floorEntity, DesignUploadForm designUploadForm) throws IOException {
        if (null != designUploadForm.getDesignPDFFile() && designUploadForm.getDesignPDFFile().getSize() > 0) {
            String fileNme = getFIleName(floorEntity, FILE_TYPE_PDF);
            String filePathForPDF = SpacePlannerUtil.getFilePath(CommonUtil.getProperty("file.location"),floorEntity.getStore().getId(), fileNme);
            FileUtil.write(filePathForPDF, designUploadForm.getDesignPDFFile().getInputStream());
            floorEntity.setPdfFileName(fileNme);
        }
    }

    private Map<String, BrandEntity> convertBrandListIntoMap(List<BrandEntity> brandList) {
        Map<String, BrandEntity> brandMap = new HashMap<String, BrandEntity>();
        for (BrandEntity brandEntity : brandList) {
            brandMap.put(brandEntity.getName().trim(), brandEntity);
        }
        return brandMap;
    }

    
    private String getFIleName(FloorEntity floor, String type){
        return floor.getFloorNumber()+"_"+floor.getVersion()+"_"+floor.getId()+type;
    }


    public void save(FloorUploadForm floorUploadForm, SpacePlannerResponseStatus status) {
        validatorUtil.validate(floorUploadForm, status);
        if (status.hasError()) {
            saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
            return;
        }
        FloorEntity floor = (FloorEntity) commonDao.get(FloorEntity.class, floorUploadForm.getFloorId());
        DesignStatus designStatus = null;
        if (floor.getDesignStatus().equals(DesignStatus.Space_Design_Published)
                || floor.getDesignStatus().equals(DesignStatus.Brand_Master_Uploaded)) {
            designStatus = DesignStatus.Brand_Master_Uploaded;
        } /*else if (floor.getDesignStatus().equals(DesignStatus.Brand_Design_published)
                || floor.getDesignStatus().equals(DesignStatus.Enrichment_Uploaded)) {
            designStatus = DesignStatus.Enrichment_Uploaded;
        }*/ else {
            status.setMessage(commonUtil.getText("error.operation.no.allowed",floor.getFloorNumber(), status.getLocale()));
            saveErrorMessage(status, HttpStatus.NOT_ACCEPTABLE.value());
            return;
        }
        try {
            processMaster(floorUploadForm.getFile(), floor.getId(), status);
            if (!status.hasError()) {
                floor.setDesignStatus(designStatus);
                floor.setUpdateBy(floorUploadForm.getUserEntity());
                floor.setUpdatedAt(new Date());
                commonDao.save(floor);
                notifyUser(floor);
                status.setMessage(commonUtil.getText("success.excel.uploaded", floor.getFloorNumber(), status.getLocale()));
                saveSuccessMessage(status);
                return;
            }/* else {
                status.setError(commonUtil.getText("error.failed.to.process.brand.details", status.getLocale()));
            }*/
        } catch (NumberFormatException e) {
            status.setError(commonUtil.getText("error.file.have.bad.data", status.getLocale()));
        } catch (IOException e) {
            status.setError(commonUtil.getText("error.failed.to.process.brand.details", status.getLocale()));
        }catch (ArrayIndexOutOfBoundsException e){
            logger.error("Unable to process excel:-" +e);
            status.setError(commonUtil.getText("error.failed.to.process.brand.details", status.getLocale()));
        }catch (Exception e){
            logger.error("Something went wrong:-" +e);
            status.setError(commonUtil.getText("error.file.have.bad.data", status.getLocale()));
        }
        saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
    }

    private void processMaster(MultipartFile multipartFile, Long floorId, SpacePlannerResponseStatus status) throws IOException, NumberFormatException {
        List<FloorDesignDetailsEntity> floorDesignDetails = commonDao.getFloorDesignDetails(floorId);
        Map<String, List<SpaceMasterDTO>> catAreaMap = ExcelUtil.readAsCategoryArea(multipartFile.getInputStream());
        Map<String, BrandEntity> brandMap = convertBrandListIntoMap(commonDao.getBrands());
        String error="";
        boolean isValid=true;
        Set<SpaceMasterDTO> processed = new HashSet<SpaceMasterDTO>();
        for (FloorDesignDetailsEntity floorDesignDetail : floorDesignDetails) {
            SpaceMasterDTO spaceMasterDTO = null;//spaceMasterMap.get(floorDesignDetail.getLocationCode());
            List<SpaceMasterDTO> catSpaceMasterDTOS = catAreaMap.get(floorDesignDetail.getCategory()+"-"+floorDesignDetail.getDesignArea());
            if(null != catSpaceMasterDTOS){
                for(SpaceMasterDTO spaceMaster :  catSpaceMasterDTOS){
                    if(!processed.contains(spaceMaster) && StringUtil.isNotNullOrEmpty(spaceMaster.getBrandCode())
                            && StringUtil.isNotNullOrEmpty(spaceMaster.getBrandName())
                            //&& ((null == floorDesignDetail.getBrand() && null != spaceMaster.getLocation() && spaceMaster.getLocation().equals(floorDesignDetail.getLocationCode()))
                            && (null == floorDesignDetail.getBrand()  || floorDesignDetail.getBrand().getName().equals(spaceMaster.getBrandName()))){
                        spaceMasterDTO = spaceMaster;
                        processed.add(spaceMaster);
                    }
                }
            }
            if (null != spaceMasterDTO) {
                if((StringUtil.isNotNullOrEmpty(spaceMasterDTO.getBrandCode()))
                        ||(StringUtil.isNotNullOrEmpty(spaceMasterDTO.getBrandName()))){
                    BrandEntity brandEntity = brandMap.get(spaceMasterDTO.getBrandName());
                    if (null != brandEntity && null != spaceMasterDTO.getBrandCode() && brandEntity.getCode().equals(spaceMasterDTO.getBrandCode().trim())) {
                        updateFloorDesign(floorDesignDetail, brandEntity, spaceMasterDTO);
                    } else {
                        error = "".equals(error)?spaceMasterDTO.getBrandCode()+"-"+spaceMasterDTO.getBrandName()
                                :error+COMMA_SEPARATOR+spaceMasterDTO.getBrandCode()+"-"+spaceMasterDTO.getBrandName();
                        isValid=false;
                    }
                }
            }
        }
        if (isValid) {
            commonDao.saveAll(floorDesignDetails);
        }else{
            status.setError(commonUtil.getText("error.invalid.brand.code.or.brand.name", error, status.getLocale()));
        }
    }

    private void updateFloorDesign(FloorDesignDetailsEntity floorDesignDetail, BrandEntity brandEntity, SpaceMasterDTO spaceMasterDTO){
        floorDesignDetail.setBrand(brandEntity);
        floorDesignDetail.setSisDetails(spaceMasterDTO.getSisDetails());
        floorDesignDetail.setMG(spaceMasterDTO.getMG());
        floorDesignDetail.setPSFPD(spaceMasterDTO.getPSFPD());
        floorDesignDetail.setSales(spaceMasterDTO.getSales());
        floorDesignDetail.setGMV(spaceMasterDTO.getGMV());
        floorDesignDetail.setAgreementMargin(spaceMasterDTO.getAgreementMargin());
        floorDesignDetail.setVistexMargin(spaceMasterDTO.getVistexMargin());
        floorDesignDetail.setGMROF(spaceMasterDTO.getGMROF());
        floorDesignDetail.setSecurityDeposit(spaceMasterDTO.getSecurityDeposit());
        floorDesignDetail.setSdAmount(spaceMasterDTO.getSdAmount());
    }

    public List<FloorDesignDetailsEntity> getFloorDesignDetails(Long floorId) {
        return commonDao.getFloorDesignDetails(floorId);
    }

    public List<FloorDesignDetailsEntity> getFloorDesignDetails(Long storeId, Long floorId, Long brandId, String floorNumber, Status status, Integer version){
        return commonDao.getFloorDesignDetails(storeId,floorId, brandId, floorNumber, status,version);
    }


    public List<BrandEntity> getBrands() {
        return commonDao.getBrands();
    }

    public BrandEntity getBrandById(Long id) {
        return commonDao.getBrandById(id);
    }


    public int getFloorMaxVersion(Long floorId) {
        FloorEntity floorEntity = (FloorEntity) commonDao.get(FloorEntity.class, floorId);
        return commonDao.getArchiveFloorMaxVersion(floorEntity.getStore().getId(), floorEntity.getFloorNumber());
    }


    public FloorEntity getFloorByNameAndVersion(String floorNumber, Integer version, Long storeId) {
        return commonDao.getFloorByNameAndVersion(storeId, floorNumber, version);
    }

    private boolean isUserAuthorizedToPublish(UserEntity user, DesignStatus designStatus){
        boolean isAuthorized = false;
        if(user.isUserInRole(Role.ROLE_ADMIN)){
           isAuthorized=true;
        }else if((designStatus.equals(DesignStatus.Master_Published)||designStatus.equals(DesignStatus.Brand_Master_Published))
                && user.isUserInRole(Role.ROLE_SPACE_PLANNER)){
          isAuthorized=true;
        } else if((designStatus.equals(DesignStatus.Space_Design_Published)|| designStatus.equals(DesignStatus.Brand_Design_published))
                && user.isUserInRole(Role.ROLE_DESIGNER)){
            isAuthorized=true;
        }
        return isAuthorized;
    }


    public FloorEntity publishFloorData(DesignStatus designStatusType,UserEntity user, Long floorId, SpacePlannerResponseStatus status) {
        FloorEntity floor = (FloorEntity) commonDao.get(FloorEntity.class, floorId);
        if(!isUserAuthorizedToPublish(user,designStatusType)){
            throw new AccessDeniedException("User "+user.getName()+" not authorized to publish this content");
        }
        String message = "";
        boolean isValid = true;
        if (floor.getDesignStatus().equals(DesignStatus.Master_Created)
                && designStatusType.equals(DesignStatus.Master_Published)) {
                message = commonUtil.getText("success.publish.master", floor.getFloorNumber());
        } else if (floor.getDesignStatus().equals(DesignStatus.Space_Design_Uploaded)
                && designStatusType.equals(DesignStatus.Space_Design_Published)) {
            if (SpacePlannerUtil.isFloorDesignAreaTolerable(floor.getRetailArea(),floor.getDesignArea())) {
                message = commonUtil.getText("success.publish.space.design", floor.getFloorNumber());
            } else {
                isValid = false;
                message = commonUtil.getText("error.design.area.does.not.match", floor.getFloorNumber());
            }
        } else if (floor.getDesignStatus().equals(DesignStatus.Brand_Master_Uploaded)
                && designStatusType.equals(DesignStatus.Brand_Master_Published)) {
            message = commonUtil.getText("success.publish.brand.master", floor.getFloorNumber());
        } else if (floor.getDesignStatus().equals(DesignStatus.Brand_Design_Uploaded)
                && designStatusType.equals(DesignStatus.Brand_Design_published)) {
            if (commonDao.isValidBrandDesign(floor.getId())) {
                message = commonUtil.getText("success.publish.brand.design", floor.getFloorNumber());
            } else {
                isValid = false;
                message = commonUtil.getText("error.design.brand.is.not.valid", floor.getFloorNumber());
            }
        } else if (floor.getDesignStatus().equals(DesignStatus.Brand_Design_published) && designStatusType.equals(DesignStatus.Published)) {
            /*TODO once sap system integrated need uncomment below line and comment other line*/
            //message = commonUtil.getText("success.publish.to.SAP", floor.getFloorNumber());
            isValid = false;
            message = commonUtil.getText("alert.publish.to.sap.not.supported");
        } else {
            isValid = false;
            message = commonUtil.getText("error.invalid.action", status.getLocale());
        }
        if (isValid) {
            floor.setUpdateBy(user);
            floor.setDesignStatus(designStatusType);
            floor.setUpdatedAt(new Date());
            commonDao.save(floor);
            notifyUser(floor);
            saveSuccessMessage(status, message);
        } else {
            status.setMessage(message);
            saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
        }
        return floor;
    }



    public boolean validateFloorNumber(Long storeId, Long floorId, String floorNumber) {
        FloorEntity floor = commonDao.getFloorByFloorNumber(storeId, floorNumber);
        return null == floor || floor.getId().equals(floorId);
    }


    public boolean validateStoreName(Long storeId, String storeName) {
        StoreEntity store = commonDao.getStoreByName(storeName);
        return null == store || store.getId().equals(storeId);
    }


    public void saveBrandMaster(InputStream inputStream) throws IOException {
        try {
            List<SpaceMasterDTO> spaceMasterDTOList = ExcelUtil.readBrandMaster(inputStream);
            List<BrandEntity> brandEntityList = new ArrayList<BrandEntity>();
            for (SpaceMasterDTO spaceMasterDTO : spaceMasterDTOList) {
                BrandEntity brandEntity = new BrandEntity();
                brandEntity.setCode(spaceMasterDTO.getBrandCode());
                brandEntity.setName(spaceMasterDTO.getBrandName());
                brandEntityList.add(brandEntity);
            }
            commonDao.saveAll(brandEntityList);
        } catch (IOException e) {
            logger.error("Failed to process brand master" + e);
            throw e;
        }
    }


    public List<FloorEntity> getArchiveFloors(Long storeId) {
        return commonDao.getArchiveFloors(storeId);
    }


    public void saveChangeRequest(FloorUploadForm floorUploadForm, SpacePlannerResponseStatus status) throws IOException {
        logger.info("Validating request...");
        validatorUtil.validate(floorUploadForm, status);
        if (status.hasError()) {
            saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
            return;
        }
        FloorEntity floorVO = (FloorEntity) commonDao.get(FloorEntity.class, floorUploadForm.getFloorId());
        if (!floorVO.getDesignStatus().equals(DesignStatus.Published)) {
            status.setError(commonUtil.getText("error.operation.no.allowed", floorVO.getFloorNumber(), status.getLocale()));
        }
        Map<String, SpaceMasterDTO> spaceMasterMap = null;
        try {
            if (!status.hasError())
                spaceMasterMap = ExcelUtil.read(floorUploadForm.getFile().getInputStream());
        } catch (NumberFormatException e) {
            status.setError(commonUtil.getText("error.file.have.bad.data", status.getLocale()));
        } catch (IOException e) {
            status.setError(commonUtil.getText("error.failed.to.process.brand.details", status.getLocale()));
        }
        if (status.hasError()) {
            saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
            return;
        }
        FloorEntity floorVN = new FloorEntity();
        floorVO.copyTo(floorVN);
        floorVN.setUpdateBy(floorUploadForm.getUserEntity());
        List<FloorDesignDetailsEntity> floorDesignDetailListVO = commonDao.getFloorDesignDetails(floorUploadForm.getFloorId());
        Map<String, CategoryDivision> categoryDivisionMap = commonDao.getCategoryDivision();
        List<FloorDesignDetailsEntity> floorDesignDetailListVN = new ArrayList<FloorDesignDetailsEntity>();
        for (FloorDesignDetailsEntity floorDesignDetailVO : floorDesignDetailListVO) {
            FloorDesignDetailsEntity floorDesignDetailVN = new FloorDesignDetailsEntity();
            Map<String, Object> detailsMap = prepareCICOBIBOMap(spaceMasterMap, floorDesignDetailVO, floorUploadForm.getChangeRequestType(), status);
            floorDesignDetailVN.setCategoryDivision(categoryDivisionMap.get(StringUtil.toString(detailsMap.get(COLUMN_CATEGORY))));
            floorDesignDetailVN.setLocationCode(floorDesignDetailVO.getLocationCode());
            floorDesignDetailVN.setDesignArea(floorDesignDetailVO.getDesignArea());
            floorDesignDetailVN.setCategory(StringUtil.toString(detailsMap.get(COLUMN_CATEGORY)));
            floorDesignDetailVN.setSisDetails(StringUtil.toString(detailsMap.get(COLUMN_SIS_DETAILS)));
            floorDesignDetailVN.setBrand((BrandEntity) detailsMap.get(COLUMN_BRAND));
            floorDesignDetailVN.setMG(StringUtil.toString(detailsMap.get(COLUMN_MG)));
            floorDesignDetailVN.setPSFPD(StringUtil.toString(detailsMap.get(COLUMN_PSFPD)));
            floorDesignDetailVN.setSales(StringUtil.toString(detailsMap.get(COLUMN_SALES)));
            floorDesignDetailVN.setGMV(StringUtil.toString(detailsMap.get(COLUMN_GMV)));
            floorDesignDetailVN.setAgreementMargin(StringUtil.toString(detailsMap.get(COLUMN_AGREEMENT_MARGIN)));
            floorDesignDetailVN.setVistexMargin(StringUtil.toString(detailsMap.get(COLUMN_VISTEX_MARGIN)));
            floorDesignDetailVN.setGMROF(StringUtil.toString(detailsMap.get(COLUMN_GMROF)));
            floorDesignDetailVN.setSecurityDeposit((Double) detailsMap.get(COLUMN_SECURITY_DEPOSIT));
            floorDesignDetailVN.setSdAmount((Double) detailsMap.get(COLUMN_SD_AMOUNT));
            floorDesignDetailVN.setRequested((Boolean) detailsMap.get(COLUMN_IS_REQUEST));
            floorDesignDetailVN.setStartDate(DateUtil.changeFormat((Date) detailsMap.get(COLUMN_START_DATE)));
            floorDesignDetailVN.setCategoryDivision((CategoryDivision)detailsMap.get(COLUMN_DIVISION));
            floorDesignDetailVN.setUpdatedAt(floorDesignDetailVN.getUpdatedAt());
            floorDesignDetailVN.setFloor(floorVN);
            floorDesignDetailListVN.add(floorDesignDetailVN);
        }
        if (!status.hasError()) {
            commonDao.save(floorVN);
            commonDao.saveAll(floorDesignDetailListVN);
            floorUploadForm.setFloorId(floorVN.getId());
            floorVO.setUpdatedAt(new Date());
            floorVO.setUpdateBy(floorUploadForm.getUserEntity());
            floorVO.setStatus(Status.ARCHIVED);
            commonDao.save(floorVO);
            ChangeRequestEntity changeRequest = new ChangeRequestEntity();
            changeRequest.setFloor(floorVN);
            changeRequest.setChangeRequestType(floorUploadForm.getChangeRequestType());
            changeRequest.setRequestBy(floorUploadForm.getUserEntity());
            commonDao.save(changeRequest);
            saveSuccessMessage(status, commonUtil.getText("success.save", status.getLocale()));
        } else {
            //status.setError(commonUtil.getText("error.failed.to.process.brand.details", status.getLocale()));
            saveErrorMessage(status, HttpStatus.BAD_REQUEST.value());
        }
    }

    private Map<String, Object> prepareCICOBIBOMap(Map<String, SpaceMasterDTO> spaceMasterMap, FloorDesignDetailsEntity floorDesignDetailVO,
                                                   ChangeRequestType changeRequestType, SpacePlannerResponseStatus status) {
        Map<String, Object> detailsMap = new HashMap<String, Object>();
        detailsMap.put(COLUMN_CATEGORY, floorDesignDetailVO.getCategory());
        detailsMap.put(COLUMN_DIVISION, floorDesignDetailVO.getCategoryDivision());
        Map<String, CategoryDivision> categoryDivisionMap = commonDao.getCategoryDivision();
        String errorLocationCode="";
        boolean isValid=true;
        if (spaceMasterMap.containsKey(floorDesignDetailVO.getLocationCode())) {
            SpaceMasterDTO spaceMasterDTO = spaceMasterMap.get(floorDesignDetailVO.getLocationCode());
            if(StringUtil.isNotNullOrEmpty(spaceMasterDTO.getBrandCode()) || StringUtil.isNotNullOrEmpty(spaceMasterDTO.getBrandName())){
                BrandEntity brand = commonDao.getBrandByNameAndCode(spaceMasterDTO.getBrandCode(), spaceMasterDTO.getBrandName());
                if (null != brand) {
                    detailsMap.put(COLUMN_BRAND, brand);
                } else {
                    errorLocationCode = "".equals(errorLocationCode)?floorDesignDetailVO.getLocationCode()
                            :errorLocationCode+COMMA_SEPARATOR+floorDesignDetailVO.getLocationCode();
                    isValid=false;
                }
                detailsMap.put(COLUMN_MG, spaceMasterDTO.getMG());
                detailsMap.put(COLUMN_PSFPD, spaceMasterDTO.getPSFPD());
                detailsMap.put(COLUMN_SALES, spaceMasterDTO.getSales());
                detailsMap.put(COLUMN_GMV, spaceMasterDTO.getGMV());
                detailsMap.put(COLUMN_AGREEMENT_MARGIN, spaceMasterDTO.getAgreementMargin());
                detailsMap.put(COLUMN_VISTEX_MARGIN, spaceMasterDTO.getVistexMargin());
                detailsMap.put(COLUMN_GMROF, spaceMasterDTO.getGMROF());
                detailsMap.put(COLUMN_SECURITY_DEPOSIT, spaceMasterDTO.getSecurityDeposit());
                detailsMap.put(COLUMN_SD_AMOUNT, spaceMasterDTO.getSdAmount());
                detailsMap.put(COLUMN_SIS_DETAILS, spaceMasterDTO.getSisDetails());
                detailsMap.put(COLUMN_START_DATE, DateUtil.getCurrentDate());
                detailsMap.put(COLUMN_IS_REQUEST,true);
                if (changeRequestType.equals(ChangeRequestType.CICO)) {
                    detailsMap.put(COLUMN_CATEGORY, spaceMasterDTO.getCategory());
                    String catKey =  spaceMasterDTO.getCategory().replaceAll("\\s","");
                    if(null ==categoryDivisionMap.get(catKey)){
                        isValid=false;
                        errorLocationCode = "".equals(errorLocationCode)?floorDesignDetailVO.getLocationCode()
                                :errorLocationCode+COMMA_SEPARATOR+floorDesignDetailVO.getLocationCode();
                    }else{
                        detailsMap.put(COLUMN_DIVISION, categoryDivisionMap.get(catKey));
                    }
                }
            }
        } else {
            detailsMap.put(COLUMN_BRAND, floorDesignDetailVO.getBrand());
            detailsMap.put(COLUMN_MG, floorDesignDetailVO.getMG());
            detailsMap.put(COLUMN_PSFPD, floorDesignDetailVO.getPSFPD());
            detailsMap.put(COLUMN_SALES, floorDesignDetailVO.getSales());
            detailsMap.put(COLUMN_GMV, floorDesignDetailVO.getGMV());
            detailsMap.put(COLUMN_AGREEMENT_MARGIN, floorDesignDetailVO.getAgreementMargin());
            detailsMap.put(COLUMN_VISTEX_MARGIN, floorDesignDetailVO.getVistexMargin());
            detailsMap.put(COLUMN_GMROF, floorDesignDetailVO.getGMROF());
            detailsMap.put(COLUMN_SECURITY_DEPOSIT, floorDesignDetailVO.getSecurityDeposit());
            detailsMap.put(COLUMN_SD_AMOUNT, floorDesignDetailVO.getSdAmount());
            detailsMap.put(COLUMN_SIS_DETAILS, floorDesignDetailVO.getSisDetails());
            detailsMap.put(COLUMN_START_DATE, floorDesignDetailVO.getStartDate());
            detailsMap.put(COLUMN_IS_REQUEST,false);
        }
        if(!isValid){
            if (changeRequestType.equals(ChangeRequestType.CICO)){
                status.setError(commonUtil.getText("error.invalid.brand.code.or.brand.name.or.category.for.location", errorLocationCode, status.getLocale()));
            }else{
                status.setError(commonUtil.getText("error.invalid.brand.code.or.brand.name", errorLocationCode, status.getLocale()));
            }

        }
        return detailsMap;
    }

    private void notifyUser(FloorEntity floor) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("storeName", floor.getStore().getName());
        map.put("floorNumber", floor.getFloorNumber());
        map.put("designStatus", floor.getDesignStatus());
        new NotifyUserTask(spacePlannerBeans).execute(map);
    }


}
