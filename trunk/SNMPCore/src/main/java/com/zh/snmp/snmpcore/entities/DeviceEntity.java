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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author Golyo
 */
@Entity
@Table(name = "DEVICE")
public class DeviceEntity implements BaseEntity, Serializable {
    private Long id;
    private String nodeId;
    private String macAddress;
    private String ipAddress;
    private Set<DeviceConfigEntity> configurations;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @ManyToMany()
    @JoinTable(name="CONFIGMAP", 
            joinColumns=@JoinColumn(name="DEVICEID", referencedColumnName="ID"),
            inverseJoinColumns=@JoinColumn(name="CONFIGID", referencedColumnName="ID"))
    public Set<DeviceConfigEntity> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Set<DeviceConfigEntity> configurations) {
        this.configurations = configurations;
    }

    @Basic
    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Basic
    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
    public DeviceConfigEntity findConfiguration(DeviceType type) {
        if (configurations != null) {
            for (DeviceConfigEntity conf: configurations) {
                if (conf.getDeviceType() == type) {
                    return conf;
                }
            }            
        }
        return null;
    }
    
    public DeviceConfigEntity changeConfig(DeviceConfigEntity newConfig) {
        if (configurations == null) {
            configurations = new HashSet<DeviceConfigEntity>();
        }
        Iterator<DeviceConfigEntity> it = configurations.iterator();
        DeviceConfigEntity old = null;
        DeviceConfigEntity act;
        while (old == null && it.hasNext()) {
            act = it.next();
            if (act.getDeviceType() == newConfig.getDeviceType()) {
                old = act;
                it.remove();
            }
        }
        configurations.add(newConfig);
        setConfigurations(configurations);
        return old;
    }
    
    @Override
    public String toString() {
        return "node:" + nodeId + ", ip:" + ipAddress + ", configs: " + getConfigurations(); 
    }
}
