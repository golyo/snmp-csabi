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

import com.zh.snmp.snmpcore.BaseTest;
import com.zh.snmp.snmpcore.MessageDebugAppender;
import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.DeviceType;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class TestSnmpManager extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestSnmpManager.class); 
    //@Autowired
    //private SmiMib m_mib;
    
    protected static String TEST_IP = "192.168.2.253"; 
    
    @Autowired
    private SnmpManager snmpManager;
    
    @Test
    public void testManager() throws IOException {
        DeviceConfigEntity config = createConfVoip("code");
        DeviceEntity device = snmpService.setDeviceConfig("testDevice", config.getCode());
        device.setIpAddress(TEST_IP);
        device = snmpService.saveDevice(device);
        
        LOGGER.debug("Device: " + device.toString());
        List<SnmpCommandResult> result = snmpManager.processOnDevice(SnmpManager.ProcessType.GET, device, DeviceType.VOIP, new MessageDebugAppender());
        LOGGER.debug("Result: " + result.toString());
    }
}
