package com.zh.snmp.snmpweb.components;

import com.zh.snmp.snmpweb.pages.BasePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DataTablePanel<R> extends BasePanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataTablePanel.class);
    protected static final int ROWS_PER_PAGE = 20;
    private static final String TABLE_ID = "datatable";
    private ZhDataDable<R> dataTable;

    public DataTablePanel(String id, ISortableDataProvider<R> dataProvider, int rowsPerPage) {
        super(id, null);
        dataTable = new ZhDataDable(TABLE_ID, createTableColumns(), dataProvider, rowsPerPage);
        FilterForm form = new FilterForm(
                "filterform", (IFilterStateLocator)dataProvider);
        dataTable.addTopToolbar(
                new FilterToolbar(dataTable, (FilterForm) form, (IFilterStateLocator)dataProvider));
        form.add(dataTable);
        add(form);
    }


    protected void onRowSelected(AjaxRequestTarget target, IModel<R> rowModel) {
        getJBetPage().changePanel(getDetailMenuConfig(), rowModel, DataTablePanel.this.getClass(), target);
    }

    protected IModel<String> createRowDisplayModel() {
        return null;
    }
    protected IModel<String> createRowLinkModel() {
        return null;
    }
    
    protected abstract IColumn<R>[] createTableColumns();
    
    @Override
    public Class<? extends BasePanel>[] getMainMenuConfig() {
        return null;
    }

    public Class<? extends BasePanel>[] getDetailMenuConfig() {
        return super.getMainMenuConfig();
    }

    public ZhDataDable<R> getDataTable() {
        return dataTable;
    }

    public IDataProvider<R> getDataProvider() {
        return dataTable.getDataProvider();
    }
}
