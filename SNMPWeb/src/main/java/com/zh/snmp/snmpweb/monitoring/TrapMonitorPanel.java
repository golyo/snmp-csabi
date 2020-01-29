package com.zh.snmp.snmpweb.monitoring;

import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.snmp.trap.TrapManager;
import java.io.IOException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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
