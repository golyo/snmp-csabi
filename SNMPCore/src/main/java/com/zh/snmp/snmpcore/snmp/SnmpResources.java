package com.zh.snmp.snmpcore.snmp;

/**
 *
 * @author Golyo
 */
public interface SnmpResources {
    //DeviceTrapInfo
    public final static String MSG_TRAP_RECEIVED = "snmp.trapReceived";
    //DeviceTrapInfo
    public static final String MSG_DEVICE_NOTFOUND = "snmp.deviceByIpNotFound";
    //DeviceEntity
    public static final String MSG_CONFIG_NOTFOUND = "snmp.configNotFound";
    //String
    public static final String MSG_SNMPERROR = "snmp.error";
    
    //SnmpCommandResult
    public static final String MSG_COMMAND_FAILED = "snmp.commandFailed";
    //SnmpCommandResult
    public static final String MSG_COMMAND_SKIPPED = "snmp.commandSkipped";
    //SnmpCommandResult
    public static final String MSG_COMMAND_SUCCES = "snmp.commandSucces";
    
}
