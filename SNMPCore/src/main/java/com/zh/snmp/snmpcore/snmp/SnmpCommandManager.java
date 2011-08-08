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

import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.domain.OidCommand;
import com.zh.snmp.snmpcore.domain.SnmpCommand;
import com.zh.snmp.snmpcore.entities.DeviceState;
import com.zh.snmp.snmpcore.message.MessageAppender;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.VariableBinding;

/**
 *
 * @author Golyo
 */
public class SnmpCommandManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnmpCommandManager.class);
    
    private Snmp snmp;
    private MessageAppender appender;
    private Device device;
    
    public SnmpCommandManager(Snmp snmp, MessageAppender appender, Device device) {
        this.device = device;
        this.snmp = snmp;
        this.appender = appender;
    }
    
    public boolean processSetCommand(CommunityTarget target, List<OidCommand> commands){
        PDU pdu = initPDU(PDU.SET, commands);        
        try {
            ResponseEvent response = snmp.set(pdu, target);                            
            return checkResponse(response, false) != null;
        } catch (IOException e) {
            device.setConfigState(DeviceState.ERROR);
            LOGGER.error("Error while snmp get on device " + device.getDeviceId(), e);
            return false;
        }
    }

    
    public ResponseEvent processGetCommand(CommunityTarget target, List<OidCommand> commands) {
        PDU pdu = initPDU(PDU.GET, commands);        
        try {
            ResponseEvent response = snmp.get(pdu, target);                            
            return checkResponse(response, true);
        } catch (IOException e) {
            LOGGER.error("Error while snmp get on device " + device.getDeviceId(), e);
            return null;
        }        
    }
    
    public boolean clearModificationSameCommands(List<OidCommand> commands, ResponseEvent response) {
        List<VariableBinding> variables = response.getResponse().getVariableBindings();
        Iterator<VariableBinding> responseIt = variables.iterator();
        Iterator<OidCommand> commandIt = commands.iterator();
        VariableBinding vb;
        while (commandIt.hasNext()) {
            OidCommand cmd = commandIt.next();
            if (responseIt.hasNext()) { 
                vb = responseIt.next();
                if (!cmd.equalsVariable(vb)) {
                    LOGGER.debug("Command variable not match on device " + device.getDeviceId() + " " + cmd + ";" + vb.toValueString() + ";");
                    appender.addMessage("error.snmp.variableEquals", cmd.getName(), cmd.getValue());
                } else {
                    commandIt.remove();
                }
            } else {
                LOGGER.debug("Command variable not match device " + device.getDeviceId() + " " + cmd);
                appender.addMessage("error.snmp.variableEquals", cmd.getName(), cmd.getValue());
            }
        }
        return !commands.isEmpty();
    }
    
    public boolean checkIfSameResult(List<OidCommand> commands, ResponseEvent response) {
        List<VariableBinding> variables = response.getResponse().getVariableBindings();
        Iterator<VariableBinding> responseIt = variables.iterator();
        Iterator<OidCommand> commandIt = commands.iterator();
        while (commandIt.hasNext()) {
            OidCommand cmd = commandIt.next();
            if (responseIt.hasNext()) { 
                if (!cmd.equalsVariable(responseIt.next())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
        
    }
    
    protected ResponseEvent checkResponse(ResponseEvent response, boolean get) {
        if (response != null) {
            PDU responsePDU = response.getResponse();
            if (responsePDU != null) {                
                int errorStatus = responsePDU.getErrorStatus();
                if (errorStatus != PDU.noError) {
                    LOGGER.error("Device response failed on device: " + device.getDeviceId() + ", ip: " + device.getIpAddress() + "; Error Statusz: " + responsePDU.getErrorStatus() + ": " + responsePDU.getErrorStatusText());
                    device.setConfigState(DeviceState.ERROR);
                    appender.addMessage("error.snmp.responseStatus", responsePDU.getErrorStatus(), responsePDU.getErrorStatusText());
                    return null;
                } else {
                    String prefix = get ? "GET " : "SET ";
                    LOGGER.debug(prefix + "Command succesfull finished on device " + device.getDeviceId() + ": " + responsePDU.getVariableBindings());
                    return response;
                }      
            } else {
                LOGGER.error("Device not found with id: " + device.getDeviceId() + ", ip: " + device.getIpAddress());
                device.setConfigState(DeviceState.NOT_FOUND);
                appender.addMessage("error.snmp.pduNull");
                return null;
            }            
        } else {
            LOGGER.error("Device request time out on device: " + device.getDeviceId() + ", ip: " + device.getIpAddress());
            device.setConfigState(DeviceState.ERROR);
            appender.addMessage("error.snmp.timeout");
            return null;
        }
    }
    
    protected PDU initPDU(int snmpType, List<OidCommand> commands) {
        PDU pdu = new PDU();
        pdu.setType(snmpType);
        pdu.setRequestID(new Integer32(1));
        // Setting the Oid and Value for sysContact variable
        for (OidCommand oidCmd: commands) {
            VariableBinding binding = oidCmd.createVariable();            
            pdu.add(binding);
            LOGGER.debug("VariableX: " + oidCmd.getName() + " value: '" + binding.toValueString() + "'");
        }
        return pdu;        
    }    
}
