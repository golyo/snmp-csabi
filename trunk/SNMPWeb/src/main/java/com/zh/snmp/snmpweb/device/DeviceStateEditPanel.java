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
package com.zh.snmp.snmpweb.device;

import com.zh.snmp.snmpcore.domain.DeviceNode;
import com.zh.snmp.snmpcore.domain.DinamicValue;
import com.zh.snmp.snmpweb.components.ModalEditPanel;
import javax.security.auth.callback.TextInputCallback;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

/**
 *
 * @author Golyo
 */
public abstract class DeviceStateEditPanel extends ModalEditPanel<DeviceNode> {
    
    public DeviceStateEditPanel(ModalWindow id, IModel<DeviceNode> node) {
        super(id, node, false);
        form.add(new Label("code", new PropertyModel(node, "code")));
        form.add(new CheckBox("selected", new PropertyModel<Boolean>(node, "selected")));
        form.add(new ListView<DinamicValue>("dinamics", new PropertyModel(node, "dinamics")) {

            @Override
            protected void populateItem(ListItem<DinamicValue> item) {
                item.add(new Label("code", new PropertyModel(item.getModelObject(), "code")));
                item.add(new TextField("value", new PropertyModel(item.getModelObject(), "value")));
            }
        });
    }

    @Override
    protected boolean onModalSave(AjaxRequestTarget target) {
        DeviceNode node = (DeviceNode)getDefaultModelObject();
        boolean hasErr = false;
        if (node.isSelected()) {
            for (DinamicValue val: node.getDinamics()) {
                if (val.getValue() == null) {
                    hasErr = true;
                    error(new StringResourceModel("error.dinamicValue", this, null, new Object[] {val.getCode()}).getString());
                    //error(getString("error.dinamicValue", Model.of(val)));
                }
            }
        }        
        if (hasErr) {
            return false;
        } else {
            onStateChanged(target);
            return true;
        }
    }

    protected abstract void onStateChanged(AjaxRequestTarget target);
}
