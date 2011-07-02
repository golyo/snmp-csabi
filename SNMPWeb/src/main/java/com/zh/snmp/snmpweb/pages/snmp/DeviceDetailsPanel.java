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
import com.zh.snmp.snmpcore.entities.DeviceType;
import com.zh.snmp.snmpweb.pages.BasePanel;
import java.util.Arrays;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Golyo
 */
public class DeviceDetailsPanel extends BasePanel<DeviceEntity> {

    private static final String EMPTY_STR = "-";

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
        ListView<DeviceType> configList = new ListView<DeviceType>("configurations", Arrays.asList(DeviceType.values())) {

            @Override
            protected void populateItem(final ListItem<DeviceType> item) {
                item.add(new EnumLabel("type", item.getModelObject()));
                DeviceConfigEntity config = getPanelModel().getObject().findConfiguration(item.getModelObject());
                item.add(new Label("code", config != null ? config.getCode() : EMPTY_STR));
                item.add(new Label("name", config != null ? config.getName() : EMPTY_STR));
                item.add(new AjaxLink("setConfig") {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        ConfigChangeOnDevicePanel panel = new ConfigChangeOnDevicePanel(getJBetPage().getModal(), model, item.getModelObject());
                        panel.show(target);
                    }
                });

            }
        };
        add(configList);
    }
}
