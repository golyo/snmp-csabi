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
                Class<? extends BasePanel>[] config = getMenuConfig(panelClz);
                if (config != null && config.length > 0 && config[0] == panelClz && parentClz != panelClz) {
                    changePanel(config, model, panelClz, art);
                } else {
                    changePanel(panelClz, model, art);                    
                }                
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
        return getMenuConfig(getClass());
    }

    public IModel getMainMenuModel() {
        return getDefaultModel();
    }
    
    public static Class<? extends BasePanel>[] getMenuConfig(Class clz) {
        MenuConfig c = (MenuConfig)clz.getAnnotation(MenuConfig.class);
        return c != null ? c.context() : null;
    }
}
