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
package com.zh.snmp.snmpweb.pages.snmp;

import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.services.SnmpService;
import com.zh.snmp.snmpweb.components.ModalEditCloseListener;
import com.zh.snmp.snmpweb.components.ModalEditPanel;
import com.zh.snmp.snmpweb.model.DetachableDeviceConfigListModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class DeviceEditPanel extends ModalEditPanel<DeviceEntity> implements ModalEditCloseListener {
    @SpringBean
    private SnmpService service;
    
    public DeviceEditPanel(ModalWindow modal, IModel<DeviceEntity> model) {
        super(modal, model, false);
        boolean isEdit = model.getObject().getId() != null;
        form.setDefaultModel(new CompoundPropertyModel<DeviceEntity>(model.getObject()));
        form.add(new TextField("nodeId").setRequired(true).setEnabled(!isEdit));
        form.add(new TextField("macAddress").setRequired(true));
        form.add(new TextField("ipAddress").setRequired(true));
        DeviceConfigEntity filter = new DeviceConfigEntity();
        filter.setActive(Boolean.TRUE);
        form.add(new DropDownChoice<DeviceConfigEntity>("config", null, new DetachableDeviceConfigListModel(filter), DetachableDeviceConfigListModel.DEVICE_CONFIG_RENDERER));
    }

    @Override
    protected boolean onModalSave(AjaxRequestTarget target) {
        String errKey = null;
        DeviceEntity saveable = (DeviceEntity)form.getDefaultModelObject();
        DeviceEntity checkCode = service.findDeviceByNodeId(saveable.getNodeId());
        if (checkCode != null && !checkCode.getId().equals(saveable.getId())) {
            errKey = "deviceEntity.error.nodeIdExists";
            error(getString(errKey));
            target.addComponent(feedback);
            return false;
        } else {
            service.saveDevice(saveable);
            return true;
        }
    }

    @Override
    public void onModalClose(AjaxRequestTarget target, boolean save, boolean delete) {
        getBasePage().refreshPanel(target);
    }
    
}
