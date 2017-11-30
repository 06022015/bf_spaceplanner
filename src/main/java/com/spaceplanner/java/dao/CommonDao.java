package com.spaceplanner.java.dao;

import com.spaceplanner.java.model.FloorDesignDetailsEntity;
import com.spaceplanner.java.model.FloorEntity;
import com.spaceplanner.java.model.StoreEntity;
import com.spaceplanner.java.model.master.BrandEntity;
import com.spaceplanner.java.model.master.CategoryDivision;
import com.spaceplanner.java.model.type.Status;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 13/07/15
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CommonDao extends BaseDao{

    List<StoreEntity> getStores(Status status);
    
    List<FloorEntity> getFloors(Long storeId, Status status);

    List<FloorEntity> getArchiveFloors(Long storeId);


    List<FloorEntity> getRecentFloors(int length, Status status);

    FloorEntity getFloorByFloorNumber(Long storeId, String floorNumber);

    Integer getArchiveFloorMaxVersion(Long storeId, String floorNumber);

    FloorEntity getFloorByNameAndVersion(Long storeId, String floorNumber, Integer version);
    
    
    List<FloorDesignDetailsEntity> getFloorDesignDetails(Long floorId);

    void deleteFloorDetails(Long floorId, int version);

    StoreEntity getStoreByName(String storeName);

    BrandEntity getBrandByNameAndCode(String code, String name);

     List<BrandEntity> getBrands();

    boolean isValidBrandDesign(Long floorId);

    CategoryDivision getCategoryDivisionByCategory(String category);

    Map<String, CategoryDivision> getCategoryDivision();
}
