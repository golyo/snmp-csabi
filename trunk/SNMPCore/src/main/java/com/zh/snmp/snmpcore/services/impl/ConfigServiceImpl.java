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

import com.zh.snmp.snmpcore.dao.DeviceConfigDao;
import com.zh.snmp.snmpcore.domain.ConfigNode;
import com.zh.snmp.snmpcore.domain.Configuration;
import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpcore.util.JAXBUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private DeviceConfigDao dao;
    
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
    public synchronized Configuration saveConfig(Configuration config) {
        DeviceConfigEntity entity = wrap(config);
        entity = dao.save(entity);
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
    public DeviceConfigEntity saveEntity(DeviceConfigEntity entity) {
        JAXBUtil.unmarshal(entity.getSnmpDescriptor(), ConfigNode.class);
        return dao.save(entity);
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
    public void importConfiguration(InputStream stream) throws IOException {
        String descriptor = IOUtils.toString(stream);
        ConfigNode node = JAXBUtil.unmarshalTyped(new StringReader(descriptor), ConfigNode.class);        
        DeviceConfigEntity conf = new DeviceConfigEntity();
        conf.setId(node.getCode());
        conf.setName(node.getDescription());
        conf.setActive(true);
        conf.setSnmpDescriptor(descriptor);
        dao.save(conf);
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
