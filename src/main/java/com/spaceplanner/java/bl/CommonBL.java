package com.spaceplanner.java.bl;

import com.spaceplanner.java.dto.DesignUploadForm;
import com.spaceplanner.java.dto.FloorUploadForm;
import com.spaceplanner.java.exception.SpacePlannerResponseStatus;
import com.spaceplanner.java.model.FloorDesignDetailsEntity;
import com.spaceplanner.java.model.FloorEntity;
import com.spaceplanner.java.model.StoreEntity;
import com.spaceplanner.java.model.UserEntity;
import com.spaceplanner.java.model.master.BrandEntity;
import com.spaceplanner.java.model.type.DesignStatus;
import org.kabeja.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 13/07/15
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CommonBL {

    void executeSQLQueryUpdate(String sqlQuery);

    StoreEntity getStore(Long id);

    List<StoreEntity>  getActiveStores();

    StoreEntity save(StoreEntity storeEntity, SpacePlannerResponseStatus status);
    
    void deleteStore(Long id, SpacePlannerResponseStatus status);

    void archiveStore(Long storeId, SpacePlannerResponseStatus status);

    FloorEntity getFloor(Long id);
    
    List<FloorEntity> getActiveFloors(Long storeId);

    List<FloorEntity> getFloors();
    


    FloorEntity save(FloorEntity floorEntity, SpacePlannerResponseStatus status);

    void deleteFloor(Long id, SpacePlannerResponseStatus status);

    void archiveFloor(Long floorId, SpacePlannerResponseStatus status);

    void save(DesignUploadForm designUploadForm, SpacePlannerResponseStatus status) throws IOException, ParseException;

    void save(FloorUploadForm enrichUploadForm, SpacePlannerResponseStatus status) throws IOException;

    List<FloorDesignDetailsEntity> getFloorDesignDetails(Long floorId);
    
    List<BrandEntity>  getBrands();

    
    int getFloorMaxVersion(Long floorId);
    
    FloorEntity getFloorByNameAndVersion(String floorNumber, Integer version, Long storeId);

    FloorEntity publishFloorData(DesignStatus designStatusType, UserEntity user, Long floorId, SpacePlannerResponseStatus status);

    /*validate*/

    boolean validateFloorNumber(Long storeId,Long floorId, String floorNumber);


    boolean validateStoreName(Long storeId, String storeName);


    void saveBrandMaster(InputStream inputStream) throws IOException;

    /*Archive data*/
    List<FloorEntity> getArchiveFloors(Long storeId);


    void saveChangeRequest(FloorUploadForm floorUploadForm, SpacePlannerResponseStatus status) throws IOException;



}
