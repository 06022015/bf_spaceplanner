package com.spaceplanner.java.controller;

import com.spaceplanner.java.bl.CommonBL;
import com.spaceplanner.java.dto.DesignUploadForm;
import com.spaceplanner.java.dto.FloorUploadForm;
import com.spaceplanner.java.exception.SpacePlannerResponseStatus;
import com.spaceplanner.java.model.FloorDesignDetailsEntity;
import com.spaceplanner.java.model.FloorEntity;
import com.spaceplanner.java.model.StoreEntity;
import com.spaceplanner.java.model.UserEntity;
import com.spaceplanner.java.model.master.BrandEntity;
import com.spaceplanner.java.model.type.ChangeRequestType;
import com.spaceplanner.java.model.type.DesignStatus;
import com.spaceplanner.java.util.CommonUtil;
import com.spaceplanner.java.util.ExcelUtil;
import com.spaceplanner.java.util.FileUtil;
import com.spaceplanner.java.util.ServletContextUtil;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 22/06/15
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/comm")
public class CommonController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private CommonBL commonBL;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(HttpServletRequest request) throws Exception {
        logger.info("Displaying Dashboard ");
        return sendRedirect("/comm/floor.html");
    }

    @RequestMapping(value = "/store", method = RequestMethod.GET)
    public String store(HttpServletRequest request) throws Exception {
        List<StoreEntity> stores = commonBL.getActiveStores();
        request.setAttribute("stores", stores);
        return "storeView";
    }

    @RequestMapping(value = "/store/form", method = RequestMethod.GET)
    public String storeForm(HttpServletRequest request, @RequestParam(value = "storeId", required = false) Long storeId) throws Exception {
        StoreEntity storeEntity = null;
        if (null != storeId) {
            storeEntity = commonBL.getStore(storeId);
        } else {
            storeEntity = new StoreEntity();
        }
        return form(request, storeEntity);
    }

    private String form(HttpServletRequest request, StoreEntity storeEntity) {
        request.setAttribute("store", storeEntity);
        request.setAttribute("storeId", storeEntity.getId());
        request.setAttribute("stores", commonBL.getActiveStores());
        if (null != storeEntity.getId()){
            request.setAttribute("floors", commonBL.getActiveFloors(storeEntity.getId()));
            request.setAttribute("showAction", true);
        }
        return "storeForm";
    }

    @RequestMapping(value = "/store/save", method = RequestMethod.POST)
    public String saveStore(HttpServletRequest request, StoreEntity store) throws Exception {
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus();
        commonBL.save(store, status);
        saveStatus(request, status);
        return sendRedirect("/comm/store/form.html?storeId="+store.getId());
    }

    @RequestMapping(value = "/store/delete", method = RequestMethod.GET)
    public String deleteStore(HttpServletRequest request, @RequestParam(value = "storeId") Long storeId) throws Exception {
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus();
        commonBL.deleteStore(storeId, status);
        saveStatus(request, status);
        return sendRedirect("/comm/store.html");
    }


    @RequestMapping(value = "/store/archive", method = RequestMethod.GET)
    public String archiveStore(HttpServletRequest request, @RequestParam(value = "storeId") Long storeId) throws Exception {
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus();
        commonBL.archiveStore(storeId, status);
        saveStatus(request, status);
        return sendRedirect("/comm/store.html");
    }

    @RequestMapping(value = "/floor", method = RequestMethod.GET)
    public String storeFloor(HttpServletRequest request, @RequestParam(value = "storeId", required = false) Long storeId) throws Exception {
        List<FloorEntity> floors = null;
        if(null!= storeId){
            floors = commonBL.getActiveFloors(storeId);
        }else{
          floors = commonBL.getFloors();
        }
        request.setAttribute("floors", floors);
        /*request.setAttribute("showAction", true);*/
        return "floorMaster";
    }

    @RequestMapping(value = "/validate/storeName", method = RequestMethod.GET)
    public String validateStoreName(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(value = "storeId", required = false) Long storeId,
                                      @RequestParam(value = "storeName", required = true) String storeName) throws Exception {
        boolean isValid = false;
        if(null!= storeName && !"".equals(storeName.trim())){
            isValid=commonBL.validateStoreName(storeId,storeName);
        }
        JSONObject object = new JSONObject();
        object.put("isValid", isValid);
        responseAsJSON(response, object);
        return null;
    }

    @RequestMapping(value = "/validate/floorNumber", method = RequestMethod.GET)
    public String validateFloorNumber(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "storeId", required = true) Long storeId,
                                    @RequestParam(value = "floorId", required = false) Long floorId,
                                    @RequestParam(value = "floorNumber", required = true) String floorNumber) throws Exception {
        boolean isValid = false;
        if(null!= floorNumber && !"".equals(floorNumber.trim())){
            isValid=commonBL.validateFloorNumber(storeId,floorId,floorNumber);
        }
        JSONObject object = new JSONObject();
        object.put("isValid", isValid);
        responseAsJSON(response, object);
        return null;
    }



    @RequestMapping(value = "/floor/save", method = RequestMethod.POST)
    public String saveFloor(HttpServletRequest request, FloorEntity floor) throws Exception {
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus();
        floor.setUpdateBy(getCurrentUsers());
        floor.setCreatedBy(getCurrentUsers());
        FloorEntity floorEntity = commonBL.save(floor, status);
        saveStatus(request, status);
        return  sendRedirect("/comm/store/form.html?storeId="+floorEntity.getStore().getId());
    }

    @RequestMapping(value = "/floor/delete", method = RequestMethod.GET)
    public String deleteFloor(HttpServletRequest request, @RequestParam(value = "storeId", required = true) Long storeId,
                              @RequestParam(value = "floorId", required = true) Long floorId) throws Exception {
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus();
        commonBL.deleteFloor(floorId, status);
        saveStatus(request, status);
        return sendRedirect("/comm/store/form.html?storeId="+storeId);
    }

    @RequestMapping(value = "/floor/archive", method = RequestMethod.GET)
    public String archiveFloor(HttpServletRequest request, @RequestParam(value = "storeId") Long storeId,
                              @RequestParam(value = "floorId") Long floorId) throws Exception {
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus();
        commonBL.archiveFloor(floorId, status);
        saveStatus(request, status);
        return sendRedirect("/comm/store/form.html?storeId="+storeId);
    }


    @RequestMapping(value = "/floor/report", method = RequestMethod.GET)
    public String floorReport(HttpServletRequest request,
                              @RequestParam(value = "storeId", required = false) Long storeId,
                              @RequestParam(value = "floorId", required = false) Long floorId) throws Exception {
        request.setAttribute("stores", commonBL.getActiveStores());
        List<FloorEntity> floorList = null;
        if (null != storeId){
            request.setAttribute("storeId", storeId);
            floorList =  commonBL.getActiveFloors(storeId);
            request.setAttribute("floors",floorList);
        }
        if(null!=floorId){
            request.setAttribute("floorId", floorId);
            List<FloorDesignDetailsEntity> floorDesignDetailList = commonBL.getFloorDesignDetails(floorId);
            DesignStatus designStatus =null;
            if(null != floorDesignDetailList && floorDesignDetailList.size()>0){
                designStatus = floorDesignDetailList.get(0).getFloor().getDesignStatus();
            }else{
               for(FloorEntity floorEntity : floorList){
                   if(floorEntity.getId().equals(floorId)){
                       designStatus = floorEntity.getDesignStatus();
                   }
               }
            }
            request.setAttribute("floorDetails", floorDesignDetailList);
            request.setAttribute("designStatus", designStatus);
        }
        return "reportView";
    }

    @RequestMapping(value = "/floor/autocad/form", method = RequestMethod.GET)
    public String floorUploadAutoCAD(HttpServletRequest request,
                                         @RequestParam(value = "storeId", required = false) Long storeId,
                                         @RequestParam(value = "floorId", required = false) Long floorId) throws Exception {
        DesignUploadForm designUploadForm = new DesignUploadForm();
        designUploadForm.setStoreId(storeId);
        designUploadForm.setFloorId(floorId);
        return form(request, designUploadForm);
    }
    
    
    private String form(HttpServletRequest request, DesignUploadForm designUploadForm){
        request.setAttribute("design", designUploadForm);
        request.setAttribute("stores", commonBL.getActiveStores());
        if(null != designUploadForm.getStoreId()){
            StoreEntity storeEntity = new StoreEntity();
            storeEntity.setId(designUploadForm.getStoreId());
            request.setAttribute("store", storeEntity);
            request.setAttribute("storeId", storeEntity.getId());
            request.setAttribute("floors", commonBL.getActiveFloors(designUploadForm.getStoreId()));
        }
        return "uploadAutocad";
    }

    @RequestMapping(value = "/floor/autocad/save", method = RequestMethod.POST)
    public String saveFloorUploadAutoCAD(HttpServletRequest request, DesignUploadForm designUploadForm) throws Exception {
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus();
        commonBL.save(designUploadForm, status);
        saveStatus(request, status);
        if(status.getCode()!=HttpStatus.OK.value()){
            return sendRedirect("/comm/floor/autocad/form.html?storeId="+designUploadForm.getStoreId()+"&floorId="+designUploadForm.getFloorId());

        }
        return sendRedirect("/comm/floor/report.html?storeId="+designUploadForm.getStoreId()+"&floorId="+designUploadForm.getFloorId());
    }

    @RequestMapping(value = "/floor/upload/form", method = RequestMethod.GET)
    public String floorUploadForm(HttpServletRequest request,
                                  @RequestParam(value = "storeId", required = false) Long storeId,
                                  @RequestParam(value = "floorId", required = false) Long floorId,
                                  @RequestParam(value = "isRequest", required = false) Boolean isRequest) throws Exception {
        FloorUploadForm floorUploadForm = new FloorUploadForm();
        floorUploadForm.setStoreId(storeId);
        floorUploadForm.setFloorId(floorId);
        if(null!=isRequest)
            floorUploadForm.setRequest(isRequest);
        return form(request, floorUploadForm);
    }
    
    private String form(HttpServletRequest request, FloorUploadForm floorUploadForm){
        request.setAttribute("uploadForm", floorUploadForm);
        request.setAttribute("stores", commonBL.getActiveStores());
        request.setAttribute("isRequest", floorUploadForm.isRequest());
        if(null != floorUploadForm.getStoreId()){
            request.setAttribute("storeId", floorUploadForm.getStoreId());
            request.setAttribute("floors", commonBL.getActiveFloors(floorUploadForm.getStoreId()));
        }
        return "floorUploadForm";
    }


    @RequestMapping(value = "/floor/upload/save", method = RequestMethod.POST)
    public String saveFloorEnrich(HttpServletRequest request,FloorUploadForm floorUploadForm) throws Exception {
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus();
        floorUploadForm.setUserEntity(getCurrentUsers());
        if(floorUploadForm.getChangeRequestType().equals(ChangeRequestType.NEW)){
            commonBL.save(floorUploadForm, status);
            floorUploadForm.setRequest(false);
        }else{
            commonBL.saveChangeRequest(floorUploadForm, status);
            floorUploadForm.setRequest(true);
        }
        saveStatus(request, status);
        String RETURN_PATH = "";
        if(status.getCode()!=HttpStatus.OK.value()){
            RETURN_PATH = sendRedirect("/comm/floor/upload/form.html?storeId="+floorUploadForm.getStoreId()+"&floorId="
                    +floorUploadForm.getFloorId())+"&isRequest="+floorUploadForm.isRequest();
        }else{
            RETURN_PATH = sendRedirect("/comm/floor/report.html?storeId="+floorUploadForm.getStoreId()+"&floorId="
                    +floorUploadForm.getFloorId());
        }
        return RETURN_PATH;
    }

    @RequestMapping(value = "/store/download", method = RequestMethod.GET)
    public String downloadStore(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "storeId", required = true) Long storeId) throws Exception {
        try {
            StoreEntity store = commonBL.getStore(storeId);
            List<FloorEntity> floorList = commonBL.getActiveFloors(storeId);
            response.setContentType("application/vnd.ms-excel");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",store.getName()+ ".xls");
            response.setHeader(headerKey, headerValue);
            ExcelUtil.write(floorList,store, response.getOutputStream());
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value = "/floor/download", method = RequestMethod.GET)
    public String downloadFloor(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(value = "floorId", required = true) Long floorId,
                                  @RequestParam(value = "version", required = false) Integer version,
                                  @RequestParam(value = "type", required = true) String type) throws Exception {
        try {
            if (null != type && !"".equals(type)) {
                boolean isExcel = false;
                FloorEntity floorEntity = commonBL.getFloor(floorId);
                if (null != version) {
                    floorEntity = commonBL.getFloorByNameAndVersion(floorEntity.getFloorNumber(), floorEntity.getVersion(), floorEntity.getStore().getId());
                }
                String baseLocation = CommonUtil.getProperty("file.location");
                String filePath = baseLocation + floorEntity.getStore().getId() + "/";
                String fileName = null;
                if ("PDF".equalsIgnoreCase(type)) {
                    fileName = floorEntity.getPdfFileName();
                } else if ("DXF".equalsIgnoreCase(type)) {
                    fileName = floorEntity.getAutoCADFileName();
                } else if ("excel".equalsIgnoreCase(type)) {
                    isExcel = true;
                    response.setContentType("application/vnd.ms-excel");
                    fileName = floorEntity.getStore().getName() + "_" + floorEntity.getFloorNumber()+"_"+floorEntity.getVersion() + ".xls";
                }
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"",fileName);
                response.setHeader(headerKey, headerValue);
                if (isExcel) {
                    List<FloorDesignDetailsEntity> floorDesignDetails = commonBL.getFloorDesignDetails(floorEntity.getId());
                    List<BrandEntity> brandList = commonBL.getBrands();
                    ExcelUtil.write(floorDesignDetails,floorEntity,brandList, response.getOutputStream());
                }else{
                    filePath = filePath + "/" + fileName;
                    response.setContentType(ServletContextUtil.getServletContext().getMimeType(filePath));
                    FileUtil.download(filePath, response.getOutputStream());
                }
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value = "/floor/publish", method = RequestMethod.GET)
    public String publishFloorData(HttpServletRequest request,
                                  @RequestParam(value = "type", required = true) DesignStatus designStatusType,
                                  @RequestParam(value = "floorId", required = true) Long floorId) throws Exception {
        SpacePlannerResponseStatus status = new SpacePlannerResponseStatus();
        UserEntity user = getCurrentUsers();
        FloorEntity floorEntity = commonBL.publishFloorData(designStatusType,user, floorId, status);

        saveStatus(request, status);
        String target = request.getParameter("target");
        if(null!=target && !"".equals(target) && "report".equals(target)){
            return sendRedirect("/comm/floor/report.html?storeId="+floorEntity.getStore().getId())+"&floorId="+floorId;
        }
        return sendRedirect("/comm/store/form.html?storeId="+floorEntity.getStore().getId());
    }

    @RequestMapping(value = "/archive/view", method = RequestMethod.GET)
    public String archiveView(HttpServletRequest request,
                              @RequestParam(value = "storeId", required = false) Long storeId,
                              @RequestParam(value = "floorId", required = false) Long floorId,
                              @RequestParam(value = "version", required = false) Integer version) throws Exception {
        request.setAttribute("stores", commonBL.getActiveStores());
        if(null!=storeId){
            request.setAttribute("storeId", storeId);
            request.setAttribute("floors", commonBL.getArchiveFloors(storeId));
        }
        if(null!= floorId){
            request.setAttribute("floorId", floorId);
            request.setAttribute("maxVersion", commonBL.getFloorMaxVersion(floorId));
        }
        if(null!=version){
            request.setAttribute("version", version);
            List<FloorDesignDetailsEntity> floorDesignDetails = commonBL.getFloorDesignDetails(floorId);
            request.setAttribute("floorDetails", floorDesignDetails);
            request.setAttribute("designStatus", floorDesignDetails.get(0).getFloor().getDesignStatus());
        }
        return "archiveView";
    }
}
