package com.zh.snmp.snmpweb.model;

import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.util.List;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class DetachableDeviceConfigListModel extends LoadableDetachableModel<List<? extends DeviceConfigEntity>> {
    public static final IChoiceRenderer<DeviceConfigEntity> DEVICE_CONFIG_RENDERER = new IChoiceRenderer<DeviceConfigEntity>() {

        @Override
        public Object getDisplayValue(DeviceConfigEntity object) {
            return object != null ? object.getId() + " - " + object.getName() : " - ";
        }

        @Override
        public String getIdValue(DeviceConfigEntity object, int index) {
            return object != null ? object.getId() : null;
        }
    };
    
    @SpringBean
    private ConfigService service;
    private DeviceConfigEntity filter;
    
    public DetachableDeviceConfigListModel() {
        this(new DeviceConfigEntity());
    }
    
    public DetachableDeviceConfigListModel(DeviceConfigEntity filter) {
        this.filter = filter;
        InjectorHolder.getInjector().inject(this);
    }
    
    @Override
    protected List<DeviceConfigEntity> load() {
        return service.findDeviceConfigByFilter(filter, null, 0, -1);
    }
}
