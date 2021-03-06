package com.zh.snmp.snmpcore.snmp.trap;

import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.message.MaxMessageAppender;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import com.zh.snmp.snmpcore.snmp.SnmpResources;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.CommunityTarget;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
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
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class TrapManager implements CommandResponder, SnmpResources, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrapManager.class);
    
    @Autowired
    private SnmpService snmpService;
    
    @Autowired
    private DeviceService deviceService;
    
    private String trapListenerAddress;
    private AbstractTransportMapping transport;
    private MessageAppender msgAppender = new MaxMessageAppender(100);
    
    private boolean initialized;
    
    public String getTrapListenerAddress() {
        return trapListenerAddress;
    }

    public void setTrapListenerAddress(String trapListenerAddress) {
        this.trapListenerAddress = trapListenerAddress;
    }
    
    public void start() throws IOException {
        msgAppender = new MaxMessageAppender(100);
        try {
            listenTrap(new UdpAddress(trapListenerAddress));  
            initialized = true;
        } catch (Exception e) {
            initialized = false;
            LOGGER.error("Trapmanager initialization failed", e);            
            msgAppender.addMessage("trapManager.initialization.failed");
            msgAppender.finish();
        }
    }

    public void stop() throws IOException {
        transport.close();
        msgAppender.finish();
        initialized = false;
    }

    public MessageAppender getMessageAppender() {
        return msgAppender;
    }
    
    public boolean isRunning() {
        return initialized && transport.isListening();
    }
    
    @Override
    public void processPdu(CommandResponderEvent cmdRespEvent) {   
        DeviceTrapInfo trapInfo = null;
        try {           
            trapInfo = new DeviceTrapInfo(cmdRespEvent);
            LOGGER.debug("Message received from " + trapInfo.getIpAdress());
            msgAppender.addMessage("message.snmp.trapReceived", trapInfo.getIpAdress());
            DeviceEntity device = deviceService.findDeviceByIp(trapInfo.ipAddress);
            if (device == null) {
                int pos = trapInfo.ipAddress.indexOf('/');
                if (pos > 0) {
                    device = deviceService.findDeviceByIp(trapInfo.ipAddress.substring(0, pos));
                }
            }
            if (device != null) {
                snmpService.startSnmpBackgroundProcess(SnmpService.TRAP_USERNAME, device.getId(), msgAppender);                            
            } else {
                msgAppender.addMessage("message.snmp.ipNotConfigured", trapInfo.getIpAdress());
            }
        } catch (IOException e) {
            LOGGER.error("Error while check device ", e);
            if (trapInfo != null) {
                msgAppender.addMessage("message.snmp.trapError", trapInfo.getIpAdress());                
            } else {
                msgAppender.addMessage("message.snmp.trapReadError");                                
            }
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
    }
    
    private static final String NODEID_OID = "";
    
    public static class DeviceTrapInfo implements Serializable {
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
