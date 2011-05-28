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

import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpweb.pages.BasePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Golyo
 */
public class DeviceDetailsPanel extends BasePanel<DeviceEntity> {
    public DeviceDetailsPanel(String id, final IModel<DeviceEntity> model) {
        super(id, new CompoundPropertyModel<DeviceEntity>(model));
        add(new AjaxLink("editLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                DeviceEditPanel panel = new DeviceEditPanel(getJBetPage().getModal(), model);
                panel.show(target);        
            }
        });
        add(new Label("nodeId"));
        add(new Label("macAddress"));
        add(new Label("ipAddress"));
        add(new Label("config.code"));
        add(new Label("config.name"));
    }
}
