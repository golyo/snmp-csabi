package com.zh.snmp.snmpcore.services.impl;

import com.zh.snmp.snmpcore.BaseTest;
import com.zh.snmp.snmpcore.domain.Configuration;
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
/*        
        DeviceEntity de1 = createDevice(conf.getCode(), "TESTID", "TESTIP", "TESTNODE", "TESTMAC");
        deviceService.saveEntity(de1);
        
        DeviceEntity de2 = createDevice(conf.getCode(), "TESTID1", "TESTIP", "TESTNODE1", "TESTMAC1");
        deviceService.saveEntity(de2);
         **/
    }
    
}
