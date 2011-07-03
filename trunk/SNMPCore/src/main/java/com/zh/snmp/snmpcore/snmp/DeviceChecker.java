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
package com.zh.snmp.snmpcore.snmp;

import com.zh.snmp.snmpcore.snmp.trap.TrapManager;
import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import java.io.IOException;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 *
 * @author Golyo
 */
public class DeviceChecker {
    static final OID OID_CONFIG = new OID(".1.3.6.1.2.1.1.1.0");
    
    private static final String COMMUNITY = "public";
    private static int SNMP_VERSION = SnmpConstants.version1;
    private static String PORT = "161";
    
    private TransportMapping transport;
    private Snmp snmp;
    
    public DeviceChecker() {
    }
    
    public void start() throws IOException {
        transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();     
    }
    
    public void stop() throws IOException {
        snmp.close();
    }
    
    public String getConfiguration(TrapManager.DeviceTrapInfo trapInfo) throws IOException {
        PDU pdu = createGetConfigPdu();
        CommunityTarget target = createTarget(trapInfo);
        ResponseEvent event = snmp.send(pdu, target, null);
        if (event != null) {
            return event.getResponse().get(0).getVariable().toString();
        } else {
            //TIMEOUT
            throw new IOException("Time out on " + trapInfo.getIpAdress());
        }
    }
    
    public boolean configureDevice(TrapManager.DeviceTrapInfo trapInfo, DeviceConfigEntity config) {
        return true;
    }
    
    private PDU createGetConfigPdu() {
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(OID_CONFIG));
        pdu.setType(PDU.GET);
        return pdu;
    }
    
    private CommunityTarget createTarget(TrapManager.DeviceTrapInfo trapInfo) {
        CommunityTarget comtarget = new CommunityTarget();
        comtarget.setCommunity(new OctetString(COMMUNITY));
        comtarget.setVersion(SNMP_VERSION);
        comtarget.setAddress(new UdpAddress(trapInfo.getIpAdress() + "/" + PORT));
        comtarget.setRetries(2);
        comtarget.setTimeout(1000);
        return comtarget;
        
    }
    
}
