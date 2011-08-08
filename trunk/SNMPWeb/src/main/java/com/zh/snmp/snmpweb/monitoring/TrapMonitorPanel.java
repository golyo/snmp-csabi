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

import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.snmp.trap.TrapManager;
import java.io.IOException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Golyo
 */
public class TrapMonitorPanel extends MonitorPanel<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrapMonitorPanel.class);

    @SpringBean
    private TrapManager trapManager;
    
    public TrapMonitorPanel(String id) {
        super(id, Model.of(""));
        add(new AjaxLink("start") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                try {
                    trapManager.start();
                    getJBetPage().changePanel(TrapMonitorPanel.class, null, target);
                } catch (IOException e) {
                    LOGGER.error("Error while start trap", e);
                    getFeedback().error(getString("error.startTrap"));
                    target.addComponent(getFeedback());
                }
            }
            
            @Override
            public boolean isVisible() {
                return !trapManager.isRunning();
            }
        });
        add(new AjaxLink("stop") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                try {
                    trapManager.stop();
                    getJBetPage().changePanel(TrapMonitorPanel.class, null, target);
                } catch (IOException e) {
                    LOGGER.error("Error while stop trap", e);
                    getFeedback().error(getString("error.stopTrap"));
                    target.addComponent(getFeedback());
                }
            }
            @Override
            public boolean isVisible() {
                return trapManager.isRunning();
            }
        });
    
    }
    
    @Override
    protected MessageAppender createMessageAppender(IModel<String> model) {
        return trapManager.getMessageAppender();
    }
    
}
