package com.zh.snmp.snmpweb.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public abstract class JBetLinkPanel extends Panel {
    public JBetLinkPanel(String id, IModel<String> labelModel) {
        super(id);
        add(new AjaxLink("panelLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onDetailClick(target);
            }
        }.add(new Label("linkLabel", labelModel)));
    }

    protected abstract void onDetailClick(AjaxRequestTarget target);

}
