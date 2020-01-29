package com.zh.snmp.snmpweb.monitoring;

import com.zh.snmp.snmpcore.message.MessageAppender;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Golyo
 */
public class MonitorPopupPanel<T> extends Panel {
    private ModalWindow modal;
    private String titleKey;
    public MonitorPopupPanel(ModalWindow modal, IModel<T> model, MessageAppender appender, String titleKey) {
        super(modal.getContentId());
        this.modal = modal;
        this.titleKey = titleKey;
        add(new MonitorPanel("monitor", model, appender));
    }
    
    public void show(AjaxRequestTarget target) {
        if (modal.isShown()) {
            return;
        }
        modal.setContent(this);
        modal.setTitle(getString(titleKey, null, titleKey));
        modal.setInitialHeight(450);
        modal.setInitialWidth(650);
        modal.setContent(this);
        modal.show(target);
    }    
}
