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
import com.zh.snmp.snmpweb.components.DataTablePanel;
import com.zh.snmp.snmpweb.components.RowLinkColumn;
import com.zh.snmp.snmpweb.menu.MenuConfig;
import com.zh.snmp.snmpweb.model.DetachableDeviceConfigListModel;
import com.zh.snmp.snmpweb.model.DeviceProvider;
import java.io.Serializable;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

/**
 *
 * @author Golyo
 */
@MenuConfig(context={DeviceDetailsPanel.class, HistoryListPanel.class})
public class DeviceListPanel extends DataTablePanel<DeviceEntity> {
    
    public DeviceListPanel(String id) {
        super(id, new DeviceProvider(), 20);
        add(new AjaxLink("newEntity") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                showEditPanel(target, Model.of(new DeviceEntity()));
            }
        });
    }

    @Override
    protected IColumn<DeviceEntity>[] createTableColumns() {
        return new IColumn[] {
            new TextFilteredPropertyColumn<DeviceEntity, String>(new ResourceModel("device.nodeId"), "nodeId"),
            new TextFilteredPropertyColumn<DeviceEntity, String>(new ResourceModel("device.macAddress"), "macAddress"),
            new TextFilteredPropertyColumn<DeviceEntity, String>(new ResourceModel("device.ipAddress"), "ipAddress"),
            new ChoiceFilteredPropertyColumn<DeviceEntity, DeviceConfigEntity>(new ResourceModel("device.config"), "config", "config", new DetachableDeviceConfigListModel()) {
                @Override
                protected IChoiceRenderer<DeviceConfigEntity> getChoiceRenderer() {
                    return DetachableDeviceConfigListModel.DEVICE_CONFIG_RENDERER;
                }                
                
                @Override
                protected IModel<?> createLabelModel(IModel<DeviceEntity> rowModel) {
                    Serializable o = (Serializable)DetachableDeviceConfigListModel.DEVICE_CONFIG_RENDERER.getDisplayValue(rowModel.getObject().getConfig());
                    return Model.of(o);
                }
            },
            new RowLinkColumn<DeviceEntity>(new ResourceModel("title.options"), new ResourceModel("link.details"), null) {
                @Override
                protected void onRowSelect(AjaxRequestTarget target, IModel<DeviceEntity> rowModel) {
                    getJBetPage().changePanel(getDetailMenuConfig(), rowModel, DeviceListPanel.this.getClass(), target);
               }              
            }
        };
    }
    
    private void showEditPanel(AjaxRequestTarget target, IModel<DeviceEntity> model) {
        DeviceEditPanel panel = new DeviceEditPanel(getJBetPage().getModal(), model);
        panel.show(target);        
    }
}