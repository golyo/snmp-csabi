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

import com.zh.snmp.snmpcore.domain.ConfigNode;
import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.domain.DeviceSelectionNode;
import com.zh.snmp.snmpcore.domain.OidCommand;
import com.zh.snmp.snmpcore.domain.SnmpCommand;
import com.zh.snmp.snmpcore.entities.DeviceState;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import com.zh.snmp.snmpcore.snmp.SnmpCommandManager;
import com.zh.snmp.snmpcore.snmp.SnmpFactory;
import com.zh.snmp.snmpcore.snmp.UpgradeConfig;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private UpgradeConfig upgradeConfig;
    
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
    public boolean applyConfigOnDevice(Device device, MessageAppender appender) {
        Device toConfig = startProcess(device, appender);
        if (toConfig == null) {
            return false;
        }
        try {
            SnmpCommandManager cmdManager = new SnmpCommandManager(snmp, appender, toConfig);
            List<SnmpCommand> commands = initDeviceCommands(device.getConfig().getRoot(), device.getConfigMap(), appender);
            CommunityTarget getTarget = SnmpFactory.createTarget(toConfig.getIpAddress(), "public");
            CommunityTarget setTarget = SnmpFactory.createTarget(toConfig.getIpAddress(), "private");
    //        if (processCommand(getTarget, setTarget, cmdManager, upgradeConfig.getUpgradeCommand(), appender, toConfig)) {
                for (SnmpCommand cmd : commands) {
                    if (!processCommand(getTarget, setTarget, cmdManager, cmd, appender, toConfig)) {
                        break;
                    }
                }            
    //        }
            
        } catch (Exception e) {
            LOGGER.error("Ismeretlen hiba történt", e);
            device.setConfigState(DeviceState.ERROR);
            appender.addMessage("message.snmp.unknownError", device);
        }
        finishProcess(toConfig, appender);
        return true;
    }    

    private List<SnmpCommand> initDeviceCommands(ConfigNode config, DeviceSelectionNode deviceNode, MessageAppender appender) {
        List<SnmpCommand> ret = initSnmpCommands(config);
        Map<String, String> dinamics = deviceNode.getDinamicValues();
        boolean canContinue = true;
        for (SnmpCommand cmd: ret) {
            changeCommandValues(cmd, deviceNode, config);
            for (OidCommand oidCmd: cmd.getCommands()) {
                if (oidCmd.isDinamic()) {
                    String val = dinamics.get(oidCmd.getDinamicName());
                    if (val == null) {
                        appender.addMessage("message.snmp.missingDinamicValue", oidCmd);
                        canContinue = false;
                    }
                    oidCmd.setValue(val);
                }
            }
        }
        return canContinue ? ret : Collections.EMPTY_LIST;
    }
    
    private List<SnmpCommand> initSnmpCommands(ConfigNode config) {
        List<SnmpCommand> ret = new LinkedList<SnmpCommand>();
        for (SnmpCommand cmd: config.getCommands()) {
            ret.add(cmd.clone());
        }
        return ret;
    }
    
    private void changeCommandValues(SnmpCommand command, DeviceSelectionNode deviceNode, ConfigNode configNode) {
        for (DeviceSelectionNode dChild: deviceNode.getChildren()) {
            if (dChild.isSelected()) {
                ConfigNode cChild = configNode.findChildByCode(dChild.getCode());
                for (SnmpCommand cmd: cChild.getCommands()) {
                    command.updateCommandValues(cmd);
                }
                changeCommandValues(command, dChild, cChild);
            }
        }
    }
    
    private boolean processCommand(CommunityTarget getTarget, CommunityTarget setTarget, SnmpCommandManager cmdManager, SnmpCommand cmd, MessageAppender appender, Device toConfig) {
        if (clearCommands(cmdManager, getTarget, cmd, appender)) {
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
        return toConfig.getConfigState().canContinue();
    }
    
    private boolean doSetCommand(SnmpCommandManager cmdManager, Device device, CommunityTarget setTarget, List<OidCommand> cmds) {
        if (cmds != null && !cmds.isEmpty()) {
            return cmdManager.processSetCommand(setTarget, cmds);            
        } else {
            return true;
        }
    }

    private boolean clearCommands(SnmpCommandManager cmdManager, CommunityTarget getTarget, SnmpCommand command, MessageAppender appender) {
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
