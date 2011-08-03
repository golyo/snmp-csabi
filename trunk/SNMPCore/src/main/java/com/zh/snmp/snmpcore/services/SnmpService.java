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
package com.zh.snmp.snmpcore.services;

import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.services.impl.SnmpBackgroundProcess;

/**
 *
 * @author Golyo
 */
public interface SnmpService {    
    public SnmpBackgroundProcess startSnmpBackgroundProcess(Device device, MessageAppender appender);
    public boolean applyConfigOnDevice(Device device, MessageAppender appender);
    public boolean checkDevice(Device device, MessageAppender appender);
}
