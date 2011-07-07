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

import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import java.util.List;

/**
 *
 * @author Golyo
 */
public interface DeviceService {
    public Device findDeviceByNodeId(String id);    
    public DeviceEntity findDeviceEntityById(String id);    
    public Device findDeviceByIp(String ip);
    
    public DeviceEntity saveEntity(DeviceEntity device);
    public Device save(Device device);
    public List<DeviceEntity> findDeviceEntityByFilter(DeviceEntity filter, String sort, int start, int count);
    public int countDevices(DeviceEntity filter);
    public DeviceEntity findDeviceEntityByFilter(DeviceEntity filter);
    
    
    public boolean setDeviceConfig(String nodeId, List<String> path, int mode);
}
