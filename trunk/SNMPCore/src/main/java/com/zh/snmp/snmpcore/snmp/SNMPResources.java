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
