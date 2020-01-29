package com.zh.snmp.snmpweb.monitoring;

import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.snmp.trap.TimerUpdater;
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
public class UpdateMonitorPanel extends MonitorPanel<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrapMonitorPanel.class);

    @SpringBean
    private TimerUpdater timerUpdater;
    
    public UpdateMonitorPanel(String id) {
        super(id, Model.of(""));
        add(new AjaxLink("start") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                timerUpdater.start();
                getJBetPage().changePanel(UpdateMonitorPanel.class, null, target);
            }
            
            @Override
            public boolean isVisible() {
                return !timerUpdater.isRunning();
            }
        });
        add(new AjaxLink("stop") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                timerUpdater.destroy();
            }
            @Override
            public boolean isVisible() {
                return timerUpdater.isRunning();
            }
        });
    
    }
    
    @Override
    protected MessageAppender createMessageAppender(IModel<String> model) {
        return timerUpdater.getMessageAppender();
    }    
    
    @Override
    protected boolean canRestart() {
        return true;
    }
    
}
