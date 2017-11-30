package com.spaceplanner.java.model;

import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * UserEntity: Ashif.Qureshi
 * Date: 21/8/14
 * Time: 11:05 AM
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public class BaseEntity implements java.io.Serializable {

    private Date createdAt=new Date();
    private Date updatedAt=new Date();

    @javax.persistence.Column(name = "created_at", nullable = true, insertable = true, updatable = false, length = 29, precision = 6)
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @javax.persistence.Column(name = "updated_at", nullable = true, insertable = true, updatable = true, length = 29, precision = 6)
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
