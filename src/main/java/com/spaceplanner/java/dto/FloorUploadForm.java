package com.spaceplanner.java.dto;

import com.spaceplanner.java.model.UserEntity;
import com.spaceplanner.java.model.type.ChangeRequestType;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 24/09/15
 * Time: 7:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class FloorUploadForm {


    private Long storeId;
    private Long floorId;
    private MultipartFile file;
    private UserEntity userEntity;
    private ChangeRequestType changeRequestType;
    private boolean request=false;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public ChangeRequestType getChangeRequestType() {
        return changeRequestType;
    }

    public void setChangeRequestType(ChangeRequestType changeRequestType) {
        this.changeRequestType = changeRequestType;
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }
}
