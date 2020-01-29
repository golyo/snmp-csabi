package com.zh.snmp.snmpcore.snmp;

import com.zh.snmp.snmpcore.domain.ConfigNode;
import com.zh.snmp.snmpcore.domain.DeviceNode;
import com.zh.snmp.snmpcore.exception.ExceptionCodesEnum;
import com.zh.snmp.snmpcore.exception.SystemException;
import com.zh.snmp.snmpcore.message.SimpleMessageAppender;
import com.zh.snmp.snmpcore.snmp.mib.MibParser;
import com.zh.snmp.snmpcore.util.JAXBUtil;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class UpgradeConfig implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpgradeConfig.class);
    
    private static final String CONFIG_FILE = "upgradeConfig.xml";
    
    @Autowired
    private MibParser mibParser;
    
    private ConfigNode upgradeConfig;
    
    private Map<String, DeviceNode> nodes = new HashMap<String, DeviceNode>();
    
    private File snmpDir;
    
    @Override
    public void afterPropertiesSet() {
        snmpDir = new File(System.getProperty("user.home"), ".snmp");
        if (!snmpDir.exists() || !snmpDir.isDirectory()) {
            throw new SystemException(ExceptionCodesEnum.ConfigurationException, ".snmp dir not exists under user home");              
        }
        InputStream stream = UpgradeConfig.class.getResourceAsStream(CONFIG_FILE);
        try {
            upgradeConfig = JAXBUtil.unmarshalTyped(new InputStreamReader(stream, "UTF-8"), ConfigNode.class);            
        } catch (UnsupportedEncodingException e) {
            throw new SystemException(ExceptionCodesEnum.ConfigurationException, e);  
        }
        SimpleMessageAppender appender = new SimpleMessageAppender();
        if (!mibParser.parseAndSetMibValues(upgradeConfig, appender)) {
            throw new SystemException(ExceptionCodesEnum.ConfigurationException, "Wrong upgrade command " + appender.toString());  
        }
    }

    public ConfigNode getUpgradeConfig() {
        return upgradeConfig;
    }
    
    public synchronized DeviceNode getUpgradeNode(String config) {
        if (nodes.containsKey(config)) {
            return nodes.get(config);
        } else {
            Properties props = loadProperties(config + "Upgrade.properties");
            DeviceNode ret = null;
            if (props != null) {
                ret = new UpgradeNode(config, props);
            }
            nodes.put(config, ret);
            return ret;
        }
    }
    
    private Properties loadProperties(String fileName) {
        Properties props = null;
        File f = new File(snmpDir, fileName);
        if (f.exists()) {
            FileReader reader = null;
            try {
                reader = new FileReader(f);
                props = new Properties();
                props.load(reader);           
                LOGGER.info("Property file loaded " + f.getName());
            } catch (IOException e) {
                LOGGER.error("Wrong upgrade property file " + f.getName(), e);
                props = null;
            } finally {
                IOUtils.closeQuietly(reader);                
            }
        } else {
            LOGGER.warn("Upgrade property file not found " + f.getName());
        }
        return props;
    }
}
