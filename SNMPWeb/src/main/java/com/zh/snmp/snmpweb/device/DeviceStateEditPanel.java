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

import com.zh.snmp.snmpcore.domain.DeviceSelectionNode;
import com.zh.snmp.snmpweb.components.ModalEditPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author Golyo
 */
public abstract class DeviceStateEditPanel extends ModalEditPanel<String> {
    private boolean originalState;
    private boolean newState;
    
    public DeviceStateEditPanel(ModalWindow id, String code, boolean state) {
        super(id, null, false);
        originalState = state;
        newState = state;
        form.add(new Label("code", code));
        form.add(new CheckBox("selected", new PropertyModel<Boolean>(this, "newState")));
    }

    @Override
    protected boolean onModalSave(AjaxRequestTarget target) {
        if (originalState != newState) {
            onStateChanged(newState, target);
        }
        return true;
    }

    public boolean isNewState() {
        return newState;
    }

    public void setNewState(boolean newState) {
        this.newState = newState;
    }

    protected abstract void onStateChanged(boolean state, AjaxRequestTarget target);
}
