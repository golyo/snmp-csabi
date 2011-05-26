/*
 *   Copyright (c) 2010 Sonrisa Informatikai Kft. All Rights Reserved.
 * 
 *  This software is the confidential and proprietary information of
 *  Sonrisa Informatikai Kft. ("Confidential Information").
 *  You shall not disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with Sonrisa.
 * 
 *  SONRISA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 *  THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SONRISA SHALL NOT BE LIABLE FOR
 *  ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 *  DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

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

/**
 *
 * @author sonrisa
 */
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
