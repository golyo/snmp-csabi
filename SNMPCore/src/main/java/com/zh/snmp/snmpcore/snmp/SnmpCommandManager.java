package com.zh.snmp.snmpcore.snmp;

import com.zh.snmp.snmpcore.domain.OidCommand;
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
    private String ipAddress;
    private DeviceState deviceState;
    private CommunityTarget getTarget;
    private CommunityTarget setTarget;

    //private Device device;
    
    public SnmpCommandManager(Snmp snmp, MessageAppender appender, String ipAddress) {
        this.snmp = snmp;
        this.appender = appender;
        this.ipAddress = ipAddress;
        getTarget = SnmpFactory.createTarget(ipAddress, "public");
        setTarget = SnmpFactory.createTarget(ipAddress, "private");
        deviceState = DeviceState.RUNNING;
    }
    
    public void processSetCommand(List<OidCommand> commands){
        if (commands != null && !commands.isEmpty()) {
            PDU pdu = initPDU(PDU.SET, commands);        
            try {
                if (deviceState.canContinue()) {
                    ResponseEvent response = snmp.set(pdu, setTarget);                            
                    checkResponse(response, false);                    
                }
            } catch (IOException e) {
                deviceState = DeviceState.ERROR;
                LOGGER.error("Error while snmp get on device ip " + ipAddress, e);            
            }            
        }
    }

    
    public ResponseEvent processGetCommand(List<OidCommand> commands) {
        PDU pdu = initPDU(PDU.GET, commands);        
        try {
            if (deviceState.canContinue()) {
                ResponseEvent response = snmp.get(pdu, getTarget);                            
                return checkResponse(response, true);                
            } else {
                return null;
            }
        } catch (IOException e) {
            deviceState = DeviceState.ERROR;
            LOGGER.error("Error while snmp get on device ip " + ipAddress, e);
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
                    LOGGER.debug("Command variable not match on device ip " + ipAddress + " " + cmd + ";" + vb.toValueString() + ";");
                    appender.addMessage("error.snmp.variableEquals", cmd.getName(), cmd.getValue());
                } else {
                    commandIt.remove();
                }
            } else {
                LOGGER.debug("Command variable not match device ip " + ipAddress + " " + cmd);
                appender.addMessage("error.snmp.variableEquals", cmd.getName(), cmd.getValue());
            }
        }
        return !commands.isEmpty();
    }
    
    public void addError(String key, Object... params) {
        deviceState = DeviceState.ERROR;
        appender.addMessage(key, params);
    }
    
    public void addMessage(String key, Object... params) {
        appender.addMessage(key, params);        
    }
    
    public boolean canContinue() {
        return deviceState.canContinue();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public DeviceState getDeviceState() {
        return deviceState;
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
                    LOGGER.error("Device response failed on device ip: " + ipAddress + "; Error Statusz: " + responsePDU.getErrorStatus() + ": " + responsePDU.getErrorStatusText());
                    deviceState = DeviceState.ERROR;
                    appender.addMessage("error.snmp.responseStatus", responsePDU.getErrorStatus(), responsePDU.getErrorStatusText());
                    return null;
                } else {
                    String prefix = get ? "GET " : "SET ";
                    LOGGER.debug(prefix + "Command succesfull finished on device ip " + ipAddress + ": " + responsePDU.getVariableBindings());
                    return response;
                }      
            } else {
                LOGGER.error("Device not found with ip: " + ipAddress);
                deviceState = DeviceState.ERROR;
                deviceState = DeviceState.NOT_FOUND;
                appender.addMessage("error.snmp.pduNull");
                return null;
            }            
        } else {
            LOGGER.error("Device request time out on device ip: " + ipAddress);
            deviceState = DeviceState.ERROR;
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
            if (oidCmd.getValue() != null) {
                VariableBinding binding = oidCmd.createVariable();            
                pdu.add(binding);                
            } else {
                appender.addMessage("message.snmp.missingDinamicValue", oidCmd.getName(), oidCmd.getDinamicName());
                deviceState = DeviceState.ERROR;
            }
        }
        return pdu;        
    }    
}
