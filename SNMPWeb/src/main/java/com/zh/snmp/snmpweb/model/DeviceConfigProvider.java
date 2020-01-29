package com.zh.snmp.snmpweb.model;

import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.util.Iterator;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class DeviceConfigProvider extends EntityDataProvider<DeviceConfigEntity> {
    @SpringBean
    private ConfigService srv;
    
    public DeviceConfigProvider() {
        super(new DeviceConfigEntity());
        InjectorHolder.getInjector().inject(this);
    }
    
    @Override
    public Iterator<? extends DeviceConfigEntity> iterator(int first, int count) {
        return srv.findDeviceConfigByFilter(getFilterState(), getSortParam(), first, count).iterator();
    }

    @Override
    public IModel<DeviceConfigEntity> model(DeviceConfigEntity object) {
        return new DetachableDeviceConfigModel(object);
    }

    @Override
    public int size() {
        return srv.countConfigEntity(getFilterState());
    }
    
}
