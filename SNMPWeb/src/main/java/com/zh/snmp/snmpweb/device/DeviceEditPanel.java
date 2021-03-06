package com.zh.snmp.snmpweb.device;

import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpweb.components.ModalEditCloseListener;
import com.zh.snmp.snmpweb.components.ModalEditPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class DeviceEditPanel extends ModalEditPanel<DeviceEntity> implements ModalEditCloseListener {
    @SpringBean
    private DeviceService service;
    @SpringBean
    private ConfigService confService;
    
    public DeviceEditPanel(ModalWindow modal, IModel<DeviceEntity> model) {
        super(modal, model, false);
        boolean isEdit = model.getObject().getId() != null;
        DeviceEntity de;
        form.add(new TextField("id").setRequired(true).setEnabled(!isEdit));
        form.add(new TextField("macAddress").setRequired(true));
        form.add(new TextField("ipAddress").setRequired(true));
        form.add(new TextField("nodeId").setRequired(true));
        form.add(new DropDownChoice("configCode", confService.getConfigCodes()).setRequired(true));
    }

    @Override
    protected boolean onModalSave(AjaxRequestTarget target) {
        String errKey = null;
        DeviceEntity saveable = (DeviceEntity)form.getDefaultModelObject();
        String checkErrKey = service.saveEntity(saveable);
        if (checkErrKey != null) {
            error(getString(checkErrKey, null, checkErrKey));
            target.addComponent(feedback);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onModalClose(AjaxRequestTarget target, boolean save, boolean delete) {
        getBasePage().refreshPanel(target);
    }    
}
