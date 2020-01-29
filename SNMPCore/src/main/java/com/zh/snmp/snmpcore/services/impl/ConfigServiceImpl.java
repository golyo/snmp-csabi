package com.zh.snmp.snmpcore.services.impl;

import com.zh.snmp.snmpcore.dao.DeviceConfigDao;
import com.zh.snmp.snmpcore.domain.ConfigNode;
import com.zh.snmp.snmpcore.domain.Configuration;
import com.zh.snmp.snmpcore.domain.SnmpCommand;
import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpcore.snmp.mib.MibParser;
import com.zh.snmp.snmpcore.util.JAXBUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Golyo
 */

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ConfigServiceImpl implements ConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceImpl.class);
    
    @Autowired
    private DeviceConfigDao dao;
    @Autowired
    private MibParser mibParser;
    
    private Map<String, Configuration> cache;
    
    public ConfigServiceImpl() {
        cache = new HashMap<String, Configuration>();
    }
    
    @Override
    public Configuration findConfigByCode(String code) {
        Configuration config = cache.get(code);
        if (config == null) {
            DeviceConfigEntity entity = dao.load(code);
            if (entity == null) {
                return null;
            } else {
                config = unwrap(entity);
                cache.put(code, config);                
            }
        }
        return config;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public synchronized Configuration saveConfig(Configuration config) {
        DeviceConfigEntity entity = wrap(config);
        entity = dao.save(entity);
        dao.flush();
        Configuration merged = unwrap(entity);
        cache.put(merged.getCode(), merged);
        return merged;
    }
    
    @Override
    public List<DeviceConfigEntity> findDeviceConfigByFilter(DeviceConfigEntity filter, String sort, int start, int count) {
        return dao.find(filter, sort, start, count);
    }
    
    @Override
    public DeviceConfigEntity findConfigEntityByCode(String code) {
        return dao.load(code);
    }
    
    @Override
    public int countConfigEntity(DeviceConfigEntity filter) {
        return dao.count(filter);
    }
    
    @Override
    public void clearCache() {
        cache.clear();
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public DeviceConfigEntity saveEntity(DeviceConfigEntity entity) {
        JAXBUtil.unmarshal(entity.getSnmpDescriptor(), ConfigNode.class);
        DeviceConfigEntity ret = dao.save(entity);
        dao.flush();
        return ret;
    }
    
    protected DeviceConfigEntity wrap(Configuration config) {
        DeviceConfigEntity entity = new DeviceConfigEntity();
        entity.setId(config.getCode());
        entity.setName(config.getName());
        entity.setActive(config.getActive());
        entity.setSnmpDescriptor(JAXBUtil.marshal(config.getRoot(), true));
        return entity;
    }
    
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Configuration importConfiguration(InputStream stream, MessageAppender appender) {        
        Configuration conf = new Configuration();
        try {
            String descriptor = IOUtils.toString(stream, "UTF8");    
            conf.setRoot(JAXBUtil.unmarshalTyped(new StringReader(descriptor), ConfigNode.class));
            conf.setCode(conf.getRoot().getCode());
            conf.setName(conf.getRoot().getDescription());
            conf.setActive(Boolean.TRUE);
        } catch (IOException e) {
            appender.addMessage("error.import.config");
            LOGGER.error("error while import config", e);
        }
        if (conf.getRoot() != null) {
            if (validateConfigNode(conf.getRoot(), appender)) {
                return saveConfig(conf);                
            }
        }
        return null;
    }
    
    private boolean validateConfigNode(ConfigNode node, MessageAppender appender) {
        List<SnmpCommand> commands = node.getCommands();
        boolean ret = true;
        if (commands != null) {
            for (SnmpCommand command: commands) {
                ret = mibParser.parseAndSetMibValues(command, appender) && ret;
            }                        
        }
        for (ConfigNode child: node.getChildren()) {
            ret = validateConfigNode(child, appender) && ret;
        }
        return ret;
    }

    @Override
    public void loadConfigurations() {
        List<DeviceConfigEntity> configs = dao.find(new DeviceConfigEntity(), null, 0, -1);
        for (DeviceConfigEntity c: configs) {
            cache.put(c.getId(), unwrap(c));
        }
    }
    
    @Override
    public List<String> getConfigCodes() {
        return new ArrayList<String>(cache.keySet());
    }
    protected Configuration unwrap(DeviceConfigEntity entity) {
        Configuration config = new Configuration();
        config.setActive(entity.getActive());
        config.setCode(entity.getId());
        config.setName(entity.getName());
        if (entity.getSnmpDescriptor() != null) {
            config.setRoot(JAXBUtil.unmarshal(entity.getSnmpDescriptor(), ConfigNode.class));
        }
        config.getRoot().setupParents();
        return config;
    }
}
