package com.zh.snmp.snmpweb.pages;

import com.zh.snmp.snmpweb.menu.MenuConfig;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public abstract class BasePanel<T> extends Panel {
    public BasePanel(String id, IModel<T> model) {
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

    public IModel<T> getPanelModel() {
        return (IModel<T>)getDefaultModel();
    }
    public Class<? extends BasePanel>[] getMainMenuConfig() {
        return BasePage.getMenuConfig(getClass());
    }
    
}
