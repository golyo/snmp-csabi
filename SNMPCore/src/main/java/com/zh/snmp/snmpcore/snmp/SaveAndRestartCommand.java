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

import antlr.debug.MessageAdapter;
import com.zh.snmp.snmpcore.domain.OidCommand;
import com.zh.snmp.snmpcore.domain.OidType;
import com.zh.snmp.snmpcore.domain.SnmpCommand;
import com.zh.snmp.snmpcore.exception.ExceptionCodesEnum;
import com.zh.snmp.snmpcore.exception.SystemException;
import com.zh.snmp.snmpcore.message.SimpleMessageAppender;
import com.zh.snmp.snmpcore.snmp.mib.MibParser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class SaveAndRestartCommand implements InitializingBean{
    @Autowired
    private MibParser mibParser;
    
    private SnmpCommand saveCommand;
    private SnmpCommand checkSaveCommand;
    private SnmpCommand restartCommand;

    public SaveAndRestartCommand() {
        saveCommand = cretaeSimpleCommand("PACKETFRONT-DRG-MIB:systemConfigSave.0", "1", "1", OidType.INT);
        checkSaveCommand = cretaeSimpleCommand("PACKETFRONT-DRG-MIB:systemConfigSave.0", "3", "3", OidType.INT);
        restartCommand = cretaeSimpleCommand("PACKETFRONT-DRG-MIB:systemConfigRestartControl.0", "2", "0", OidType.INT);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SimpleMessageAppender appender = new SimpleMessageAppender();
        boolean succes = mibParser.parseAndSetMibValues(saveCommand, appender);
        succes = mibParser.parseAndSetMibValues(checkSaveCommand, appender) && succes;
        succes = mibParser.parseAndSetMibValues(restartCommand, appender) && succes;
        if (!succes) {
           throw new SystemException(ExceptionCodesEnum.ConfigurationException, "Wrong restart command " + appender.toString());              
        }
    }

    public SnmpCommand getCheckSaveCommand() {
        return checkSaveCommand;
    }

    public void setCheckSaveCommand(SnmpCommand checkSaveCommand) {
        this.checkSaveCommand = checkSaveCommand;
    }

    public SnmpCommand getRestartCommand() {
        return restartCommand;
    }

    public void setRestartCommand(SnmpCommand restartCommand) {
        this.restartCommand = restartCommand;
    }

    public SnmpCommand getSaveCommand() {
        return saveCommand;
    }

    public void setSaveCommand(SnmpCommand saveCommand) {
        this.saveCommand = saveCommand;
    }
    
    private SnmpCommand cretaeSimpleCommand(String name, String value, String expectedValue, OidType type) {
        OidCommand oidCmd = new OidCommand();
        oidCmd.setName(name);
        oidCmd.setExpectedValue(expectedValue);
        oidCmd.setValue(value);
        oidCmd.setType(type);
        SnmpCommand cmd = new SnmpCommand();
        cmd.getCommands().add(oidCmd);
        return cmd;
    }
    
    
}
