package com.spaceplanner.java.dto;

import com.spaceplanner.java.model.UserEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 24/09/15
 * Time: 7:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class DesignUploadForm {
    
    
    private Long storeId;
    private Long floorId;
    private MultipartFile designDXFFile;
    private MultipartFile designPDFFile;
    private UserEntity userEntity;


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

    public MultipartFile getDesignDXFFile() {
        return designDXFFile;
    }

    public void setDesignDXFFile(MultipartFile designDXFFile) {
        this.designDXFFile = designDXFFile;
    }

    public MultipartFile getDesignPDFFile() {
        return designPDFFile;
    }

    public void setDesignPDFFile(MultipartFile designPDFFile) {
        this.designPDFFile = designPDFFile;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
