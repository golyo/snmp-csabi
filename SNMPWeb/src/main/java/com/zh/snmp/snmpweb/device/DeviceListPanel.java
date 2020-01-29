package com.zh.snmp.snmpweb.device;

import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpweb.components.DataTablePanel;
import com.zh.snmp.snmpweb.components.EnumPropertyColumn;
import com.zh.snmp.snmpweb.components.RowLinkColumn;
import com.zh.snmp.snmpweb.menu.MenuConfig;
import com.zh.snmp.snmpweb.model.DeviceProvider;
import com.zh.snmp.snmpweb.pages.snmp.HistoryListPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
@MenuConfig(context={DeviceDetailsPanel.class, HistoryListPanel.class})
public class DeviceListPanel extends DataTablePanel<DeviceEntity> {
    @SpringBean
    private ConfigService configService;
    
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
            
            new TextFilteredPropertyColumn<DeviceEntity, String>(new ResourceModel("device.deviceId"), "id"),
            new TextFilteredPropertyColumn<DeviceEntity, String>(new ResourceModel("device.nodeId"), "nodeId"),
            new TextFilteredPropertyColumn<DeviceEntity, String>(new ResourceModel("device.macAddress"), "macAddress"),
            new TextFilteredPropertyColumn<DeviceEntity, String>(new ResourceModel("device.ipAddress"), "ipAddress"),
            new EnumPropertyColumn(new ResourceModel("device.configState"), "configState"),
            new ChoiceFilteredPropertyColumn<DeviceEntity, String>(new ResourceModel("device.config"), "configCode", "configCode", Model.ofList(configService.getConfigCodes())),
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