package com.zh.snmp.snmpweb.components;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilteredColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public abstract class RowLinkColumn<T> extends AbstractColumn<T> implements IFilteredColumn<T> {

    private String cssClass;
    private IModel<String> linkModel;

    public RowLinkColumn(IModel displayModel, IModel<String> linkModel, String cssClass) {
        super(displayModel);
        this.linkModel = linkModel;
        this.cssClass = cssClass;
    }

    @Override
    public String getCssClass() {
        return cssClass;
    }
    
    @Override
    public void populateItem(final Item<ICellPopulator<T>> cellItem, final String componentId,final IModel<T> rowModel) {
        cellItem.add(new JBetLinkPanel(componentId, linkModel) {
            @Override
            protected void onDetailClick(AjaxRequestTarget target) {
                onRowSelect(target, rowModel);
            }
        });
    }

    protected abstract void onRowSelect(AjaxRequestTarget target, IModel<T> rowModel);

    @Override
    public Component getFilter(String componentId, FilterForm form) {
        return new RightGoAndClearFilter(componentId, form);
    }
}
