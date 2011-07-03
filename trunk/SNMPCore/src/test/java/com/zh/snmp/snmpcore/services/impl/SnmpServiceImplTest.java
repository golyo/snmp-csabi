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
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.HistoryEntity;
import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.entities.DeviceType;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.io.IOException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;

/**
 *
 * @author Golyo
 */
public class SnmpServiceImplTest extends BaseTest {
    
    @Autowired
    private SnmpService snmpService;
    
    public SnmpServiceImplTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSnmpService() {
        DeviceConfigEntity type = new DeviceConfigEntity();
        type.setActive(true);
        type.setName("testName");
        type.setCode("testCode");
        type.setDeviceType(DeviceType.VOIP);
        type.setSnmpDescriptor("testDescriptor");
        DeviceConfigEntity saved = snmpService.saveDeviceConfig(type);
        assertNotNull(saved.getId());
        
        DeviceConfigEntity check = snmpService.findDeviceConfigById(saved.getId());
        assertNotNull(check);
        
        List<DeviceConfigEntity> ls = snmpService.findDeviceConfigByFilter(new DeviceConfigEntity(), null, 0, -1);
        assertEquals(ls.size(), 1);
        
        check = snmpService.findDeviceConfigByCode(type.getCode());
        assertNotNull(check);
        
        String nodeId = "ip";
        DeviceEntity device = snmpService.setDeviceConfig(nodeId, "unknowType");
        assertNull(device);
        device = snmpService.setDeviceConfig(nodeId, type.getCode());
        assertNotNull(device);
        assertEquals(device.getNodeId(), nodeId);
        
        List<HistoryEntity> histories = snmpService.getDeviceHistory(new HistoryEntity(), null, 0, -1);
        assertEquals(histories.size(), 1);
        
        device = snmpService.setDeviceConfig(nodeId, null);
        assertTrue(device.getConfigurations().isEmpty());
        //assertNull(device.getConfig());

        histories = snmpService.getDeviceHistory(new HistoryEntity(), null, 0, -1);
        assertEquals(histories.size(), 2);
    }

    @Test
    public void testConfigs() throws IOException {
        //DeviceConfigEntity conf1 = createConfVoip("test1");
        //DeviceConfigEntity conf2 = createConfVoip("test2");
    }
    
}
