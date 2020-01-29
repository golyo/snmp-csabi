package com.zh.snmp.snmpweb.pages.snmp;

import com.zh.snmp.snmpcore.entities.ChangeLogEntity;
import com.zh.snmp.snmpcore.services.SnmpService;
import com.zh.snmp.snmpweb.components.DataTablePanel;
import com.zh.snmp.snmpweb.components.DatePropertyColumn;
import com.zh.snmp.snmpweb.components.EnumPropertyColumn;
import com.zh.snmp.snmpweb.components.RightGoAndClearFilter;
import com.zh.snmp.snmpweb.menu.MenuConfig;
import com.zh.snmp.snmpweb.model.HistoryProvider;
import com.zh.snmp.snmpweb.monitoring.TrapMonitorPanel;
import com.zh.snmp.snmpweb.monitoring.UpdateMonitorPanel;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilteredColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

/**
 *
 * @author Golyo
 */
@MenuConfig(context={BackgroundHistoryListPanel.class, TrapMonitorPanel.class, UpdateMonitorPanel.class})
public class BackgroundHistoryListPanel extends DataTablePanel<ChangeLogEntity> {

    private static final List<String> BACKGROUND_USERSS = Arrays.asList(SnmpService.TRAP_USERNAME, SnmpService.WEBSERVICE_USERNAME, SnmpService.AUTO_UPDATE_USERNAME);
    
    public BackgroundHistoryListPanel(String id) {
        super(id, new HistoryProvider(), 20);
        HistoryProvider provider = (HistoryProvider)getDataProvider();
        provider.getFilterState().setUserName(SnmpService.WEBSERVICE_USERNAME);
    }

    @Override
    protected IColumn<ChangeLogEntity>[] createTableColumns() {
        return new IColumn[] {
            new DatePropertyColumn<ChangeLogEntity>(new ResourceModel("changeLog.updateTime"), "updateTime", "updateTime", true),
            new ChoiceFilteredPropertyColumn(new ResourceModel("changeLog.userName"), "userName", "userName", Model.ofList(BACKGROUND_USERSS)),
            new TextFilteredPropertyColumn<ChangeLogEntity, String>(new ResourceModel("device.deviceId"), "device.deviceId", "device.deviceId"),
            new EnumPropertyColumn<ChangeLogEntity>(new ResourceModel("changeLog.stateBefore"), "stateBefore"),
            new GoClearFilteredEnumColumn<ChangeLogEntity>(new ResourceModel("changeLog.stateAfter"), "stateAfter") {
                
            }
        };
    }
    
    public static class GoClearFilteredEnumColumn<T> extends EnumPropertyColumn<T> implements IFilteredColumn<T> {
        public GoClearFilteredEnumColumn(IModel<String> resourceModel, String property) {
            super(resourceModel, property);
        }

        @Override
        public Component getFilter(String componentId, FilterForm form) {
            return new RightGoAndClearFilter(componentId, form);
        }
        
        
    }
}