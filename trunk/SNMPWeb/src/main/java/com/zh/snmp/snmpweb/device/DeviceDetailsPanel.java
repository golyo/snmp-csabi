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
package com.zh.snmp.snmpweb.device;

import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.domain.DeviceNode;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.message.SimpleMessageAppender;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import com.zh.snmp.snmpcore.services.impl.SnmpBackgroundProcess;
import com.zh.snmp.snmpweb.BaseSession;
import com.zh.snmp.snmpweb.model.DetachableDeviceModel;
import com.zh.snmp.snmpweb.monitoring.MonitorPopupPanel;
import com.zh.snmp.snmpweb.pages.BasePanel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class DeviceDetailsPanel extends BasePanel<Device> {

    @SpringBean
    private DeviceService service;
    
    @SpringBean
    private SnmpService snmpService;
    
    private static final String EMPTY_STR = "-";
    private boolean changed;
    private IModel<Device> deviceModel;

    public DeviceDetailsPanel(String id, final IModel<DeviceEntity> model) {
        super(id, null);
        setOutputMarkupId(true);
        deviceModel = new DetachableDeviceModel(model.getObject().getId());
        setDefaultModel(new CompoundPropertyModel<Device>(deviceModel)); 
        add(new Label("config.code"));
        add(new Label("deviceId"));
        add(new Label("nodeId"));
        add(new Label("macAddress"));
        add(new Label("ipAddress"));
        LinkTree tree;
        
        final TreeModel treeModel = new DefaultTreeModel(deviceModel.getObject().getConfigMap());
        add(tree = new LinkTree("tree", treeModel) {
            @Override
            protected IModel getNodeTextModel(IModel<Object> model) {
                return Model.of(((DeviceNode) model.getObject()).getCode());
            }

            @Override
            protected void onNodeLinkClicked(Object node, BaseTree tree, AjaxRequestTarget target) {
                final DeviceNode selnode = getNode(node);
                if (selnode.getParent() != null) {
                    DeviceStateEditPanel editPanel = new DeviceStateEditPanel(getModal(), Model.of(selnode)) {
                        @Override
                        protected void onStateChanged(AjaxRequestTarget target) {
                            changed = true;
                            target.addComponent(DeviceDetailsPanel.this);
                        }
                    };
                    editPanel.show(target);                    
                }
            }
            
            @Override
            protected String getItemClass(Object node) {
                return getNode(node).isSelected() ? "selected" : "unselected";
            }
        });
        tree.getTreeState().expandAll();
        add(new AjaxButton("delete") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                service.deleteDevice(deviceModel.getObject().getDeviceId());
                getJBetPage().backToParentPanel(target);
            }
        });
        add(new AjaxButton("finalize") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Object o = treeModel.getRoot();
                DeviceNode root = getNode(treeModel.getRoot());
                Device device = (Device)DeviceDetailsPanel.this.getDefaultModelObject();
                device.setConfigMap(root);
                service.save(device);
                changed = false;
                target.addComponent(DeviceDetailsPanel.this);
            }
            @Override
            public boolean isVisible() {
                return changed;
            }        
        });
        
        add(new AjaxButton("doSnmp") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                SnmpBackgroundProcess startSnmp = snmpService.startSnmpBackgroundProcess(BaseSession.get().getUser().getName(), deviceModel.getObject().getDeviceId(), new SimpleMessageAppender());
                MonitorPopupPanel popup = new MonitorPopupPanel(getModal(), null, startSnmp.getAppender(), "snmp.backgroundModalTitle");
                popup.show(target);
            }
            @Override
            public boolean isVisible() {
                return !changed;
            }        
        });
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        deviceModel.detach();
    }
    
    public DeviceNode getNode(Object node) {
        return (DeviceNode)node;        
    }
    
}
