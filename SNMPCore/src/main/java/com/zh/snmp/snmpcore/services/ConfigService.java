package com.zh.snmp.snmpcore.services;

import com.zh.snmp.snmpcore.domain.Configuration;
import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.message.MessageAppender;
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
    public Configuration importConfiguration(InputStream stream, MessageAppender appender);
}
