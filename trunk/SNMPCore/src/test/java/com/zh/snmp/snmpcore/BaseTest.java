/*
 *  Copyright 2010 sonrisa.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.zh.snmp.snmpcore;

import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.entities.DeviceType;
import com.zh.snmp.snmpcore.services.SnmpService;
import com.zh.snmp.snmpcore.snmp.mib.MibParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.SQLException;
import javax.naming.NamingException;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sonrisa
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:applicationContext.xml",
    "classpath:snmp-applicationContext.xml",
    "classpath:dao-applicationContext.xml",
    "classpath:test-dao-partial-applicationContext.xml",
    "classpath:security-applicationContext.xml",
    "classpath:service-applicationContext.xml"})
@Transactional
public abstract class BaseTest {
    protected static final String TEST_FILE = "test_config_mib.txt";
    
    @Autowired
    protected SnmpService snmpService;
    
    @BeforeClass
    public static void setUpBaseClass() throws NamingException, SQLException {
    }


    @Before
    public void setUpBase() {
        System.out.println("beforebase");
    }

    @After
    public void tearDownBase() {
    }    
    
    public DeviceConfigEntity getTextDevice(String code) {
        return null;
    }
    
    protected InputStream gettestMibStream() {
        return MibParser.class.getResourceAsStream(TEST_FILE);
    }
    
    protected DeviceConfigEntity createConfVoip(String code) throws IOException {
        DeviceConfigEntity type = new DeviceConfigEntity();
        type.setActive(true);
        type.setName(code);
        type.setCode(code);
        type.setDeviceType(DeviceType.VOIP);
        //InputStream stream = 
        type.setSnmpDescriptor(IOUtils.toString(gettestMibStream()));
        DeviceConfigEntity saved = snmpService.saveDeviceConfig(type);
        Assert.assertNotNull(saved.getId());
        return saved;        
    }
}
