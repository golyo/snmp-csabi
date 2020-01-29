package com.zh.snmp.snmpweb.model;

import com.zh.snmp.snmpcore.entities.ChangeLogEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.util.Collections;
import java.util.Iterator;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class HistoryProvider extends EntityDataProvider<ChangeLogEntity> {
    @SpringBean
    private DeviceService srv;
    
    public HistoryProvider() {
        super(new ChangeLogEntity());
        getFilterState().setDevice(new DeviceEntity());
        InjectorHolder.getInjector().inject(this);
    }
    
    @Override
    public Iterator<? extends ChangeLogEntity> iterator(int first, int count) {
        return srv.findLogs(getFilterState(), getSortParam(), first, count).iterator();
    }

    @Override
    public IModel<ChangeLogEntity> model(ChangeLogEntity object) {
        return Model.of(object);
    }

    @Override
    public int size() {
        return srv.countLogs(getFilterState());
    }
    
}

