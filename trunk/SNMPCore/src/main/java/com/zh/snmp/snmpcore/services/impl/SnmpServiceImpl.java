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
import com.zh.snmp.snmpcore.domain.DeviceNode;
import com.zh.snmp.snmpcore.domain.DinamicValue;
import com.zh.snmp.snmpcore.domain.OidCommand;
import com.zh.snmp.snmpcore.domain.SnmpCommand;
import com.zh.snmp.snmpcore.entities.DeviceState;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import com.zh.snmp.snmpcore.snmp.SaveAndRestartCommand;
import com.zh.snmp.snmpcore.snmp.SnmpCommandManager;
import com.zh.snmp.snmpcore.snmp.SnmpFactory;
import com.zh.snmp.snmpcore.snmp.UpgradeConfig;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private UpgradeConfig upgradeConfigManager;
    
    @Autowired
    private DeviceService service;

    @Autowired
    private SaveAndRestartCommand saveAndRestartCommand;
    
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
    public boolean checkDevice(Device device, MessageAppender appender) {
        ConfigNode config = device.getConfig().getRoot();
        try {
            SnmpCommandManager cmdManager = new SnmpCommandManager(snmp, appender, device.getIpAddress());
            List<SnmpCommand> commands = initDeviceCommands(config, device.getConfigMap(), appender);
            for (SnmpCommand cmd : commands) {
                clearSameCommands(cmdManager, cmd, appender);
                if (!cmdManager.canContinue() || !cmd.getCommands().isEmpty()) {
                    return false;
                }
            }            
        } catch (Exception e) {
            LOGGER.error("Ismeretlen hiba történt", e);
            appender.addMessage("message.snmp.unknownError", device.getIpAddress());
            return false;
        }
        return true;
    }
    
    @Override
    public boolean applyConfigOnDevice(Device device, MessageAppender appender) {
        Device toConfig = startProcess(device, appender);
        if (toConfig == null) {
            return false;
        }
        long start = System.currentTimeMillis();
        ConfigNode config = device.getConfig().getRoot();
        try {
            SnmpCommandManager cmdManager = new SnmpCommandManager(snmp, appender, toConfig.getIpAddress());

            //ConfigNode upgradeConfig = upgradeConfigManager.getUpgradeConfig();
            //DeviceNode upradeNode = upgradeConfigManager.getUpgradeNode(config.getCode());
            //TODO init device commands;        
            
            List<SnmpCommand> commands = initDeviceCommands(config, device.getConfigMap(), appender);
            boolean hasAnyChangesOnDevice = processCommands(cmdManager, commands, appender);
            if ((config.isRestartDevice() == Boolean.TRUE) && cmdManager.canContinue() && hasAnyChangesOnDevice) {
                saveAndRestartDevice(cmdManager, appender);
            }
            toConfig.setConfigState(cmdManager.getDeviceState());
        } catch (Exception e) {
            LOGGER.error("Ismeretlen hiba történt", e);
            toConfig.setConfigState(DeviceState.ERROR);
            appender.addMessage("message.snmp.unknownError", device.getIpAddress());
        }
        finishProcess(toConfig, appender, start);
        return device.getConfigState() == DeviceState.CONFIGURED;
    }    

    private List<SnmpCommand> initDeviceCommands(ConfigNode config, DeviceNode device, MessageAppender appender) {
        List<SnmpCommand> ret = initSnmpCommands(config);
        for (SnmpCommand cmd: ret) {
            changeCommandValues(cmd, device, config, appender);
        }
        return ret;
    }
    
    private List<SnmpCommand> initSnmpCommands(ConfigNode config) {
        List<SnmpCommand> ret = new LinkedList<SnmpCommand>();
        for (SnmpCommand cmd: config.getCommands()) {
            ret.add(cmd.clone());
        }
        return ret;
    }
    
    private void changeCommandValues(SnmpCommand command, DeviceNode deviceNode, ConfigNode configNode, MessageAppender appender) {
        for (DeviceNode dChild: deviceNode.getChildren()) {
            if (dChild.isSelected()) {
                ConfigNode cChild = configNode.findChildByCode(dChild.getCode());
                for (SnmpCommand cmd: cChild.getCommands()) {
                    updateCommandValues(command, cmd, dChild, appender);
                }
                changeCommandValues(command, dChild, cChild, appender);
            }
        }
    }
    
    private void updateCommandValues(SnmpCommand source, SnmpCommand mergeCmd, DeviceNode deviceNode, MessageAppender appender) {
        if (mergeCmd.getPriority() == source.getPriority()) {
            for (OidCommand mergeOid: mergeCmd.getCommands()) {
                OidCommand act = source.setNewOidValue(mergeOid);
                if (act == null) {
                    act = mergeOid.clone();
                    source.getCommands().add(act);                    
                }
                if (act.isDinamic()) {
                    DinamicValue actDinamic = deviceNode.findDinamic(act.getDinamicName());
                    if (actDinamic != null) {
                        act.setValue(actDinamic.getValue());                                                
                    }
                }
            }            
        }
    }
    
    private boolean processCommands(SnmpCommandManager cmdManager, List<SnmpCommand> commands, MessageAppender appender) {
        boolean hasAnyChangesOnDevice = false;
        for (SnmpCommand cmd : commands) {
            if (cmd.isPreCondition()) {
                clearSameCommands(cmdManager, cmd, appender);
                if (cmd.getCommands().isEmpty()) {
                    //Nem kell tovább módosítani, vége a folyamatnak, mert precondition
                    return hasAnyChangesOnDevice;
                }
            } else {
                processCommand(cmdManager, cmd, appender);
                hasAnyChangesOnDevice = hasAnyChangesOnDevice || !cmd.getCommands().isEmpty();
            }
            if (!cmdManager.canContinue()) {
                break;
            }
        }        
        return hasAnyChangesOnDevice;
    }
    
    private void processCommand(SnmpCommandManager cmdManager, SnmpCommand cmd, MessageAppender appender) {
        clearSameCommands(cmdManager, cmd, appender);
        if (cmdManager.canContinue() && !cmd.getCommands().isEmpty()) {
            cmdManager.processSetCommand(cmd.getBefore());
            if (cmdManager.canContinue()) {
                cmdManager.processSetCommand(cmd.getCommands());
            }
            cmdManager.processSetCommand(cmd.getAfter());
            if (cmdManager.canContinue()) {
                appender.addMessage("message.snmp.succesCmd", cmd.getName(), cmdManager.getIpAddress());                    
            } else {
                appender.addMessage("message.snmp.failedCmd", cmd.getName(), cmdManager.getIpAddress());                    
            }            
        }
    }

    private void clearSameCommands(SnmpCommandManager cmdManager, 
            SnmpCommand command, MessageAppender appender) {
        if (command.getCommands().isEmpty()) {
            LOGGER.warn("Nincs parancssor definiálva: " + command.getName());
        }
        ResponseEvent event = cmdManager.processGetCommand(command.getCommands());        
        if (event != null) {
            if (cmdManager.clearModificationSameCommands(command.getCommands(), event)) {
                LOGGER.info("Modification found on command");
                appender.addMessage("message.snmp.changesFound", command.getName());
            } else {
                LOGGER.info("No modification found on command");
                appender.addMessage("message.snmp.noChangesFound", command.getName());
            }
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
                appender.addMessage("message.snmp.start", device.getIpAddress());
            } else {
                appender.addMessage("message.snmp.wait", device.getIpAddress());
                return null;
            }            
        }    
        device.setConfigState(DeviceState.RUNNING);
        return service.save(device);
    }
    
    private void finishProcess(Device device, MessageAppender appender, long start) {
        if (device.getConfigState().canContinue()) {
            device.setConfigState(DeviceState.CONFIGURED);
        }
        long took = System.currentTimeMillis() - start;
        appender.addMessage("message.snmp.stop", device.getIpAddress(), device.getConfigState(), took);
        appender.finish();
        service.save(device);
    }    
    
    private boolean saveAndRestartDevice(SnmpCommandManager manager, MessageAppender appender) {
        manager.processSetCommand(saveAndRestartCommand.getSaveCommand().getCommands());
        if (manager.canContinue()) {
            ResponseEvent checkSaved = manager.processGetCommand(saveAndRestartCommand.getCheckSaveCommand().getCommands());
            if (manager.checkIfSameResult(saveAndRestartCommand.getCheckSaveCommand().getCommands(), checkSaved)) {
                manager.processSetCommand(saveAndRestartCommand.getRestartCommand().getCommands());
                if (manager.canContinue()) {
                    appender.addMessage("message.snmp.restartSucces", manager.getIpAddress());
                    return true;
                }
            }
        }
        appender.addMessage("message.snmp.restartFailed", manager.getIpAddress());
        return false;
    }
}
