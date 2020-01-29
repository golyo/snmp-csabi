package com.zh.snmp.snmpcore.services.impl;

import com.zh.snmp.snmpcore.dao.ChangeLogDao;
import com.zh.snmp.snmpcore.dao.DeviceDao;
import com.zh.snmp.snmpcore.domain.Configuration;
import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.domain.DeviceNode;
import com.zh.snmp.snmpcore.domain.DinamicValue;
import com.zh.snmp.snmpcore.entities.ChangeLogEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.DeviceState;
import com.zh.snmp.snmpcore.exception.ExceptionCodesEnum;
import com.zh.snmp.snmpcore.exception.SystemException;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.util.JAXBUtil;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Golyo
 */
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private ConfigService configService;
    
    @Autowired
    private DeviceDao dao;
    
    @Autowired
    private ChangeLogDao logDao;
    
    
    @Override
    public Device findDeviceByDeviceId(String nodeId) {      
        if (nodeId == null) {
            return null;
        }
        DeviceEntity entity =dao.load(nodeId);
        return entity != null ? unwrap(entity) : null;
    }

    @Override
    public DeviceEntity findDeviceByIp(String ip) {
        if (ip == null) {
            return null;
        }
        DeviceEntity filter = new DeviceEntity();
        filter.setIpAddress(ip);
        return dao.findExampleEntity(filter);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Device save(Device device) {
        DeviceEntity modified = wrap(device);
        DeviceEntity merged = dao.save(modified);
        return unwrap(merged);
    }
    
    @Override
    public DeviceEntity findDeviceEntityById(String id) {
        return dao.load(id);
    }    
    
    @Override
    public List<DeviceEntity> findDeviceEntityByFilter(DeviceEntity filter, String sort, int start, int count) {
        return dao.find(filter, sort, start, count);
    }
    
    @Override
    public int countDevices(DeviceEntity filter) {
        return dao.count(filter);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public String saveEntity(DeviceEntity device) {
        String errKey = dao.checkDevice(device);
        if (errKey != null) {
            return errKey;
        }
        if (device.getDeviceMap() == null) {
            Configuration config = configService.findConfigByCode(device.getConfigCode());
            if (config != null) {
                DeviceNode dm = new DeviceNode(config.getRoot());
                dm.setSelected(true);
                device.setDeviceMap(JAXBUtil.marshal(dm, true));                            
            } else {
                throw new SystemException(ExceptionCodesEnum.Unsupported, "Config code not found " + device.getConfigCode());
            }
        }
        if (device.getConfigState() == null) {
            device.setConfigState(DeviceState.NEW);
        }
        
        DeviceEntity ret = dao.save(device);
        dao.flush();
        return null;
    }
        
    @Override
    public DeviceEntity findDeviceEntityByFilter(DeviceEntity filter) {
        return dao.findExampleEntity(filter);
    }    
    
    @Override
    public List<DeviceEntity> getRetryUpdateDevices() {
        return dao.getRetryUpdateDevices();
    }
    
    @Override
    public Device setDeviceConfig(String nodeId, List<String> path, List<DinamicValue> dinamicValues, int mode) {
        Device device = findDeviceByDeviceId(nodeId);
        if (device == null) {
            return null;
        }
        LinkedList<String> pathl = new LinkedList<String>(path);
        DeviceNode dconfig = device.getConfigMap();
        if (!pathl.isEmpty()) {
            String rootc = pathl.pop();               
            if (!dconfig.getCode().equals(rootc) || pathl.isEmpty()) {
                return null;
            }
        }
        DeviceNode node = dconfig.findChainChild(pathl);
        if (node != null) {
            node.setSelected(mode == 1);
            if (dinamicValues != null) {
                for (DinamicValue dv: dinamicValues) {
                    node.setDinamicValue(dv.getCode(), dv.getValue());
                }
            }
            device.setConfigMap(dconfig);
            return save(device);
        } else {
            return null;
        }
    }
    
    public ChangeLogEntity startSetDeviceConfig(String nodeId, List<String> path, List<DinamicValue> dinamicValues, int mode) {
        return null;
    }
    
    protected DeviceEntity wrap(Device device) {
        DeviceEntity entity = new DeviceEntity();
        entity.setConfigCode(device.getConfig().getCode());
        DeviceNode toWrap = device.getConfigMap() != null ? device.getConfigMap() : new DeviceNode(device.getConfig().getRoot());
        entity.setDeviceMap(JAXBUtil.marshal(toWrap, true));            
        entity.setId(device.getDeviceId());
        entity.setIpAddress(device.getIpAddress());
        entity.setMacAddress(device.getMacAddress());
        entity.setNodeId(device.getNodeId());
        entity.setConfigState(device.getConfigState());
        return entity;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean deleteDevice(String id) {
        DeviceEntity de = dao.load(id);
        if (de != null) {
            dao.delete(de);
            return true;
        } else {
            return false;
        }
    }
     
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ChangeLogEntity changeDeviceState(String userName, DeviceEntity device, DeviceState newState, ChangeLogEntity originalLog) {
        ChangeLogEntity log = null;
        if (originalLog != null) {
            log = originalLog;
        } else {
            log = new ChangeLogEntity();
            log.setStateBefore(device.getConfigState());
        }
        device.setConfigState(newState);
        saveEntity(device);        
        log.setUserName(userName);
        log.setStateAfter(newState);
        log.setDevice(device);
        log.setUpdateTime(new Date());
        ChangeLogEntity ret = logDao.save(log);
        logDao.flush();
        return ret;
    }
    
    @Override
    public ChangeLogEntity findLog(Long id) {
        return logDao.load(id);
    }
    
    @Override
    public List<ChangeLogEntity> findLogs(ChangeLogEntity filter, String sort, int start, int count) {
        return logDao.find(filter, sort, start, count);
    }
    
    @Override
    public int countLogs(ChangeLogEntity filter) {
        return logDao.count(filter);
    }
    
    protected Device unwrap(DeviceEntity entity) {
        Device device = new Device();
        device.setConfig(configService.findConfigByCode(entity.getConfigCode()));
        device.setConfigMap(JAXBUtil.unmarshal(entity.getDeviceMap(), DeviceNode.class));
        device.setIpAddress(entity.getIpAddress());
        device.setMacAddress(entity.getMacAddress());
        device.setDeviceId(entity.getId());
        device.setNodeId(entity.getNodeId());
        device.getConfigMap().setupParents();
        device.setConfigState(entity.getConfigState());
        return device;
    }
}
