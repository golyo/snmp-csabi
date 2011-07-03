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
package com.zh.snmp.snmpcore.snmp.trap;

import com.zh.snmp.snmpcore.message.MaxMessageAppender;
import com.zh.snmp.snmpcore.message.MessageAppender;
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
import org.snmp4j.mp.StateReference;
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

/**
 *
 * @author Golyo
 */
public class TrapManager implements CommandResponder {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrapManager.class);
    private final static String MSG_TRAP_RECEIVED = "snmp.trapReceived";
    //private final Object synchObj = new Object();
    //private boolean interrupted;
    
    private String trapListenerAddress;
    private TrapListener trapListener;
    private AbstractTransportMapping transport;
    private MessageAppender msgAppender = new MaxMessageAppender(10);
    

    
    public TrapListener getTrapListener() {
        return trapListener;
    }

    public void setTrapListener(TrapListener trapListener) {
        this.trapListener = trapListener;
    }

    public String getTrapListenerAddress() {
        return trapListenerAddress;
    }

    public void setTrapListenerAddress(String trapListenerAddress) {
        this.trapListenerAddress = trapListenerAddress;
    }
    
    public void start() throws IOException {
        listenTrap(new UdpAddress(trapListenerAddress));
        /*
        synchronized (synchObj) {
            while (!interrupted) {
                try {
                    synchObj.wait();                    
                } catch (InterruptedException e) {
                    LOGGER.debug("Thread interrupted");
                }
            }
        }
         * 
         */
    }

    public void stop() throws IOException {
        transport.close();
        /*
        synchronized (synchObj) {
            interrupted = true;
            synchObj.notifyAll();
            transport.close();
        } 
         * 
         */
    }

    public MessageAppender getMessageAppender() {
        return msgAppender;
    }
    
    @Override
    public void processPdu(CommandResponderEvent cmdRespEvent) {        
        try {
            DeviceTrapInfo trapInfo = new DeviceTrapInfo(cmdRespEvent);
            if (trapListener != null) {
                msgAppender.addMessage(MSG_TRAP_RECEIVED, trapInfo);
                trapListener.processTrapResponse(trapInfo, msgAppender);
            }            
            /*
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
        LOGGER.debug("Listening on " + address);
/*
        try {
            this.stop();
            this.wait();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
 * 
 */
    }
    
    private static final String NODEID_OID = "";
    
    public static class DeviceTrapInfo {
        private String nodeId;
        private String ipAddress;

        private DeviceTrapInfo(CommandResponderEvent cmdRespEvent) throws IOException {
            List<VariableBinding> variables = cmdRespEvent.getPDU().getVariableBindings();
            StateReference stateReference = cmdRespEvent.getStateReference();
            ipAddress = stateReference.getAddress().toString();
            for (VariableBinding vb: variables) {
                if (vb.getOid().toString().equals(NODEID_OID)) {
                    nodeId = vb.getVariable().toString();
                }
            }
            /*
            if (nodeId == null) {
                throw new IOException("nodeId not defined at " + IPADDRESS_OID + " variables are: " + variables);
            }
             * 
             */
            if (ipAddress == null) {
                throw new IOException("ipAddress not defined");
            }
        }

        public String getNodeId() {
            return nodeId;
        }

        public String getIpAdress() {
            return ipAddress;
        }
        
        @Override
        public String toString() {
            return ipAddress + ", " + nodeId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DeviceTrapInfo other = (DeviceTrapInfo) obj;
            if ((this.ipAddress == null) ? (other.ipAddress != null) : !this.ipAddress.equals(other.ipAddress)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + (this.ipAddress != null ? this.ipAddress.hashCode() : 0);
            return hash;
        }
        
        
    }
}
