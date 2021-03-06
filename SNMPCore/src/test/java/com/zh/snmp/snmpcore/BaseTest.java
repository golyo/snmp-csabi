package com.zh.snmp.snmpcore;

import com.zh.snmp.snmpcore.domain.ConfigNodeTest;
import com.zh.snmp.snmpcore.domain.Configuration;
import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.DeviceState;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import com.zh.snmp.snmpcore.snmp.mib.MibParser;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import javax.naming.NamingException;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    protected ConfigService configService;
    @Autowired
    protected DeviceService deviceService;
    
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
    
    protected Configuration createTestConfig(MessageAppender appender, String resource) {
        InputStream stream = ConfigNodeTest.class.getResourceAsStream(resource);
        Configuration conf = configService.importConfiguration(stream, appender);
        return conf;
    }
    
    protected DeviceEntity createTestDevice(String configCode, String ip) {
        DeviceEntity e = new DeviceEntity();
        e.setConfigCode(configCode);
        e.setConfigState(DeviceState.NEW);
        e.setId("ID" + ip.substring(0,3));
        e.setMacAddress("MAC" + ip.substring(0,3));
        e.setIpAddress(ip);
        e.setNodeId("NODE" + ip.substring(0,3));
        Assert.assertEquals("succes", deviceService.saveEntity(e));
        return e;
    }
    
    protected DeviceEntity createDevice(String configCode, String deviceId, String ip, String nodeId, String macAddress) {
        DeviceEntity ret = new DeviceEntity();
        ret.setId(deviceId);
        ret.setNodeId(nodeId);
        ret.setIpAddress(ip);
        ret.setMacAddress(macAddress);
        ret.setConfigCode(configCode);
        return ret;
    }
    
    protected DeviceConfigEntity createConfVoip(String code) throws IOException {
        /*
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
         * 
         */
        return  null;
    }
}
