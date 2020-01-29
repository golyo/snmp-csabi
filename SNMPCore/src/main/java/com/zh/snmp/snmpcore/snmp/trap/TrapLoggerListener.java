package com.zh.snmp.snmpcore.snmp.trap;

import com.zh.snmp.snmpcore.message.MessageAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Golyo
 */
public class TrapLoggerListener implements TrapListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrapListener.class);
    

    @Override
    public void processTrapResponse(TrapManager.DeviceTrapInfo trapInfo, MessageAppender appender) {
        LOGGER.debug("Trap received from" + trapInfo);
    }
    
}
