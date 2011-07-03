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

import com.zh.snmp.snmpcore.message.MessageAppender;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
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
    private Snmp snmp;
    private MessageAppender appender;
    
    public SnmpCommandManager(Snmp snmp, MessageAppender appender) throws IOException {
        this.snmp = snmp;
        this.appender = appender;
    }
    
    public SnmpCommandResult processCommand(CommunityTarget target, SnmpCommand command) throws IOException {
        PDU pdu = initPDU(PDU.SET, command);        
        ResponseEvent response = snmp.set(pdu, target);                
        return processSetResponse(response);
    }

    public SnmpCommandResult checkCommand(CommunityTarget target, SnmpCommand command) throws IOException {
        CommunityTarget tt = DeviceSettings.TEST_DEVICE.createTarget(false);
        PDU pdu = initPDU(PDU.GET, command);        
        ResponseEvent response = snmp.get(pdu, tt);                
        return processGetResponse(command, response);
    }
    
    protected SnmpCommandResult processGetResponse(SnmpCommand command, ResponseEvent response) {
        SnmpCommandResult result = initCommand(response);
        if (result.getType() == SnmpCommandResult.ResultType.SUCCES) {
            List<VariableBinding> variables = response.getResponse().getVariableBindings();
            Iterator<VariableBinding> responseIt = variables.iterator();
            Iterator<VariableBinding> commandIt = command.getBindings().iterator();
            boolean equals = true;
            while (commandIt.hasNext() && equals) {
                equals = responseIt.hasNext() && commandIt.next().equals(responseIt.next());
            }
            if (equals) {
                result.resultSkip();
            }
        }
        return result;
    }
        
    protected SnmpCommandResult processSetResponse(ResponseEvent response) {
        return initCommand(response);
    }
    
    protected SnmpCommandResult initCommand(ResponseEvent response) {
        SnmpCommandResult result = new SnmpCommandResult(new SnmpCommand());
        if (response != null) {
            PDU responsePDU = response.getResponse();
            if (responsePDU != null) {
                result.getCommand().setBindings(responsePDU.getVariableBindings());
                int errorStatus = responsePDU.getErrorStatus();
                if (errorStatus != PDU.noError) {
                    result.resultFailed("Error: Request Failed, Error Status = " + errorStatus + ", Error Status Text = " + responsePDU.getErrorStatusText());
                }                
            } else {
                result.resultFailed("Error: Response PDU is null");
            }            
        } else {
            result.resultFailed("Error: Agent Timeout");
        }
        return result;        
    }
    
    protected PDU initPDU(int snmpType, SnmpCommand command) {
        PDU pdu = new PDU();
        pdu.setType(snmpType);
        pdu.setRequestID(new Integer32(1));
        // Setting the Oid and Value for sysContact variable
        for (VariableBinding vb: command.getBindings()) {
            pdu.add(vb);            
        } 
        return pdu;        
    }    
}
