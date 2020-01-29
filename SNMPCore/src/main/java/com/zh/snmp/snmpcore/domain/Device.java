package com.zh.snmp.snmpcore.domain;

import com.zh.snmp.snmpcore.entities.DeviceState;
import java.io.Serializable;

/**
 *
 * @author Golyo
 */
public class Device implements Serializable {
    private String deviceId;
    private String nodeId;
    private String macAddress;
    private String ipAddress;
    private DeviceState configState;
    private Configuration config;
    private DeviceNode configMap;

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public DeviceNode getConfigMap() {
        return configMap;
    }

    public void setConfigMap(DeviceNode configMap) {
        this.configMap = configMap;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public DeviceState getConfigState() {
        return configState;
    }

    public void setConfigState(DeviceState configState) {
        this.configState = configState;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Device other = (Device) obj;
        if ((this.deviceId == null) ? (other.deviceId != null) : !this.deviceId.equals(other.deviceId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.deviceId != null ? this.deviceId.hashCode() : 0);
        return hash;
    }
    
    
}
