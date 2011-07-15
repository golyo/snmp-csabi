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
package com.zh.snmp.snmpcore.services.impl;

import com.zh.snmp.snmpcore.domain.CommandType;
import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.domain.OidCommand;
import com.zh.snmp.snmpcore.domain.SnmpCommand;
import com.zh.snmp.snmpcore.entities.DeviceState;
import com.zh.snmp.snmpcore.message.BackgroundProcess;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.message.SimpleMessageAppender;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import com.zh.snmp.snmpcore.snmp.SnmpCommandManager;
import com.zh.snmp.snmpcore.snmp.SnmpFactory;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Appender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class SnmpServiceImpl implements SnmpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnmpServiceImpl.class);
    //private static final long MAX_DATE_DIFF = 1000 * 60 * 10; //10 perc
    private static final long MAX_DATE_DIFF = 1000 * 10; //10 mp

    private Snmp snmp;

    private final Map<String, Date> runningDeviceConfMap;

    @Autowired
    private DeviceService service;
    
    public SnmpServiceImpl() throws IOException {
        snmp = SnmpFactory.createSnmp();
        runningDeviceConfMap = new HashMap<String, Date>();
    }
    
    @Override
    public SnmpBackgroundProcess startSnmpBackgroundProcess(final Device device, MessageAppender appender) {
        SnmpBackgroundProcess ret = new SnmpBackgroundProcess(this, device, appender);
        ret.start();
        return ret;
    }
    
    @Override
    public SnmpBackgroundProcess startSnmpBackgroundProcess(String ipAddress, MessageAppender appender) {
        Device device = service.findDeviceByIp(ipAddress);
        if (device != null) {
            return startSnmpBackgroundProcess(device, appender);            
        } else {
            appender.addMessage("message.snmp.ipNotConfigured", ipAddress);
            appender.finish();
            return null;
        }
    }
    
    @Override
    public boolean applyConfigOnDevice(Device device, MessageAppender appender) {
        Device toConfig = startProcess(device, appender);
        if (toConfig == null) {
            return false;
        }
        SnmpCommandManager cmdManager = new SnmpCommandManager(snmp, appender, toConfig);
        List<SnmpCommand> commands = toConfig.getConfig().getRoot().cloneCommandsByMap(toConfig.getConfigMap());
        CommunityTarget getTarget = SnmpFactory.createTarget(toConfig.getIpAddress(), "public");
        CommunityTarget setTarget = SnmpFactory.createTarget(toConfig.getIpAddress(), "private");
        for (SnmpCommand cmd : commands) {
            if (clearCommands(cmdManager, toConfig, getTarget, cmd, appender)) {
                if (toConfig.getConfigState().canContinue()) {
                    doSetCommand(cmdManager, toConfig, setTarget, cmd.getBefore());
                }
                if (toConfig.getConfigState().canContinue()) {
                    doSetCommand(cmdManager, toConfig, setTarget, cmd.getCommands());
                }
                doSetCommand(cmdManager, toConfig, setTarget, cmd.getAfter());
                if (toConfig.getConfigState().canContinue()) {
                    appender.addMessage("message.snmp.succesCmd", cmd);                    
                } else {
                    appender.addMessage("message.snmp.failedCmd", cmd);                    
                }
            }
            if (!toConfig.getConfigState().canContinue()) {
                break;
            }
        }
        finishProcess(toConfig, appender);
        return true;
    }    
    
    private boolean doSetCommand(SnmpCommandManager cmdManager, Device device, CommunityTarget setTarget, List<OidCommand> cmds) {
        if (cmds != null && !cmds.isEmpty()) {
            return cmdManager.processSetCommand(setTarget, cmds);            
        } else {
            return true;
        }
    }
    
    private boolean clearCommands(SnmpCommandManager cmdManager, Device device, CommunityTarget getTarget, SnmpCommand command, MessageAppender appender) {
        if (command.getCommands() == null || command.getCommands().isEmpty()) {
            LOGGER.info("Nincs parancssor definiálva: " + command.getName());
            return false;
        }
        ResponseEvent event = cmdManager.processGetCommand(getTarget, command.getCommands());        
        if (event != null) {
            if (cmdManager.clearModificationSameCommands(command.getCommands(), event)) {
                LOGGER.info("Modification found on command");
                appender.addMessage("message.snmp.changesFound", command);
                return true;
            } else {
                LOGGER.info("No modification found on command");
                appender.addMessage("message.snmp.noChangesFound", command);
                return false;
            }
        } else {
            return false;
        }
    }
    
    private Device startProcess(Device device, MessageAppender appender) {
        if (device.getConfigState() == DeviceState.RUNNING) {
            appender.addMessage("message.snmp.running", device);
            return null;
        }
        synchronized (runningDeviceConfMap) {
            Date date = runningDeviceConfMap.get(device.getDeviceId());
            Date now = new Date();
            if (date == null || (now.getTime()-date.getTime()) > MAX_DATE_DIFF) {
                runningDeviceConfMap.put(device.getDeviceId(), now);
                appender.addMessage("message.snmp.start", device);
            } else {
                appender.addMessage("message.snmp.wait", device);
                return null;
            }            
        }    
        device.setConfigState(DeviceState.RUNNING);
        return service.save(device);
    }
    
    private void finishProcess(Device device, MessageAppender appender) {
        if (device.getConfigState().canContinue()) {
            device.setConfigState(DeviceState.CONFIGURED);
        }
        appender.addMessage("message.snmp.stop", device);
        appender.finish();
        service.save(device);
    }
}
