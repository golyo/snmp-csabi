package com.zh.snmp.snmpcore.services.impl;

import com.zh.snmp.snmpcore.domain.ConfigNode;
import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.domain.DeviceNode;
import com.zh.snmp.snmpcore.domain.DinamicValue;
import com.zh.snmp.snmpcore.domain.OidCommand;
import com.zh.snmp.snmpcore.domain.SnmpCommand;
import com.zh.snmp.snmpcore.entities.ChangeLogEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.DeviceState;
import com.zh.snmp.snmpcore.exception.ExceptionCodesEnum;
import com.zh.snmp.snmpcore.exception.SystemException;
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
    private DeviceService deviceService;
    
    @Autowired
    private SaveAndRestartCommand saveAndRestartCommand;
    
    public SnmpServiceImpl() throws IOException {
        snmp = SnmpFactory.createSnmp();
        runningDeviceConfMap = new HashMap<String, Date>();
    }
    
    @Override
    public SnmpBackgroundProcess startSnmpBackgroundProcess(String userName, String deviceId, MessageAppender appender) {
        DeviceEntity device = deviceService.findDeviceEntityById(deviceId);
        if (device == null) {
            return null;
        }
        ChangeLogEntity log = startProcess(userName, device, appender);
        SnmpBackgroundProcess ret = new SnmpBackgroundProcess(this, log, appender);
        ret.startProcess();
        return ret;
    }
    
    @Override
    public boolean checkDevice(Device device, MessageAppender appender) {
        ConfigNode config = device.getConfig().getRoot();
        try {
            SnmpCommandManager cmdManager = new SnmpCommandManager(snmp, appender, device.getIpAddress());
            List<SnmpCommand> commands = initDeviceCommands(config, device.getConfigMap());
            for (SnmpCommand cmd : commands) {
                clearSameCommands(cmdManager, cmd);
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
    
    void applyConfigOnDevice(ChangeLogEntity log, MessageAppender appender) {

        Device device = deviceService.findDeviceByDeviceId(log.getDevice().getId());
        long start = System.currentTimeMillis();
        ConfigNode config = device.getConfig().getRoot();
        try {
            SnmpCommandManager cmdManager = new SnmpCommandManager(snmp, appender, device.getIpAddress());

            //ConfigNode upgradeConfig = upgradeConfigManager.getUpgradeConfig();
            //DeviceNode upradeNode = upgradeConfigManager.getUpgradeNode(config.getCode());
            //TODO init device commands;        
            if (upgradeDevice(cmdManager, config.getCode())) {
                device.setConfigState(DeviceState.UPDATED);
            } else {
                if (cmdManager.canContinue()) {
                    List<SnmpCommand> commands = initDeviceCommands(config, device.getConfigMap());
                    boolean hasAnyChangesOnDevice = processCommands(cmdManager, commands);
                    if ((config.isRestartDevice() == Boolean.TRUE) && cmdManager.canContinue() && hasAnyChangesOnDevice) {
                        saveAndRestartDevice(cmdManager, appender);
                    }                    
                }
                device.setConfigState(cmdManager.getDeviceState());                
            }                     
        } catch (Exception e) {
            LOGGER.error("Ismeretlen hiba történt", e);
            device.setConfigState(DeviceState.ERROR);
            appender.addMessage("message.snmp.unknownError", device.getIpAddress());
        }
        finishProcess(log, device.getConfigState(), appender, start);
    }    
    
    private boolean upgradeDevice(SnmpCommandManager cmdManager, String configCode) {
        ConfigNode upgradeConfig = upgradeConfigManager.getUpgradeConfig();
        DeviceNode upgradeNode = upgradeConfigManager.getUpgradeNode(configCode);
        try {
            if (upgradeNode != null) {
                List<SnmpCommand> commands = initDeviceCommands(upgradeConfig, upgradeNode);
                boolean hasAnyChangesOnDevice = processCommands(cmdManager, commands);
                if (hasAnyChangesOnDevice) {
                    if (cmdManager.canContinue()) {
                        cmdManager.addMessage("message.snmp.upgraded", cmdManager.getIpAddress());                
                        return true;
                    }
                } else {
                    cmdManager.addMessage("message.snmp.notNeedUpgrade", cmdManager.getIpAddress());   
                }
            }
            return false;                        
        } catch (SystemException e) {
            if (e.getCode() == ExceptionCodesEnum.WrongDeviceType) {
                cmdManager.addError("message.snmp.wrongDeviceType", cmdManager.getIpAddress(), upgradeNode.findDinamic("productName").getValue());
                return false;
            } else {
                throw e;
            }
        }
    }
    
    private List<SnmpCommand> initDeviceCommands(ConfigNode config, DeviceNode device) {
        //List<SnmpCommand> ret = initSnmpCommands(config);
        List<SnmpCommand> ret = new LinkedList<SnmpCommand>();
        for (SnmpCommand cmd: config.getCommands()) {
            SnmpCommand source = cmd.cloneEmpty();
            updateCommandValues(source, cmd, device);
            changeChildCommandValues(source, device, config);
            ret.add(source);
        }
        return ret;
    }
    /*
    private List<SnmpCommand> initSnmpCommands(ConfigNode config) {
        List<SnmpCommand> ret = new LinkedList<SnmpCommand>();
        for (SnmpCommand cmd: config.getCommands()) {
            ret.add(cmd.clone());
        }
        return ret;
    }
    */
    private void changeChildCommandValues(SnmpCommand command, DeviceNode deviceNode, ConfigNode configNode) {
        for (DeviceNode dChild: deviceNode.getChildren()) {
            if (dChild.isSelected()) {
                ConfigNode cChild = configNode.findChildByCode(dChild.getCode());
                for (SnmpCommand cmd: cChild.getCommands()) {
                    updateCommandValues(command, cmd, dChild);
                }
                changeChildCommandValues(command, dChild, cChild);
            }
        }
    }
    
    private void updateCommandValues(SnmpCommand source, SnmpCommand mergeCmd, DeviceNode deviceNode) {
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
    
    private boolean processCommands(SnmpCommandManager cmdManager, List<SnmpCommand> commands) {
        boolean hasAnyChangesOnDevice = false;
        for (SnmpCommand cmd : commands) {
            if (cmd.isPreCondition()) {
                clearSameCommands(cmdManager, cmd);
                if (cmd.getCommands().isEmpty()) {
                    //Nem kell tovább módosítani, vége a folyamatnak, mert precondition stimmel,
                    //Utána lévő parancsokat csak különbség esetén kell futtatni
                    return hasAnyChangesOnDevice;
                } else {
                    hasAnyChangesOnDevice = true;
                }
            } else {
                processCommand(cmdManager, cmd);
                hasAnyChangesOnDevice = hasAnyChangesOnDevice || !cmd.getCommands().isEmpty();                    
            }
            if (!cmdManager.canContinue()) {
                break;
            }
        }        
        return hasAnyChangesOnDevice;
    }
    
    private void processCommand(SnmpCommandManager cmdManager, SnmpCommand cmd) {
        clearSameCommands(cmdManager, cmd);
        if (cmdManager.canContinue() && !cmd.getCommands().isEmpty()) {
            cmdManager.processSetCommand(cmd.getBefore());
            if (cmdManager.canContinue()) {
                cmdManager.processSetCommand(cmd.getCommands());
            }
            cmdManager.processSetCommand(cmd.getAfter());
            if (cmdManager.canContinue()) {
                cmdManager.addMessage("message.snmp.succesCmd", cmd.getName(), cmdManager.getIpAddress());                    
            } else {
                cmdManager.addMessage("message.snmp.failedCmd", cmd.getName(), cmdManager.getIpAddress());                    
            }            
        }
    }

    private void clearSameCommands(SnmpCommandManager cmdManager, 
            SnmpCommand command) {
        if (command.getCommands().isEmpty()) {
            LOGGER.warn("Nincs parancssor definiálva: " + command.getName());
        }
        ResponseEvent event = cmdManager.processGetCommand(command.getCommands());        
        if (event != null) {
            if (cmdManager.clearModificationSameCommands(command.getCommands(), event)) {
                LOGGER.info("Modification found on command");
                cmdManager.addMessage("message.snmp.changesFound", command.getName());
            } else {
                LOGGER.info("No modification found on command");
                cmdManager.addMessage("message.snmp.noChangesFound", command.getName());
            }
        }
    }
    
    private ChangeLogEntity startProcess(String userName, DeviceEntity device, MessageAppender appender) {
        /*
        if (device.getConfigState() == DeviceState.RUNNING) {
            appender.addMessage("message.snmp.running", device.getIpAddress());
            return null;
        }
         * 
         */
        synchronized (runningDeviceConfMap) {
            Date date = runningDeviceConfMap.get(device.getId());
            Date now = new Date();
            if (date == null || (now.getTime()-date.getTime()) > MAX_DATE_DIFF) {
                runningDeviceConfMap.put(device.getId(), now);
                appender.addMessage("message.snmp.start", device.getIpAddress());
            } else {
                appender.addMessage("message.snmp.wait", device.getIpAddress());
                return null;
            }            
        }    
        return deviceService.changeDeviceState(userName, device, DeviceState.RUNNING, null);
    }
    
    private ChangeLogEntity finishProcess(ChangeLogEntity logEntity, DeviceState newState, MessageAppender appender, long start) {
        DeviceState finishedState = newState.canContinue() ? DeviceState.CONFIGURED : newState;
        DeviceEntity device = logEntity.getDevice();
        long took = System.currentTimeMillis() - start;
        appender.addMessage("message.snmp.stop", device.getIpAddress(), finishedState, took);
        appender.finish();
        return deviceService.changeDeviceState(logEntity.getUserName(), device, finishedState, logEntity);
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
