package com.zh.snmp.snmpweb.config;

import com.zh.snmp.snmpcore.domain.ConfigNode;
import com.zh.snmp.snmpcore.domain.Configuration;
import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpweb.pages.BasePanel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class ConfigDetailsPanel extends BasePanel<DeviceConfigEntity> {
    @SpringBean
    private ConfigService service;
    
    public ConfigDetailsPanel(String id, IModel<DeviceConfigEntity> model) {
        super(id, model);
        //add(new LinkTree(id, ))
        Configuration conf = service.findConfigByCode(model.getObject().getId());
        TreeModel treeModel = new DefaultTreeModel(conf.getRoot());
        final WebMarkupContainer cont = new WebMarkupContainer("cont");
        cont.setOutputMarkupId(true);
        add(cont);
        LinkTree tree;
        add(tree = new LinkTree("tree", treeModel) {

            @Override
            protected void onNodeLinkClicked(Object node, BaseTree tree, AjaxRequestTarget target) {
                cont.replace(new ConfigNodePanel("nodePanel", (ConfigNode)node));
                target.addComponent(cont);
            }            
        });  
        tree.getTreeState().expandAll();
        cont.add(new Label("nodePanel"));
        
    }
}
