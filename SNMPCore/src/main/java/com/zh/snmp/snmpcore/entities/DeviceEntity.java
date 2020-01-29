package com.zh.snmp.snmpcore.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 *
 * @author Golyo
 */
@Entity
@Table(name = "DEVICE")
public class DeviceEntity implements BaseEntity<String>, Serializable {
    private String deviceId;
    private String nodeId;
    private String macAddress;
    private String ipAddress;
    private String configCode;
    private String deviceMap;
    private DeviceState configState;

    @Id
    @Column(name="DEVICEID")
    @Override
    public String getId() {
        return deviceId;
    }

    public void setId(String id) {
        this.deviceId = id;
    }

    @Basic
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Basic
    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    @Basic
    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }


    @Basic
    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Lob
    @Basic
    public String getDeviceMap() {
        return deviceMap;
    }

    public void setDeviceMap(String deviceMap) {
        this.deviceMap = deviceMap;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    public DeviceState getConfigState() {
        return configState;
    }

    public void setConfigState(DeviceState configState) {
        this.configState = configState;
    }


}
