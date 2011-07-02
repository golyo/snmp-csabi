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

import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.CommunityTarget;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class TrapManager implements CommandResponder {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrapManager.class);
    
    @Autowired
    private SnmpService service;
    
    private String trapListenerAddress;
    private DeviceChecker deviceChecker;

    public void TrapManager(String trapListenerAddress) {
        this.trapListenerAddress = trapListenerAddress;
        deviceChecker = new DeviceChecker();
    }

    public void start() throws IOException {
        deviceChecker.start();
        listenTrap(new UdpAddress(null));
    }

    public void stop() throws IOException {
        deviceChecker.stop();
    }

    @Override
    public void processPdu(CommandResponderEvent cmdRespEvent) {
        LOGGER.info("Start process pdu ");
        PDU pdu = cmdRespEvent.getPDU();        
        try {
            DeviceTrapInfo trapInfo = new DeviceTrapInfo(pdu.getVariableBindings());
            DeviceEntity device = service.findDeviceByNodeId(trapInfo.nodeId);
            if (device == null) {
                LOGGER.error("Unregistered device found to " + trapInfo.nodeId + " at " + trapInfo.ipAddress);
                return;
            }
            String config = deviceChecker.getConfiguration(trapInfo);
            /*
            if (config != null) {
                if (!config.equals(device.getConfig().getCode())) {
                    deviceChecker.configureDevice(trapInfo, device.getConfig());
                }
            } else {
                //TODO must reset config
            }
             * 
             */
        } catch (IOException e) {
            LOGGER.error("Error while check device ", e);
        }
    }

    
    private void listenTrap(TransportIpAddress address) throws IOException {
        AbstractTransportMapping transport;
        if (address instanceof TcpAddress) {
            transport = new DefaultTcpTransportMapping((TcpAddress) address);
        } else {
            transport = new DefaultUdpTransportMapping((UdpAddress) address);
        }

        ThreadPool threadPool = ThreadPool.create("DispatcherPool", 10);
        MessageDispatcher mtDispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());

        // add message processing models
        mtDispatcher.addMessageProcessingModel(new MPv1());
        mtDispatcher.addMessageProcessingModel(new MPv2c());

        // add all security protocols
        SecurityProtocols.getInstance().addDefaultProtocols();
        SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

        //Create Target
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));

        Snmp snmp = new Snmp(mtDispatcher, transport);
        snmp.addCommandResponder(this);

        transport.listen();
        System.out.println("Listening on " + address);

        try {
            this.wait();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    
    private static final String IPADDRESS_OID = "";
    private static final String NODEID_OID = "";
    
    public static class DeviceTrapInfo {
        private String nodeId;
        private String ipAddress;

        private DeviceTrapInfo(List<VariableBinding> variables) throws IOException {
            for (VariableBinding vb: variables) {
                if (vb.getOid().toString().equals(NODEID_OID)) {
                    nodeId = vb.getVariable().toString();
                } else if (vb.getOid().toString().equals(IPADDRESS_OID)) {
                    ipAddress = vb.getVariable().toString();
                }
            }
            if (nodeId == null) {
                throw new IOException("nodeId not defined at " + IPADDRESS_OID + " variables are: " + variables);
            }
            if (ipAddress == null) {
                throw new IOException("ipAddress not defined at " + IPADDRESS_OID + " variales are: " + variables);
            }
        }

        public String getNodeId() {
            return nodeId;
        }

        public String getIpAdress() {
            return ipAddress;
        }
    }
}
