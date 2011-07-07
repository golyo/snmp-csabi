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

import com.zh.snmp.snmpcore.domain.Configuration;
import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author Golyo
 */
public interface ConfigService {
    public void loadConfigurations();
    public Configuration saveConfig(Configuration config);
    public Configuration findConfigByCode(String code);
    public DeviceConfigEntity findConfigEntityByCode(String code);
    public List<DeviceConfigEntity> findDeviceConfigByFilter(DeviceConfigEntity filter, String sort, int start, int count);
    public int countConfigEntity(DeviceConfigEntity filter);
    
    public DeviceConfigEntity saveEntity(DeviceConfigEntity entity);
    public void clearCache();
    public List<String> getConfigCodes();
    public void importConfiguration(InputStream stream) throws IOException;
}
