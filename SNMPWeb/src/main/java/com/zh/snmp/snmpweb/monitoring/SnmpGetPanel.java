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
package com.zh.snmp.snmpweb.monitoring;

import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.snmp.SnmpManager;
import java.util.LinkedList;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class SnmpGetPanel extends MonitorPanel<DeviceEntity> {
    @SpringBean
    private SnmpManager snmpManager;
    
    //private IModel<DeviceType> selectedType;
    private DropDownChoice typeChoice;
    
    public SnmpGetPanel(String id, IModel<DeviceEntity> model) {        
        super(id, model);
        /*
        progressLabel.setVisible(false);
        selectedType = Model.of();
        add(typeChoice = new DropDownChoice("typeSelect", selectedType, new PropertyModel(this, "types"), new EnumChoiceRenderer())); 
        typeChoice.setOutputMarkupId(true);
        typeChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                appender = getAppender(getPanelModel());
                progressLabel.setVisible(true);
                typeChoice.setEnabled(false);
                snmpManager.processOnDevice(SnmpManager.ProcessType.GET, getPanelModel().getObject(), selectedType.getObject(), appender);
                target.addComponent(SnmpGetPanel.this);
            }
        });
         * 
         */
    }

    @Override
    protected void onEndProcess(AjaxRequestTarget target) {
        typeChoice.setEnabled(true);
        target.addComponent(typeChoice);        
    }    
}
