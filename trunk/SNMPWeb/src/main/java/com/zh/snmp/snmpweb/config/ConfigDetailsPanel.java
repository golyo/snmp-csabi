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
