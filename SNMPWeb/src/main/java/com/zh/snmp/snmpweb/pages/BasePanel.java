/*
 *  Copyright 2010 sonrisa.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.zh.snmp.snmpweb.pages;

import com.zh.snmp.snmpweb.menu.MenuConfig;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author sonrisa
 */
public abstract class BasePanel extends Panel {
    public BasePanel(String id, IModel model) {
        super(id, model);
    }

    public String getPanelTitle() {
        String key = getClass().getSimpleName() + ".panelTitle";
        return getString(key, null, key);
    }

    public BasePage getJBetPage() {
        return (BasePage) getPage();
    }

    protected ModalWindow getModal() {
        return getJBetPage().getModal();
    }

    protected Form getForm() {
        return getJBetPage().getForm();
    }
    public FeedbackPanel getFeedback() {
        return getJBetPage().getFeedback();
    }

    public void onSubmitForm() {
        
    }

    public Class<? extends BasePanel>[] getMainMenuConfig() {
        MenuConfig c = getClass().getAnnotation(MenuConfig.class);
        return c != null ? c.context() : null;
    }
}
