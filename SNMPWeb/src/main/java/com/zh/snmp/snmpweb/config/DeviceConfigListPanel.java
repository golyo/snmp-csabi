package com.zh.snmp.snmpweb.config;

import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpweb.components.DataTablePanel;
import com.zh.snmp.snmpweb.components.RowLinkColumn;
import com.zh.snmp.snmpweb.menu.MenuConfig;
import com.zh.snmp.snmpweb.model.DeviceConfigProvider;
import com.zh.snmp.snmpweb.pages.snmp.DeviceConfigImportPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

/**
 *
 * @author Golyo
 */
@MenuConfig(context={ConfigDetailsPanel.class})
public class DeviceConfigListPanel extends DataTablePanel<DeviceConfigEntity> {
    
    public DeviceConfigListPanel(String id) {
        super(id, new DeviceConfigProvider(), 20);
        add(new AjaxLink("newEntity") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                showEditPanel(target, Model.of(new DeviceConfigEntity()));
            }
        });
    }

    @Override
    protected IColumn<DeviceConfigEntity>[] createTableColumns() {
        return new IColumn[] {
            new TextFilteredPropertyColumn(new ResourceModel("deviceConfig.code"), "code"),
            new PropertyColumn(new ResourceModel("deviceConfig.name"), "name"),
            /*
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
            },*/
            
            new RowLinkColumn<DeviceConfigEntity>(new ResourceModel("title.options"), new ResourceModel("link.details"), null) {
                @Override
                protected void onRowSelect(AjaxRequestTarget target, IModel<DeviceConfigEntity> rowModel) {
                    getJBetPage().changePanel(getDetailMenuConfig(), rowModel, DeviceConfigListPanel.this.getClass(), target);
               }              
            }
        };
    }
    
    private void showEditPanel(AjaxRequestTarget target, IModel<DeviceConfigEntity> model) {
        DeviceConfigImportPanel panel = new DeviceConfigImportPanel(getJBetPage().getModal());
        //DeviceConfigEditPanel panel = new DeviceConfigEditPanel(getJBetPage().getModal(), model);
        panel.show(target);        
    }
}
