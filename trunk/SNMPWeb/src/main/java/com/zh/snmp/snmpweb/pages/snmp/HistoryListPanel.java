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
import com.zh.snmp.snmpcore.entities.ChangeLogEntity;
import com.zh.snmp.snmpweb.components.DataTablePanel;
import com.zh.snmp.snmpweb.components.DatePropertyColumn;
import com.zh.snmp.snmpweb.components.EnumPropertyColumn;
import com.zh.snmp.snmpweb.model.DetachableDeviceConfigListModel;
import com.zh.snmp.snmpweb.model.HistoryProvider;
import java.io.Serializable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

/**
 *
 * @author Golyo
 */
public class HistoryListPanel extends DataTablePanel<ChangeLogEntity> {
    
    public HistoryListPanel(String id, IModel<DeviceEntity> deviceModel) {
        super(id, new HistoryProvider(), 20);
        HistoryProvider provider = (HistoryProvider)getDataTable().getDataProvider();
        provider.getFilterState().setDevice(deviceModel.getObject());
        add(new Label("nodeId", new PropertyModel<ChangeLogEntity>(deviceModel, "nodeId")));
    }

    @Override
    protected IColumn<ChangeLogEntity>[] createTableColumns() {
        return new IColumn[] {
            new DatePropertyColumn<ChangeLogEntity>(new ResourceModel("changeLog.updateTime"), "updateTime", "updateTime", true),
            new TextFilteredPropertyColumn<ChangeLogEntity, String>(new ResourceModel("changeLog.userName"), "userName", "userName"),
            //new PropertyColumn<ChangeLogEntity>(new ResourceModel("device.nodeId"), "device.id"),
            new EnumPropertyColumn<ChangeLogEntity>(new ResourceModel("changeLog.stateBefore"), "stateBefore"),
            new EnumPropertyColumn<ChangeLogEntity>(new ResourceModel("changeLog.stateAfter"), "stateAfter")
        };
    }
    
}