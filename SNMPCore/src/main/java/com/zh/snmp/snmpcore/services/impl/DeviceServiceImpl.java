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
import com.zh.snmp.snmpcore.domain.ConfigNode;
import com.zh.snmp.snmpcore.domain.Configuration;
import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.domain.DeviceMap;
import com.zh.snmp.snmpcore.domain.DeviceSelectionNode;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.DeviceState;
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
    public Device findDeviceByNodeId(String nodeId) {        
        DeviceEntity entity =dao.load(nodeId);
        return entity != null ? unwrap(entity) : null;
    }

    @Override
    public Device findDeviceByIp(String ip) {
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
                DeviceMap dm = new DeviceMap();
                dm.setCode(config.getCode());
                device.setDeviceMap(JAXBUtil.marshal(dm, true));                            
                return dao.save(device);
            }
        }
        return null;
    }
        
    @Override
    public DeviceEntity findDeviceEntityByFilter(DeviceEntity filter) {
        return dao.findExampleEntity(filter);
    }    
    
    private static final String PATH_DELIM = ".";
    @Override
    public boolean setDeviceConfig(String nodeId, List<String> path, int mode) {
        Device device = findDeviceByNodeId(nodeId);
        ConfigNode act = device.getConfig().getRoot().findChildByPath(new LinkedList<String>(path));
        if (act == null) {
            return false;            
        } else {
            LinkedList<String> lpath = new LinkedList<String>(path);
            lpath.pop();
            if (!lpath.isEmpty()) {
                device.getConfigMap().setByPath(lpath);
            }
            save(device);
            return true;
        }
        
    }
    
    @Override
    public DeviceSelectionNode createSelectionNode(Device device) {
        DeviceSelectionNode selectionNode = new DeviceSelectionNode();
        appendSelectionNode(selectionNode, device.getConfigMap(), device.getConfig().getRoot());
        selectionNode.setupParents();
        return selectionNode;
    }
       
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
    protected DeviceEntity wrap(Device device) {
        DeviceEntity entity = new DeviceEntity();
        entity.setConfigCode(device.getConfig().getCode());
        DeviceMap toWrap = device.getConfigMap() != null ? device.getConfigMap() : new DeviceMap();
        entity.setDeviceMap(JAXBUtil.marshal(toWrap, true));            
        entity.setId(device.getDeviceId());
        entity.setIpAddress(device.getIpAddress());
        entity.setMacAddress(device.getMacAddress());
        entity.setNodeId(device.getNodeId());
        entity.setConfigState(device.getConfigState());
        return entity;
    }
    
    protected Device unwrap(DeviceEntity entity) {
        Device device = new Device();
        device.setConfig(configService.findConfigByCode(entity.getConfigCode()));
        device.setConfigMap(JAXBUtil.unmarshal(entity.getDeviceMap(), DeviceMap.class));
        device.setIpAddress(entity.getIpAddress());
        device.setMacAddress(entity.getMacAddress());
        device.setDeviceId(entity.getId());
        device.setNodeId(entity.getNodeId());
        device.getConfigMap().setupParents();
        device.setConfigState(entity.getConfigState());
        return device;
    }
}
