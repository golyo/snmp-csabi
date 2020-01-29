package com.zh.snmp.snmpweb.model;

import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class DetachableDeviceEntityModel extends LoadableDetachableModel<DeviceEntity> {
    @SpringBean
    private DeviceService service;
    private String id;
    
    public DetachableDeviceEntityModel(String id) {
        super();
        InjectorHolder.getInjector().inject(this);
        this.id = id;
    }
    
    public DetachableDeviceEntityModel(DeviceEntity object) {
        super(object);
        InjectorHolder.getInjector().inject(this);
        this.id = object.getId();
    }
    
    @Override
    protected DeviceEntity load() {
        return service.findDeviceEntityById(id);
    }
    
}
