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
