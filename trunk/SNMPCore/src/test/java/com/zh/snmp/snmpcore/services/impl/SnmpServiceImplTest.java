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
import com.zh.snmp.snmpcore.domain.CommandType;
import com.zh.snmp.snmpcore.domain.Configuration;
import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.domain.OidCommand;
import com.zh.snmp.snmpcore.domain.SnmpCommand;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.message.BackgroundProcess;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.message.SimpleMessageAppender;
import com.zh.snmp.snmpcore.snmp.SnmpCommandManager;
import com.zh.snmp.snmpcore.snmp.SnmpFactory;
import java.util.Arrays;
import java.util.BitSet;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;

/**
 *
 * @author Golyo
 */
public class SnmpServiceImplTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnmpServiceImplTest.class);
    private static final String TESTIP = "192.168.2.253/161";
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSnmpService() throws Exception {
        String ip = TESTIP;
        MessageAppender appender = new SimpleMessageAppender();
        Configuration conf = createTestConfig(appender, "accessConfig.xml");
        DeviceEntity de = createTestDevice(conf.getCode(), ip);
        Device device = deviceService.findDeviceByDeviceId(de.getId());
        Assert.assertNotNull(device);
        //device.getConfigMap().findChild("internet").setSelected(true);
        //device.getConfigMap().findChild("catv").setSelected(true);
        device = deviceService.save(device);
        
        
        
        BackgroundProcess bp = snmpService.startSnmpBackgroundProcess("TESTUSER", de.getId(), appender);
        while (!bp.isFinished()) {
            Thread.sleep(100);
        }
        //snmpService.applyConfigOnDevice(device, appender);
        //snmpService.startSnmpBackgroundProcess(de.getIpAddress(), appender);
        
        LOGGER.debug(appender.toString());
/*
        LOGGER.debug(device.getConfigMap().getSelectedChildList().toString());
        device.getConfigMap().findChild("internet").setSelected(true);
        LOGGER.debug(device.getConfigMap().getSelectedChildList().toString());
        device.getConfigMap().findChild("catv").setSelected(true);
        LOGGER.debug(device.getConfigMap().getSelectedChildList().toString());
 * 
 */
        int i = 1;
        int j = i;
    }
    
    //@Test
    public void testCommnand() throws Exception {
        String ip = TESTIP;
        MessageAppender appender = new SimpleMessageAppender();
        Configuration conf = createTestConfig(appender, "accessConfig.xml");
        DeviceEntity de = createTestDevice(conf.getCode(), ip);
        
        Device d = deviceService.findDeviceByDeviceId(de.getId());
        Snmp snmp = SnmpFactory.createSnmp();
        SnmpCommandManager cm = new SnmpCommandManager(snmp, appender, d.getIpAddress());
        
        int idx = 0;
        BitSet set = new BitSet();
        set.set(1);
        set.set(2);
        set.set(3);
        CommunityTarget target = DeviceSettings.TEST_DEVICE.createTarget(true);
        for (SnmpCommand cmd: conf.getRoot().getCommands()) {
            if (set.get(idx)) {
                for (OidCommand ocmd: cmd.getCommands()) {
                    SnmpCommand testcmd = new SnmpCommand();
                    testcmd.setCommands(Arrays.asList(ocmd));
                    cm.processSetCommand(cmd.getCommands());
                    if (cm.canContinue()) {
                        idx = 10;
                        LOGGER.error("COMMAND FAILED " + testcmd);
                        break;
                    }
                }
            }
            idx++;
        }
        //snmpService.applyConfigOnDevice(ip, appender);
        
        LOGGER.debug(appender.toString());
        int i = 1;
        int j = i;
     }
}
