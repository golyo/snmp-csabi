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
import com.zh.snmp.snmpcore.domain.DeviceNode;
import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.message.SimpleMessageAppender;
import com.zh.snmp.snmpcore.snmp.mib.MibParser;
import com.zh.snmp.snmpcore.util.JAXBUtil;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.OID;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;
/**
 *
 * @author Golyo
 */
public class ConfigServiceTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceTest.class);
    private static final String ACCES = "ACCES";
    
    @Autowired
    private MibParser mibParser;
    
    @Test
    public void testConfig() throws Exception {
        Configuration conf = createTestAccesConfig();
        configService.saveConfig(conf);
        configService.clearCache();
        Configuration check = configService.findConfigByCode(ACCES);
        List<DeviceConfigEntity> configs = configService.findDeviceConfigByFilter(new DeviceConfigEntity(), ACCES, 0, -1);
        
        String ip = "testip";
        DeviceEntity de = createTestDevice(conf.getCode(), ip);
        assertNotNull(de);
        
        
        Device device = deviceService.findDeviceByDeviceId(de.getId());
        assertNotNull(device);
        
        List<DeviceEntity> devices = deviceService.findDeviceEntityByFilter(new DeviceEntity(), null, 0, -1);
        
        DeviceEntity deentity = deviceService.findDeviceByIp(ip);
        assertNotNull(deentity);
        
        List<DeviceEntity> retries = deviceService.getRetryUpdateDevices();
        assertFalse(retries.isEmpty());
        
        LOGGER.debug("Sleeep");
        Thread.sleep(20000);
        LOGGER.debug("Sleeep finished");
    }
    
    @Test
    public void testParse() {
        MessageAppender appaneder = new SimpleMessageAppender();
        Configuration conf = createTestConfig(appaneder, "voipConfig.xml");
        ConfigNode node = conf.getRoot();
        
        //OidCommand pwdcmd = line1.getCommands().get(0).getCommands().get(3);
        //pwdcmd.setValueConverter(ValueConverter.SHA1);
        LOGGER.debug("+++" + conf.toString());
        LOGGER.debug(JAXBUtil.marshal(node, true));
    }
    
    @Test
    public void testMib() throws Exception {
        String name = "PACKETFRONT-DRG-MIB:productSwImageRev.0";
        OID oid = mibParser.parseMib(name);
        System.out.println("++++++++++++++++++++++++++++++++" + oid.toString());
    }

    private Device createTestDevice(Configuration config) {
        Device device = new Device();
        device.setConfig(config);
        device.setDeviceId("nodeid");
        device.setIpAddress("ipAddress");
        device.setMacAddress("macAddress");
        DeviceNode map = new DeviceNode(config.getRoot());        
        device.setConfigMap(map);
        
        return device;
    }
    private Configuration createTestAccesConfig() {
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
        voip.setCode(ACCES);
        children.add(voip);
        node.setChildren(children);
        return node;
    }
}
