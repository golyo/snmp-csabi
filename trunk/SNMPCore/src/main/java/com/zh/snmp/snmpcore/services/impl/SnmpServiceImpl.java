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

import com.zh.snmp.snmpcore.dao.DeviceDao;
import com.zh.snmp.snmpcore.dao.HistoryDao;
import com.zh.snmp.snmpcore.dao.DeviceConfigDao;
import com.zh.snmp.snmpcore.dao.UserDao;
import com.zh.snmp.snmpcore.entities.HistoryEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.exception.ExceptionCodesEnum;
import com.zh.snmp.snmpcore.exception.SystemException;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class SnmpServiceImpl implements SnmpService {
    @Autowired
    private DeviceConfigDao deviceConfigDao;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private DeviceDao changeLogDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private HistoryDao historyDao;
    
    @Override
    public DeviceConfigEntity findDeviceConfigByCode(String code) {
        if (code != null) {
            DeviceConfigEntity entity = new DeviceConfigEntity();
            entity.setCode(code);
            return deviceConfigDao.findExampleEntity(entity);                        
        } else {
            return null;
        }
    }

    @Override
    public DeviceConfigEntity findDeviceConfigById(Long id) {
        return deviceConfigDao.load(id);
    }
    
    @Override
    public DeviceConfigEntity saveDeviceConfig(DeviceConfigEntity type) {
        DeviceConfigEntity ret = deviceConfigDao.save(type);
        deviceConfigDao.flush();
        return ret;
    }
    
    @Override
    public List<DeviceConfigEntity> findDeviceConfigByFilter(DeviceConfigEntity filter, String sort, int start, int count) {
        return deviceConfigDao.find(filter, sort, start, count);
    }
    
    @Override
    public DeviceEntity findDeviceById(Long id) {
        return deviceDao.load(id);
    }
    
    @Override
    public DeviceEntity findDeviceByNodeId(String nodeId) {
        DeviceEntity device = new DeviceEntity();
        device.setNodeId(nodeId);
        return deviceDao.findExampleEntity(device);
    }
    
    @Override
    public List<DeviceEntity> findDeviceByFilter(DeviceEntity filter, String sort, int start, int count) {
        return deviceDao.find(filter, sort, start, count);
    }
    
    @Override
    public DeviceEntity saveDevice(DeviceEntity device) {      
        return deviceDao.save(device); 
        /*
        DeviceConfigEntity old = device.getId() != null ? deviceDao.load(device.getId()).getConfig() : null;                
        
        DeviceEntity newDevice = deviceDao.save(device);
        if ((newDevice.getConfig() != null && !newDevice.getConfig().equals(old)) || (newDevice.getConfig() == null && old != null)) {
            clientSnmpTypeChanged(newDevice, old);          
        }
        return newDevice;
         * 
         */
    }
    
    @Override
    public int countDevice(DeviceEntity filter) {
        return deviceDao.count(filter);
    }
    
    @Override
    public int changeConfigToAllDevice(String oldConfigCode, String newConfigCode) {
        return -1;
        /*
        DeviceConfigEntity oldConfig = findDeviceConfigByCode(oldConfigCode);
        DeviceConfigEntity newConfig = findDeviceConfigByCode(newConfigCode);
        if (oldConfig.getDeviceType() != newConfig.getDeviceType()) {
            throw new SystemException(ExceptionCodesEnum.Unsupported);
        }
        if (oldConfig == null || newConfig == null) {
            return 0;
        } else {
            /*
            DeviceEntity filterClient = new DeviceEntity();
            Set<DeviceConfigEntity> filterSet = new HashSet<DeviceConfigEntity>();
            filterClient.setConfig(oldConfig);
            List<DeviceEntity> clients = deviceDao.find(filterClient, null, 0, -1);
            for (DeviceEntity client: clients) {
                client.setConfig(newConfig);
                clientSnmpTypeChanged(client, oldConfig);
            }
            deviceDao.flush();
            return clients.size();
             
        }
         * 
         */
    }

    @Override
    public DeviceEntity setDeviceConfig(String nodeId, String configCode) {
        DeviceConfigEntity config = findDeviceConfigByCode(configCode);
        if (config == null && configCode != null) {
            return null;
        } 
        DeviceEntity filterClient = new DeviceEntity();
        filterClient.setNodeId(nodeId);
        //TODO
        DeviceEntity saveable = deviceDao.findExampleEntity(filterClient);
        if (saveable == null) {
            saveable = filterClient;            
        }
        if (configCode == null) {
            for (DeviceConfigEntity conf: saveable.getConfigurations()) {
                clientSnmpTypeChanged(saveable, conf, null);
            }
            saveable.setConfigurations(Collections.EMPTY_SET);
        } else {
            DeviceConfigEntity oldConfig = saveable.changeConfig(config);
            saveable = deviceDao.save(saveable);
            clientSnmpTypeChanged(saveable, oldConfig, config);
        }
        deviceDao.flush();
        return saveable;            
    }
    
    @Override
    public List<HistoryEntity> getDeviceHistory(HistoryEntity filter, String sort, int start, int count) {
        return historyDao.find(filter, sort, start, count);
    }
    
    private void clientSnmpTypeChanged(DeviceEntity device, DeviceConfigEntity oldConfig, DeviceConfigEntity newConfig) {
        HistoryEntity log = new HistoryEntity();
        log.setDevice(device);
        log.setNewConfig(newConfig);
        log.setOldConfig(oldConfig);
        log.setUpdateTime(new Date());
        historyDao.save(log);
    }
    
    @Override
    public List<DeviceConfigEntity> getDeviceHistory(DeviceConfigEntity filter, String sort, int start, int count) {
        return deviceConfigDao.find(filter, sort, start, count);
    }
    
    @Override
    public int countDeviceHistory(DeviceConfigEntity filter) {
        return deviceConfigDao.count(filter);
    }

    @Override
    public HistoryEntity findHsitoryById(Long id) {
        return historyDao.load(id);
    }
    
    @Override
    public List<HistoryEntity> findHistoryByFilter(HistoryEntity filter, String sort, int start, int count) {
        return historyDao.find(filter, sort, start, count);
    }
    
    @Override
    public int countHistory(HistoryEntity filter) {
        return historyDao.count(filter);
    }
            
}
