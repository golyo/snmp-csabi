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

import com.zh.snmp.snmpcore.dao.ClientDao;
import com.zh.snmp.snmpcore.dao.HistoryDao;
import com.zh.snmp.snmpcore.dao.SnmpTypeDao;
import com.zh.snmp.snmpcore.dao.UserDao;
import com.zh.snmp.snmpcore.entities.HistoryEntity;
import com.zh.snmp.snmpcore.entities.ClientEntity;
import com.zh.snmp.snmpcore.entities.SnmpTypeEntity;
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
    private SnmpTypeDao snmpTypedDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private ClientDao changeLogDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private HistoryDao historyDao;
    
    @Override
    public SnmpTypeEntity findSnmpTypeByCode(String code) {
        if (code != null) {
            SnmpTypeEntity entity = new SnmpTypeEntity();
            entity.setCode(code);
            return snmpTypedDao.findExampleEntity(entity);            
        } else {
            return null;
        }
    }

    @Override
    public SnmpTypeEntity findSnmpTypeById(Long id) {
        return snmpTypedDao.load(id);
    }
    
    @Override
    public SnmpTypeEntity saveSnmpType(SnmpTypeEntity type) {
        SnmpTypeEntity ret = snmpTypedDao.save(type);
        snmpTypedDao.flush();
        return ret;
    }
    
    @Override
    public List<SnmpTypeEntity> findSnmpTypesByFilter(SnmpTypeEntity filter, String sort, int start, int count) {
        return snmpTypedDao.find(filter, sort, start, count);
    }
    
    @Override
    public int changeTypeToAllClient(String oldTypeCode, String newTypeCode) {
        SnmpTypeEntity oldType = findSnmpTypeByCode(oldTypeCode);
        SnmpTypeEntity newType = findSnmpTypeByCode(newTypeCode);
        if (oldType == null || newType == null) {
            return 0;
        } else {
            ClientEntity filterClient = new ClientEntity();
            filterClient.setType(oldType);
            List<ClientEntity> clients = clientDao.find(filterClient, null, 0, -1);
            for (ClientEntity client: clients) {
                client.setType(newType);
                clientSnmpTypeChanged(client, oldType);
            }
            clientDao.flush();
            return clients.size();
        }
    }

    @Override
    public ClientEntity setSnmpTypeToClient(String ipAddress, String typeCode) {
        SnmpTypeEntity type = findSnmpTypeByCode(typeCode);
        if (type == null && typeCode != null) {
            return null;
        } else {
            ClientEntity filterClient = new ClientEntity();
            filterClient.setIpAddress(ipAddress);
            ClientEntity saveable = clientDao.findExampleEntity(filterClient);
            if (saveable == null) {
                saveable = filterClient;            
            }
            SnmpTypeEntity oldType = saveable.getType();
            saveable.setType(type);
            clientDao.save(saveable);
            clientSnmpTypeChanged(saveable, oldType);
            clientDao.flush();
            return saveable;            
        }
    }
    
    @Override
    public List<HistoryEntity> getClientHistory(HistoryEntity filter, String sort, int start, int count) {
        return historyDao.find(filter, sort, start, count);
    }
    
    private void clientSnmpTypeChanged(ClientEntity client, SnmpTypeEntity oldType) {
        HistoryEntity log = new HistoryEntity();
        log.setClient(client);
        log.setNewType(client.getType());
        log.setOldType(oldType);
        log.setUpdateTime(new Date());
        historyDao.save(log);
    }
}
