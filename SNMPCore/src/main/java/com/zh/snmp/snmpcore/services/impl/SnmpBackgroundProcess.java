package com.zh.snmp.snmpcore.services.impl;

import com.zh.snmp.snmpcore.entities.ChangeLogEntity;
import com.zh.snmp.snmpcore.message.BackgroundProcess;
import com.zh.snmp.snmpcore.message.MessageAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Golyo
 */
public class SnmpBackgroundProcess extends BackgroundProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnmpBackgroundProcess.class);
    
    private SnmpServiceImpl service;
    private ChangeLogEntity log;    
    
    SnmpBackgroundProcess(SnmpServiceImpl service, ChangeLogEntity log, MessageAppender appender) {
        super(appender);
        this.service = service;
        this.log = log;
    }
    
    @Override
    protected void doWork() {
        service.applyConfigOnDevice(log, getAppender());
    }

    @Override
    protected void handleException(Exception exception) {
        LOGGER.error("Unknown exception", exception);
        getAppender().addMessage("background.exception", exception.toString());
    }

    public ChangeLogEntity getLog() {
        return log;
    }
    
}
