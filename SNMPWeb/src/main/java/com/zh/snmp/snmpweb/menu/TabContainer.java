package com.zh.snmp.snmpweb.menu;

import com.zh.snmp.snmpweb.pages.BasePage;
import com.zh.snmp.snmpweb.pages.BasePanel;
import java.util.Iterator;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class TabContainer extends Panel {

    private RepeatingView menuView;

    public TabContainer(String id, BasePage page, Class<? extends BasePanel>[] config) {
        super(id);
        add(menuView = new RepeatingView("menuCont"));
        addNewTabLine(page.getDefaultModel(), page.getClass(), page.getMainPanel().getClass(), config);
        setOutputMarkupId(true);
    }

    protected void onTabChanged() {
    }

    protected abstract void onTabMenuClick(Class<? extends MarkupContainer> parentClz, Class<? extends BasePanel> panelClz, IModel model, AjaxRequestTarget art);


    public void triggerParentClick(AjaxRequestTarget target) {
        int size = menuView.size();
        if (size > 1) {
            MenuContainer cont = (MenuContainer)menuView.get(size-2);
            onMenuContainerClick(cont, true, target);
        }
    }

    protected void onMenuContainerClick(MenuContainer mcont, boolean changedItem, AjaxRequestTarget art) {
        Iterator<MenuContainer> it = (Iterator<MenuContainer>)menuView.iterator();
        MenuContainer act, changedCont = null;
        boolean changed = false;
        while (changedCont == null && it.hasNext()) {
            act = it.next();
            if (mcont.getParentMenu() == act.getParentMenu()) {
                changedCont = act;
            }
        }
        while (it.hasNext()) {
            it.next();
            it.remove();
            changed = true;
        }
        if (changed) {
            art.addComponent(TabContainer.this);
            onTabChanged();
        }
        if (changed || changedItem) {
            onTabMenuClick(mcont.getParentMenu(), mcont.getSelectedMenu(), mcont.getDefaultModel(), art);
        }
    }

    public void addNewTabLine(final IModel model, Class<? extends MarkupContainer> parentMenu, Class<? extends BasePanel> selectedMenu, Class<? extends BasePanel>[] config) {
        if (config != null) {
            MenuContainer cont = new MenuContainer(menuView.newChildId(), model, parentMenu, selectedMenu, config) {
                @Override
                protected void onMenuClick(Class<? extends MarkupContainer> parentClz, Class<? extends BasePanel> panelClz, boolean changedItem, AjaxRequestTarget art) {
                    onMenuContainerClick(this, changedItem, art);
                }
            };
            if (menuView.size() == 0) {
                cont.add(new AttributeModifier("class", true, Model.of("firstBar")));
            }
            menuView.add(cont);
        }
    }    
}
