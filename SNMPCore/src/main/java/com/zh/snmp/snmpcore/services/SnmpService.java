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
package com.zh.snmp.snmpcore.services;

import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.HistoryEntity;
import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import java.util.List;

/**
 *
 * @author Golyo
 */
public interface SnmpService {
    public List<DeviceConfigEntity> findDeviceConfigByFilter(DeviceConfigEntity filter, String sort, int start, int count);
    public DeviceConfigEntity saveDeviceConfig(DeviceConfigEntity type);
    public DeviceConfigEntity findDeviceConfigByCode(String code);
    public DeviceConfigEntity findDeviceConfigById(Long id);
    public int countDeviceHistory(DeviceConfigEntity filter);
    
    public DeviceEntity findDeviceById(Long id);
    public DeviceEntity findDeviceByNodeId(String nodeId);
    public DeviceEntity findDeviceByFilter(DeviceEntity filter);
    public List<DeviceEntity> findDeviceByFilter(DeviceEntity filter, String sort, int start, int count);
    public DeviceEntity saveDevice(DeviceEntity device);
    public int countDevice(DeviceEntity filter);
    
    public HistoryEntity findHsitoryById(Long id);
    public List<HistoryEntity> findHistoryByFilter(HistoryEntity filter, String sort, int start, int count);
    public int countHistory(HistoryEntity filter);
        
    public DeviceEntity setDeviceConfig(String ipAddress, String configCode);
    public int changeConfigToAllDevice(String oldTypeCode, String newTypeCode);
    
    public List<DeviceConfigEntity> getDeviceHistory(DeviceConfigEntity filter, String sort, int start, int count);
    public List<HistoryEntity> getDeviceHistory(HistoryEntity filter, String sort, int start, int count);
}
