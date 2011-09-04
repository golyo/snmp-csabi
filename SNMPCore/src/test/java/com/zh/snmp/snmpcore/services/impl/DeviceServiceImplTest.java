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

import com.zh.snmp.snmpcore.BaseTest;
import com.zh.snmp.snmpcore.domain.Configuration;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.message.SimpleMessageAppender;
import org.junit.Test;

/**
 *
 * @author Golyo
 */
public class DeviceServiceImplTest extends BaseTest {
    @Test
    public void testUniques() {
        MessageAppender appaneder = new SimpleMessageAppender();
        Configuration conf = createTestConfig(appaneder, "voipConfig.xml");
        
        
        DeviceEntity de1 = createDevice(conf.getCode(), "TESTID", "TESTIP", "TESTNODE", "TESTMAC");
        deviceService.saveEntity(de1);
        
        DeviceEntity de2 = createDevice(conf.getCode(), "TESTID1", "TESTIP", "TESTNODE1", "TESTMAC1");
        deviceService.saveEntity(de2);
    }
    
}
