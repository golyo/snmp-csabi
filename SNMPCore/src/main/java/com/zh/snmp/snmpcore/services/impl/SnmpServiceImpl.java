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

import com.zh.snmp.snmpcore.dao.TestDao;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.services.SnmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Golyo
 */
@Transactional
public class SnmpServiceImpl implements SnmpService {
    @Autowired
    private TestDao testDao;

    @Override
    public DeviceEntity saveDevice(DeviceEntity device) {
        return testDao.save(device);
    }
    /*
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
      */      
}
