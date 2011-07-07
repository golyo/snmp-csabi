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
import com.zh.snmp.snmpcore.domain.ConfigNode;
import com.zh.snmp.snmpcore.domain.Configuration;
import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.domain.DeviceMap;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class ConfigServiceTest extends BaseTest {
    private static final String ACCES = "ACCES";
    
    @Autowired
    private ConfigService configService;
    @Autowired
    private DeviceService deviceService;
    
    @Test
    public void testConfig() {
        Configuration conf = createTestConfig();
        configService.saveConfig(conf);
        configService.clearCache();
        Configuration check = configService.findConfigByCode(ACCES);
        
        Device dev = createTestDevice(conf);
        dev = deviceService.save(dev);        
        Device test = deviceService.findDeviceByIp("ipAddress");
        
        int i = 0;
        int j = i;
        
    }
    
    //@Test
    public void testSave() {
        DeviceEntity de = new DeviceEntity();
        de.setId("NodeId");
        DeviceEntity dd = snmpService.saveDevice(de);

        int i = 0;
        int j = i;
    }
    private Device createTestDevice(Configuration config) {
        Device device = new Device();
        device.setConfig(config);
        device.setNodeId("nodeid");
        device.setIpAddress("ipAddress");
        device.setMacAddress("macAddress");
        DeviceMap map = new DeviceMap();
        
        map.setCode(config.getCode());
        List<DeviceMap> children = new LinkedList<DeviceMap>();
        DeviceMap internet = new DeviceMap();
        internet.setCode("internet");
        children.add(internet);
        device.setConfigMap(map);
        
        return device;
    }
    private Configuration createTestConfig() {
        Configuration conf = new Configuration();
        conf.setActive(true);
        conf.setCode(ACCES);
        conf.setName("Acces konfiguráció");        
        conf.setRoot(createVoipRoot());
        
        return conf;
    } 
    
    private ConfigNode createVoipRoot() {
        ConfigNode node = new ConfigNode();
        
        List<ConfigNode> children = new LinkedList<ConfigNode>();
        ConfigNode internet = new ConfigNode();
        internet.setCode("internet");
        children.add(internet);
        ConfigNode voip = new ConfigNode();
        voip.setCode("voip");
        children.add(voip);
        node.setChildren(children);
        return node;
    }
}
