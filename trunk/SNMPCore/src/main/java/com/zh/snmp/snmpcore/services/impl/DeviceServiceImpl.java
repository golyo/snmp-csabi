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
import com.zh.snmp.snmpcore.domain.Configuration;
import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.domain.DeviceSelectionNode;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.DeviceState;
import com.zh.snmp.snmpcore.exception.ExceptionCodesEnum;
import com.zh.snmp.snmpcore.exception.SystemException;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.util.JAXBUtil;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private ConfigService configService;
    
    @Autowired
    private DeviceDao dao;
    
    @Override
    public Device findDeviceByDeviceId(String nodeId) {      
        if (nodeId == null) {
            return null;
        }
        DeviceEntity entity =dao.load(nodeId);
        return entity != null ? unwrap(entity) : null;
    }

    @Override
    public Device findDeviceByIp(String ip) {
        if (ip == null) {
            return null;
        }
        DeviceEntity filter = new DeviceEntity();
        filter.setIpAddress(ip);
        DeviceEntity entity = dao.findExampleEntity(filter);
        return entity != null ? unwrap(entity) : null;
    }

    @Override
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
    public DeviceEntity saveEntity(DeviceEntity device) {
        if (device.getDeviceMap() == null) {
            Configuration config = configService.findConfigByCode(device.getConfigCode());
            if (config != null) {
                DeviceSelectionNode dm = new DeviceSelectionNode(config.getRoot());
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
        return ret;
    }
        
    @Override
    public DeviceEntity findDeviceEntityByFilter(DeviceEntity filter) {
        return dao.findExampleEntity(filter);
    }    
    
    private static final String PATH_DELIM = ".";
    
    @Override
    public Device setDeviceConfig(String nodeId, List<String> path, int mode) {
        Device device = findDeviceByDeviceId(nodeId);
        if (device == null) {
            return null;
        }
        LinkedList<String> pathl = new LinkedList<String>(path);
        DeviceSelectionNode dconfig = device.getConfigMap();
        if (!pathl.isEmpty()) {
            String rootc = pathl.pop();               
            if (!dconfig.getCode().equals(rootc) || pathl.isEmpty()) {
                return null;
            }
        }
        DeviceSelectionNode node = dconfig.findChainChild(pathl);
        if (node != null) {
            node.setSelected(mode == 1);
            device.setConfigMap(dconfig);
            return save(device);
        } else {
            return null;
        }
    }
    
    /*
    protected void appendSelectionNode(DeviceSelectionNode selection, DeviceMap map, ConfigNode config) {
        selection.setCode(config.getCode());
        if (map != null) {
            selection.setSelected(true);
        }
        List<DeviceSelectionNode> selChilds = new LinkedList<DeviceSelectionNode>();
        selection.setChildren(selChilds);
        for (ConfigNode child: config.getChildren()) {
            DeviceMap mchild = map != null ? map.findChild(child.getCode()) : null;            
            DeviceSelectionNode selChild = new DeviceSelectionNode();
            selChilds.add(selChild);
            appendSelectionNode(selChild, mchild, child);
        }
    }
     * 
     */
    protected DeviceEntity wrap(Device device) {
        DeviceEntity entity = new DeviceEntity();
        entity.setConfigCode(device.getConfig().getCode());
        DeviceSelectionNode toWrap = device.getConfigMap() != null ? device.getConfigMap() : new DeviceSelectionNode(device.getConfig().getRoot());
        entity.setDeviceMap(JAXBUtil.marshal(toWrap, true));            
        entity.setId(device.getDeviceId());
        entity.setIpAddress(device.getIpAddress());
        entity.setMacAddress(device.getMacAddress());
        entity.setNodeId(device.getNodeId());
        entity.setConfigState(device.getConfigState());
        return entity;
    }
    
    @Override
    public boolean deleteDevice(String id) {
        DeviceEntity de = dao.load(id);
        if (de != null) {
            dao.delete(dao.load(id));
            return true;
        } else {
            return false;
        }
    }
    
    protected Device unwrap(DeviceEntity entity) {
        Device device = new Device();
        device.setConfig(configService.findConfigByCode(entity.getConfigCode()));
        device.setConfigMap(JAXBUtil.unmarshal(entity.getDeviceMap(), DeviceSelectionNode.class));
        device.setIpAddress(entity.getIpAddress());
        device.setMacAddress(entity.getMacAddress());
        device.setDeviceId(entity.getId());
        device.setNodeId(entity.getNodeId());
        device.getConfigMap().setupParents();
        device.setConfigState(entity.getConfigState());
        return device;
    }
}
