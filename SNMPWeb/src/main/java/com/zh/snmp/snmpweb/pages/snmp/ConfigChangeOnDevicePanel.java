package com.zh.snmp.snmpweb.pages.snmp;

import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.services.SnmpService;
import com.zh.snmp.snmpweb.components.ModalEditCloseListener;
import com.zh.snmp.snmpweb.components.ModalEditPanel;
import com.zh.snmp.snmpweb.model.DetachableDeviceConfigListModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class ConfigChangeOnDevicePanel extends ModalEditPanel<DeviceEntity> implements ModalEditCloseListener {
    @SpringBean
    private SnmpService service;
    private IModel<DeviceConfigEntity> selected;
    
    public ConfigChangeOnDevicePanel(ModalWindow modal, IModel<DeviceEntity> model) {
        super(modal, model, false);
        /*
        form.setDefaultModel(new CompoundPropertyModel<DeviceEntity>(model.getObject()));
        form.add(new Label("nodeId"));
        form.add(new Label("macAddress"));
        form.add(new Label("ipAddress"));
        
        
        DeviceConfigEntity actValue = model.getObject().findConfiguration(type);
        selected = Model.of(actValue);
        form.add(new EnumLabel("selectedType", type));
        
        DeviceConfigEntity filter = new DeviceConfigEntity();
        filter.setActive(Boolean.TRUE);
        filter.setDeviceType(type);
        DetachableDeviceConfigListModel configs = new DetachableDeviceConfigListModel(filter);
        
        form.add(new DropDownChoice<DeviceConfigEntity>("newConfig", selected, configs, DetachableDeviceConfigListModel.DEVICE_CONFIG_RENDERER)); 
         * 
         */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        selected.detach();
    }
    
    @Override
    protected boolean onModalSave(AjaxRequestTarget target) {
        return false;
        /*
        String selectedKod = selected.getObject() != null ? selected.getObject().getCode() : null;
        service.setDeviceConfig(getEntityObject().getNodeId(), selectedKod);
        getBasePage().refreshPanel(target);
        return true;
         * 
         */
    }

    @Override
    public void onModalClose(AjaxRequestTarget target, boolean save, boolean delete) {
        getBasePage().refreshPanel(target);
    }    
}
