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

import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.DeviceType;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.services.SnmpService;
import com.zh.snmp.snmpcore.snmp.mib.MibParser;
import com.zh.snmp.snmpcore.snmp.trap.TrapListener;
import com.zh.snmp.snmpcore.snmp.trap.TrapManager.DeviceTrapInfo;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class SnmpManager implements TrapListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnmpManager.class);
    private static final String MSG_DEVICE_NOTFOUND = "snmp.deviceByIpNotFound";
    private static final String MSG_CONFIG_NOTFOUND = "snmp.configNotFound";
    private static final String MSG_SNMPERROR = "snmp.error";
    private static final String MSG_GETRESULT = "snmp.getResult";
    private static final String MSG_COMMAND_FAILED = "snmp.commandFailed";
    private static final String MSG_COMMAND_SKIPPED = "snmp.commandSkipped";
    private static final String MSG_COMMAND_SUCCES = "snmp.commandSucces";
    
    private Snmp snmp;
    
    @Autowired   
    private SnmpService service;
    @Autowired 
    private MibParser parser;

    public SnmpManager() throws IOException {
        snmp = SnmpFactory.createSnmp();
    }
    
    @Override
    public void processTrapResponse(DeviceTrapInfo trapInfo, MessageAppender appender) {
        DeviceEntity entity = parseDevice(trapInfo, appender);
        if (entity == null) {
            appender.addMessage(MSG_DEVICE_NOTFOUND, trapInfo);
        } else {
            if (entity.getConfigurations() != null && !entity.getConfigurations().isEmpty()) {
                for (DeviceConfigEntity config: entity.getConfigurations()) {
                    checkAndSet(entity.getIpAddress(), config, appender);                    
                }                
            } else {
                appender.addMessage(MSG_CONFIG_NOTFOUND, entity);
            }
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<SnmpCommandResult> processOnDevice(ProcessType processType, DeviceEntity device, DeviceType deviceType, MessageAppender appender) {
        DeviceConfigEntity config = device.findConfiguration(deviceType);
        if (config == null) {
            appender.addMessage(MSG_CONFIG_NOTFOUND, device);
            return null;
        } else {
            return processOnIp(processType, device.getIpAddress(), config, appender);            
        }
    }
    
    protected void checkAndSet(String ip, DeviceConfigEntity config, MessageAppender appender) {        
        List<SnmpCommandResult> results = processOnIp(ProcessType.GET, ip, config, appender);
        if (checkResults(results, appender)) {
            results = processOnIp(ProcessType.SET, ip, config, appender);
            checkResults(results, appender);
        }
    }
    
    private boolean checkResults(List<SnmpCommandResult> results, MessageAppender appender) {
        boolean hasFailed = false;
        boolean allSkip = true;
        for (SnmpCommandResult r: results) {
            switch (r.getType()) {
                case FAILED: {
                    hasFailed = true;
                    appender.addMessage(MSG_COMMAND_FAILED, r);
                    break;
                }
                case SKIPED: {
                    appender.addMessage(MSG_COMMAND_SKIPPED, r);
                    break;
                }
                case SUCCES: {
                    allSkip = false;                    
                    appender.addMessage(MSG_COMMAND_SUCCES, r);                    
                    break;
                }
            }
        }        
        return !hasFailed && !allSkip;
    }
    
    protected List<SnmpCommandResult> processOnIp(ProcessType processType, String ip, DeviceConfigEntity config, MessageAppender appender) {
        try {
            SnmpCommandManager cmdManager = new SnmpCommandManager(snmp, appender);            
            List<SnmpCommand> commands = parser.parseCommands(new StringReader(config.getSnmpDescriptor()));
            List<SnmpCommandResult> results = new LinkedList<SnmpCommandResult>();
            CommunityTarget target = SnmpFactory.createTarget(ip, processType);
            for (SnmpCommand command: commands) {
                SnmpCommandResult result = null;
                switch (processType) {
                    case GET: {
                        result = cmdManager.checkCommand(target, command);
                        break;
                    }
                    case SET: {
                        result = cmdManager.processCommand(target, command);
                        break;
                    }
                    default: {
                        throw new IOException("Process type not found " + processType);
                    }
                }
                appender.addMessage(MSG_GETRESULT, result);
                results.add(result);
            }
            return results;
        } catch (IOException e) {
            appender.addMessage(MSG_SNMPERROR, ip);
            LOGGER.error("Error while parse commands", e);
            return null;
        }        
    }
    
    public DeviceEntity parseDevice(DeviceTrapInfo trapInfo, MessageAppender appender) {
        DeviceEntity filter = new DeviceEntity();
        filter.setIpAddress(trapInfo.getIpAdress());
        return service.findDeviceByFilter(filter);        
    }
    
    public enum ProcessType {
        SET("private"),
        GET("public");
        
        private String community;
        private ProcessType(String community) {
            this.community = community;
        }
        
        public String getCommunity() {
            return community;
        }
    }
}
