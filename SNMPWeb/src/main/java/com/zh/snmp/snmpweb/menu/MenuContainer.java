package com.zh.snmp.snmpweb.menu;

import com.zh.snmp.snmpweb.pages.BasePanel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

public abstract class MenuContainer extends Panel {
    private Class<? extends MarkupContainer> parentMenu;
    private Class<? extends BasePanel> selectedMenu;
    private Class<? extends BasePanel>[] config;

    public MenuContainer(String id, final IModel model, Class<? extends MarkupContainer> parentMenu, Class<? extends BasePanel>[] config) {
        this(id, model, parentMenu, null, config);
    }
    
    public MenuContainer(String id, final IModel model, Class<? extends MarkupContainer> parentMenu, Class<? extends BasePanel> selectedMenu, Class<? extends BasePanel>[] config) {
        super(id, model);
        this.config = config;
        this.parentMenu = parentMenu;
        this.selectedMenu = selectedMenu;
        setOutputMarkupId(true);
        setVisible(config != null);
        createView();
    }

    protected void createView() {
        RepeatingView menurw = new RepeatingView("menuItems");
        if (config != null) {
            WebMarkupContainer itemPanel;
            for (final Class<? extends BasePanel> child: config) {
                menurw.add(itemPanel = new WebMarkupContainer(menurw.newChildId(), null));
                itemPanel.add(new AttributeModifier("class", true, new Model<String>(){
                    @Override
                    public String getObject() {
                        return (selectedMenu == child) ? "selected" : "";
                    }
                }));
                itemPanel.setOutputMarkupId(true);
                String key = child.getSimpleName() + ".menuTitle";
                AjaxLink link = new AjaxLink("menuLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        if (selectedMenu != child) {
                            selectedMenu = child;
                            target.addComponent(MenuContainer.this);
                            onMenuClick(parentMenu, child, true, target);
                        } else {
                            onMenuClick(parentMenu, child, false, target);
                        }
                    }
                };
                link.add(new Label("menuText", new StringResourceModel(key, this, null, key)));
                itemPanel.add(link);
            }
        }
        add(menurw);
    }

    public Class<? extends BasePanel> getSelectedMenu() {
        return selectedMenu;
    }

    public Class<? extends MarkupContainer> getParentMenu() {
        return parentMenu;
    }
    
    protected abstract void onMenuClick(Class<? extends MarkupContainer> parentClz, Class<? extends BasePanel> panelClz, boolean changed, AjaxRequestTarget art);
}
