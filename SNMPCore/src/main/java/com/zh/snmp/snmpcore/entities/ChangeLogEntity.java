package com.zh.snmp.snmpcore.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Golyo
 */
@Entity
@Table(name = "DEVICECHANGE")
public class ChangeLogEntity implements BaseEntity<Long>, Serializable {
    private Long id;
    private DeviceEntity device;
    private Date updateTime;
    private DeviceState stateBefore;
    private DeviceState stateAfter;
    private String userName;
    private String description;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne()
    @JoinColumn(name="DEVICEID")
    public DeviceEntity getDevice() {
        return device;
    }

    public void setDevice(DeviceEntity device) {
        this.device = device;
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Basic
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    public DeviceState getStateAfter() {
        return stateAfter;
    }

    public void setStateAfter(DeviceState stateAfter) {
        this.stateAfter = stateAfter;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    public DeviceState getStateBefore() {
        return stateBefore;
    }

    public void setStateBefore(DeviceState stateBefore) {
        this.stateBefore = stateBefore;
    }

    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
