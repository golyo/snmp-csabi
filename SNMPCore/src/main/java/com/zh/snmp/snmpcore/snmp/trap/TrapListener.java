package com.zh.snmp.snmpcore.snmp.trap;

import com.zh.snmp.snmpcore.message.MessageAppender;

/**
 *
 * @author Golyo
 */
public interface TrapListener {
    public void processTrapResponse(TrapManager.DeviceTrapInfo trapInfo, MessageAppender appender);
}
