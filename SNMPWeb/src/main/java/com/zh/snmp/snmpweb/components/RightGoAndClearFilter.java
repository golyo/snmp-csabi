package com.zh.snmp.snmpweb.components;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoAndClearFilter;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

/**
 * Jobb oldalra rendzettt GoAndClearFilter.
 * @author golyo
 */
public class RightGoAndClearFilter extends GoAndClearFilter {
    public RightGoAndClearFilter(String id, FilterForm form) {
        super(id, form);
        getClearButton().setModel(new StringResourceModel("GoAndClearFilter.clear", null));
        getClearButton().setDefaultFormProcessing(false);
        getGoButton().setModel(new StringResourceModel("GoAndClearFilter.go", null));
        add(new AttributeAppender("class", new Model<String>("buttonHolder"), " "));
    }

    @Override
    protected void onClearSubmit(Button button) {
        button.getForm().clearInput();
        super.onClearSubmit(button);
    }
}
