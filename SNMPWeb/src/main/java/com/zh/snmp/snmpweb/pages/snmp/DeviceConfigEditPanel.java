package com.zh.snmp.snmpweb.pages.snmp;

import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpweb.components.ModalEditCloseListener;
import com.zh.snmp.snmpweb.components.ModalEditPanel;
import java.io.IOException;
import java.io.InputStream;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.IOUtils;

/**
 *
 * @author Golyo
 */
public class DeviceConfigEditPanel extends ModalEditPanel<DeviceConfigEntity> implements ModalEditCloseListener {
    @SpringBean
    private ConfigService service;
    private FileUploadField file;
    
    public DeviceConfigEditPanel(ModalWindow modal, IModel<DeviceConfigEntity> model) {
        super(modal, model, false);
        boolean isEdit = model.getObject().getId() != null;
        form.setDefaultModel(new CompoundPropertyModel<DeviceConfigEntity>(model.getObject()));
        form.add(new TextField("code").setRequired(true).setEnabled(!isEdit));
        form.add(new TextField("name").setRequired(true));
        form.add(file = new FileUploadField("uploadFile", new Model<FileUpload>()));
        //form.add(ne)
                
        file.setRequired(!isEdit).setVisible(!isEdit);        
    }

    @Override
    protected boolean onModalSave(AjaxRequestTarget target) {
        String errKey = null;
        DeviceConfigEntity saveable = (DeviceConfigEntity)form.getDefaultModelObject();
        DeviceConfigEntity checkCode = service.findConfigEntityByCode(saveable.getId());
        if (checkCode != null && !checkCode.getId().equals(saveable.getId())) {
            errKey = "deviceConfigEntity.error.codeExists";
        } else if (saveable.getId() == null) {
            FileUpload upload = file.getFileUpload();
            InputStream is = null;
            try {
                is = upload.getInputStream();
                saveable.setSnmpDescriptor(IOUtils.toString(is));            
            } catch (IOException e) {
                IOUtils.closeQuietly(is);
                errKey = "fileUpload.exception";
            }            
        }
        if (errKey != null) {
            error(getString(errKey));
            target.addComponent(feedback);
            return false;
        } else {
            service.saveEntity(saveable);
            return true;
        }
    }
    
    @Override
    public void onModalClose(AjaxRequestTarget target, boolean save, boolean delete) {
        getBasePage().refreshPanel(target);
    }
}
