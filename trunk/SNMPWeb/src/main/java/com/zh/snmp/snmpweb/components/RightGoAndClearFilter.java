/*
 *  Copyright (c) 2010 Sonrisa Informatikai Kft. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Sonrisa Informatikai Kft. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sonrisa.
 *
 * SONRISA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SONRISA SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
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
