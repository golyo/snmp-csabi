package com.zh.snmp.snmpcore.services;

import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.services.impl.SnmpBackgroundProcess;

/**
 *
 * @author Golyo
 */
public interface SnmpService {    
    public static final String TRAP_USERNAME = "TRAP";
    public static final String WEBSERVICE_USERNAME = "WEBSERVICE";    
    public static final String AUTO_UPDATE_USERNAME = "AUTOUPDATE";    
    
    public SnmpBackgroundProcess startSnmpBackgroundProcess(String userName, String deviceId, MessageAppender appender);
    //public ChangeLogEntity applyConfigOnDevice(Device device, MessageAppender appender);
    public boolean checkDevice(Device device, MessageAppender appender);
}
