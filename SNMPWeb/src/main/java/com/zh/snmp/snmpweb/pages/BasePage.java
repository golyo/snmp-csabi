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

import com.zh.snmp.snmpweb.BaseSession;
import com.zh.snmp.snmpweb.menu.MenuConfig;
import com.zh.snmp.snmpweb.menu.TabContainer;
import com.zh.snmp.snmpweb.util.WicketUtils;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sonrisa
 */
public abstract class BasePage extends WebPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class);
    private static final long serialVersionUID = 1L;

    public static final String MAIN = "main";
    public static final String MENU = "menu";

    private BasePanel main = null;
    private final ModalWindow modal;
    private final Form form;
    private final FeedbackPanel feedback;
    private final TabContainer tabContainer;

    public BasePage(final PageParameters parameters) {
        super(parameters);
        add(new Link("logout") {
            @Override
            public boolean isVisible() {
                return BaseSession.get().isSignedIn();
            }
            @Override
            public void onClick() {
                BaseSession.get().signOut();
                setResponsePage(SnmpPage.class, null);
            }
        }) ;
        add(form = isFormStateless() ? new StatelessForm("form") {
            @Override
            public void onSubmit() {
                main.onSubmitForm();
            }
        }
        : new Form("form") {
            @Override
            public void onSubmit() {
                main.onSubmitForm();
            }
        });
        form.add(main = buildContent(MAIN, parameters));
        main.setOutputMarkupId(true);
        form.add(new Label("panelTitle", getPanelTitleModel())).setOutputMarkupId(true);
        add(modal = new ModalWindow("modal"));
        form.add(feedback = new FeedbackPanel("feedback"));
        //addMainMenuToForm(null);
        feedback.setOutputMarkupId(true);
        
        tabContainer = new TabContainer(MENU, this, getMainMenuConfig()) {
            @Override
            protected void onTabMenuClick(Class<? extends MarkupContainer> parentClz, Class<? extends BasePanel> panelClz, IModel model, AjaxRequestTarget art) {
                changePanel(panelClz, model, art);
            }
        };
        add(tabContainer);
        //actualizeMenu(null);
    }
    public abstract BasePanel buildContent(String id, final PageParameters parameters);

    public void backToParentPanel(AjaxRequestTarget target) {
        tabContainer.triggerParentClick(target);
    }
    public boolean isFormStateless() {
        return false;
    }

    @Override
    public BaseSession getSession() {
        return (BaseSession) super.getSession();
    }

    public void changePanel(Class<? extends BasePanel> panelClz, IModel model, AjaxRequestTarget art) {
        BasePanel panel = WicketUtils.createPanel(BasePage.MAIN, panelClz, model);
        main.getParent().replace(panel);
        main = panel;
        //addMainMenuToForm(model);
        art.addComponent(form);
    }

    public void refreshPanel(AjaxRequestTarget target) {
        target.addComponent(form);
    }
    
    public void changePanel(Class<? extends BasePanel>[] config, IModel model, Class<? extends MarkupContainer> parentClz, AjaxRequestTarget art) {
        tabContainer.addNewTabLine(model, parentClz, config[0], config);
        art.addComponent(tabContainer);
        changePanel(config[0], model, art);
    }
    
/*
    protected void addMainMenuToForm(IModel pmodel) {
        final Class<? extends BasePanel>[] config = main.getMainMenuConfig();
        MenuContainer cont = new MenuContainer("mainMenu", pmodel, main.getClass(), config) {
            @Override
            protected void onMenuClick(Class<? extends MarkupContainer> parentClz, Class<? extends BasePanel> panelClz, boolean changedMenuItem, AjaxRequestTarget art) {
                tabContainer.addNewTabLine(getDefaultModel(), parentClz, panelClz, config);
                art.addComponent(tabContainer);
                changePanel(panelClz, getDefaultModel(), art);
            }
        };
        if (form.get("mainMenu") != null) {
            form.replace(cont);
        } else {
            form.add(cont);
        }
    }
*/
    protected IModel<String> getPanelTitleModel() {
        return new Model<String>() {
                private final static long serialVersionUID = 1L;
                @Override
                public String getObject() {
                    return main.getPanelTitle();
                }
            };
    }

    public BasePanel getMainPanel() {
        return main;
    }

    public ModalWindow getModal() {
        return modal;
    }
    
    public FeedbackPanel getFeedback() {
        return feedback;
    }

    public Form getForm() {
        return form;
    }

    public Class<? extends BasePanel>[] getMainMenuConfig() {
        MenuConfig c = getClass().getAnnotation(MenuConfig.class);
        return c != null ? c.context() : null;
    }

    public IModel getMainMenuModel() {
        return getDefaultModel();
    }
}
