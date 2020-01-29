package com.zh.snmp.snmpweb.config;

import com.zh.snmp.snmpcore.domain.ConfigNode;
import com.zh.snmp.snmpcore.domain.OidCommand;
import com.zh.snmp.snmpcore.domain.SnmpCommand;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author Golyo
 */
public class ConfigNodePanel extends Panel {
    
    
    public ConfigNodePanel(String id, ConfigNode node) {
        super(id);        
        add(new Label("code", node.getCode()));
        add(new Label("description", node.getDescription()));
        add(new ListView<SnmpCommand>("command", node.getCommands()) {
            @Override
            protected void populateItem(ListItem<SnmpCommand> item) {
                SnmpCommand command = item.getModelObject();
                item.add(new Label("cmdprior", Integer.toString(command.getPriority())));
                item.add(new ListView<OidCommand>("oids", command.getCommands()) {
                    @Override
                    protected void populateItem(ListItem<OidCommand> item) {
                        OidCommand cmd = item.getModelObject();
                        item.add(new Label("name", cmd.getName()));
                        item.add(new Label("oid", cmd.getOid()));
                        item.add(new Label("value", cmd.getValue()));
                    }
                });
            }
        });
    }
}
