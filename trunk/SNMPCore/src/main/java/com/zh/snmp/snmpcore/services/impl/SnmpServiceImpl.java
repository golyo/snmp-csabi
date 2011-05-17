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
import com.zh.snmp.snmpcore.services.SnmpService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class SnmpServiceImpl implements SnmpService {
    @Autowired
    private DeviceConfigDao snmpTypedDao;
    @Autowired
    private DeviceDao clientDao;
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
            return snmpTypedDao.findExampleEntity(entity);            
        } else {
            return null;
        }
    }

    @Override
    public DeviceConfigEntity findDeviceConfigById(Long id) {
        return snmpTypedDao.load(id);
    }
    
    @Override
    public DeviceConfigEntity saveDeviceConfig(DeviceConfigEntity type) {
        DeviceConfigEntity ret = snmpTypedDao.save(type);
        snmpTypedDao.flush();
        return ret;
    }
    
    @Override
    public List<DeviceConfigEntity> findSnmpTypesByFilter(DeviceConfigEntity filter, String sort, int start, int count) {
        return snmpTypedDao.find(filter, sort, start, count);
    }
    
    @Override
    public int changeConfigToAllDevice(String oldConfigCode, String newConfigCode) {
        DeviceConfigEntity oldConfig = findDeviceConfigByCode(oldConfigCode);
        DeviceConfigEntity newConfig = findDeviceConfigByCode(newConfigCode);
        if (oldConfig == null || newConfig == null) {
            return 0;
        } else {
            DeviceEntity filterClient = new DeviceEntity();
            filterClient.setConfig(oldConfig);
            List<DeviceEntity> clients = clientDao.find(filterClient, null, 0, -1);
            for (DeviceEntity client: clients) {
                client.setConfig(newConfig);
                clientSnmpTypeChanged(client, oldConfig);
            }
            clientDao.flush();
            return clients.size();
        }
    }

    @Override
    public DeviceEntity setDeviceConfig(String ipAddress, String configCode) {
        DeviceConfigEntity config = findDeviceConfigByCode(configCode);
        if (config == null && configCode != null) {
            return null;
        } else {
            DeviceEntity filterClient = new DeviceEntity();
            filterClient.setIpAddress(ipAddress);
            //TODO
            DeviceEntity saveable = clientDao.findExampleEntity(filterClient);
            if (saveable == null) {
                saveable = filterClient;            
            }
            DeviceConfigEntity oldConfig = saveable.getConfig();
            saveable.setConfig(config);
            clientDao.save(saveable);
            clientSnmpTypeChanged(saveable, oldConfig);
            clientDao.flush();
            return saveable;            
        }
    }
    
    @Override
    public List<HistoryEntity> getDeviceHistory(HistoryEntity filter, String sort, int start, int count) {
        return historyDao.find(filter, sort, start, count);
    }
    
    private void clientSnmpTypeChanged(DeviceEntity device, DeviceConfigEntity oldConfig) {
        HistoryEntity log = new HistoryEntity();
        log.setDevice(device);
        log.setNewConfig(device.getConfig());
        log.setOldConfig(oldConfig);
        log.setUpdateTime(new Date());
        historyDao.save(log);
    }
}
