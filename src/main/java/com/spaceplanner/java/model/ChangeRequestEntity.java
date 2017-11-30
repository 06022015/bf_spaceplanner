package com.spaceplanner.java.model;

import com.spaceplanner.java.model.type.ChangeRequestType;
import com.spaceplanner.java.model.type.Status;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 13/09/15
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name = "change_request")
public class ChangeRequestEntity  extends BaseEntity{

    private Long id;
    private ChangeRequestType changeRequestType;
    private String requestFileName;
    private FloorEntity floor;
    private Status status=Status.CREATED;
    private UserEntity requestBy;



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Enumerated
    public ChangeRequestType getChangeRequestType() {
        return changeRequestType;
    }

    public void setChangeRequestType(ChangeRequestType changeRequestType) {
        this.changeRequestType = changeRequestType;
    }

    public String getRequestFileName() {
        return requestFileName;
    }

    public void setRequestFileName(String requestFileName) {
        this.requestFileName = requestFileName;
    }

    @ManyToOne()
    public FloorEntity getFloor() {
        return floor;
    }

    public void setFloor(FloorEntity floor) {
        this.floor = floor;
    }

    @Enumerated
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public UserEntity getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(UserEntity requestBy) {
        this.requestBy = requestBy;
    }
}
