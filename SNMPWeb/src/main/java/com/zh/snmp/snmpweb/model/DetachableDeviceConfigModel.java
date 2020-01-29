package com.zh.snmp.snmpweb.model;

import com.zh.snmp.snmpcore.domain.Configuration;
import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class DetachableDeviceConfigModel extends LoadableDetachableModel<DeviceConfigEntity> {
    @SpringBean
    private ConfigService service;
    private String id;
    
    public DetachableDeviceConfigModel(String id) {
        super();
        InjectorHolder.getInjector().inject(this);
        this.id = id;
    }
    
    public DetachableDeviceConfigModel(DeviceConfigEntity object) {
        super(object);
        InjectorHolder.getInjector().inject(this);
        this.id = object.getId();
    }
    
    @Override
    protected DeviceConfigEntity load() {
        return service.findConfigEntityByCode(id);
    }
    
}
