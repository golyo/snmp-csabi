package com.zh.snmp.snmpweb.model;

import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.util.Iterator;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class DeviceProvider extends EntityDataProvider<DeviceEntity> {
    @SpringBean
    private DeviceService srv;
    
    public DeviceProvider() {
        super(new DeviceEntity());
        InjectorHolder.getInjector().inject(this);
    }
    
    @Override
    public Iterator<? extends DeviceEntity> iterator(int first, int count) {
        return srv.findDeviceEntityByFilter(getFilterState(), getSortParam(), first, count).iterator();
    }

    @Override
    public IModel<DeviceEntity> model(DeviceEntity object) {
        return new DetachableDeviceEntityModel(object);
    }

    @Override
    public int size() {
        return srv.countDevices(getFilterState());
    }
    
}
