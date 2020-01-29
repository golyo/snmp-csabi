package com.zh.snmp.snmpcore.services;

import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.domain.DinamicValue;
import com.zh.snmp.snmpcore.entities.ChangeLogEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.DeviceState;
import java.util.List;

/**
 *
 * @author Golyo
 */
public interface DeviceService {
    public Device findDeviceByDeviceId(String id);    
    public DeviceEntity findDeviceEntityById(String id);    
    public DeviceEntity findDeviceByIp(String ip);
    
    public String saveEntity(DeviceEntity device);
    public Device save(Device device);
    public List<DeviceEntity> findDeviceEntityByFilter(DeviceEntity filter, String sort, int start, int count);
    public int countDevices(DeviceEntity filter);
    public DeviceEntity findDeviceEntityByFilter(DeviceEntity filter);
    
    public Device setDeviceConfig(String nodeId, List<String> path, List<DinamicValue> dinamicValues, int mode);
    
    public boolean deleteDevice(String id);
    
    //public ChangeLogEntity saveLog(ChangeLogEntity log);
    public ChangeLogEntity findLog(Long id);
    public List<ChangeLogEntity> findLogs(ChangeLogEntity filter, String sort, int start, int count);
    public int countLogs(ChangeLogEntity filter);
    
    public List<DeviceEntity> getRetryUpdateDevices();
    public ChangeLogEntity changeDeviceState(String userName, DeviceEntity device, DeviceState newState, ChangeLogEntity originalLog);
}
