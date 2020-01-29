package com.zh.snmp.snmpweb.model;

import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.services.DeviceService;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class DetachableDeviceModel extends LoadableDetachableModel<Device> {
    @SpringBean
    private DeviceService service;
    
    private String id;
    
    public DetachableDeviceModel(String id) {
        super();
        InjectorHolder.getInjector().inject(this);
        this.id = id;
    }
    
    public DetachableDeviceModel(Device object) {
        super(object);
        InjectorHolder.getInjector().inject(this);
        this.id = object.getDeviceId();
    }
    
    @Override
    protected Device load() {
        return service.findDeviceByDeviceId(id);
    }
    
}
