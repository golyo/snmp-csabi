/*
 *   Copyright (c) 2010 Sonrisa Informatikai Kft. All Rights Reserved.
 * 
 *  This software is the confidential and proprietary information of
 *  Sonrisa Informatikai Kft. ("Confidential Information").
 *  You shall not disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with Sonrisa.
 * 
 *  SONRISA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 *  THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SONRISA SHALL NOT BE LIABLE FOR
 *  ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 *  DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.zh.snmp.snmpcore.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author Golyo
 */
//@Entity
//@Table(name = "HISTORY")
public class HistoryEntity implements BaseEntity<Long>, Serializable {
    private Long id;
    private DeviceEntity device;
    private DeviceConfigEntity oldConfig;
    private DeviceConfigEntity newConfig;
    private UserEntity user;
    private Date updateTime;

//    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
//    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    @ManyToOne()
//    @JoinColumn(name="DEVICEID")
    public DeviceEntity getDevice() {
        return device;
    }

    public void setDevice(DeviceEntity device) {
        this.device = device;
    }

//    @ManyToOne()
//    @JoinColumn(name="NEWCONFIGID")
    public DeviceConfigEntity getNewConfig() {
        return newConfig;
    }

    public void setNewConfig(DeviceConfigEntity newConfig) {
        this.newConfig = newConfig;
    }

//    @ManyToOne()
//    @JoinColumn(name="OLDCONFIGID")
    public DeviceConfigEntity getOldConfig() {
        return oldConfig;
    }

    public void setOldConfig(DeviceConfigEntity oldConfig) {
        this.oldConfig = oldConfig;
    }

//    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
//    @Basic
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

//    @ManyToOne()
//    @JoinColumn(name="USERID")
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }    
}
