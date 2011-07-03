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
package com.zh.snmp.snmpcore.snmp.mib;

import org.snmp4j.CommunityTarget;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;

/**
 *
 * @author Golyo
 */
public class DeviceSettings {    
    public static final DeviceSettings TEST_DEVICE = new DeviceSettings("public", "private", "192.168.2.253", "161", "162", SnmpConstants.version2c);
    
    private String readCommunity;
    private String writeCommunity;
    private String ipAddress;
    private String port;
    private String trapPort;
    private int snmpVersion;
    
    public DeviceSettings(String readCommunity, String writeCommunity, String ipAddress, String port, String trapPort, int snmpVersion) {
        this.readCommunity = readCommunity;
        this.writeCommunity = writeCommunity;
        this.ipAddress = ipAddress;
        this.port = port;
        this.trapPort = trapPort;
        this.snmpVersion = snmpVersion;
        
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPort() {
        return port;
    }

    public String getReadCommunity() {
        return readCommunity;
    }

    public int getSnmpVersion() {
        return snmpVersion;
    }

    public String getTrapPort() {
        return trapPort;
    }

    public String getWriteCommunity() {
        return writeCommunity;
    }
    
    public String getUdpAddress() {
        return ipAddress + "/" + port;
    }
    
    public CommunityTarget createTarget(boolean write) {
        CommunityTarget comtarget = new CommunityTarget();
        comtarget.setCommunity(new OctetString(write ? getWriteCommunity() : getReadCommunity()));
        comtarget.setVersion(getSnmpVersion());
        comtarget.setAddress(new UdpAddress(getUdpAddress()));
        comtarget.setRetries(2);
        comtarget.setTimeout(1000);
        return comtarget;
    }
}
