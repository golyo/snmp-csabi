package com.zh.snmp.snmpweb.components;

import com.zh.snmp.snmpweb.components.ZhNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;

public class ZhDataDable<T> extends DataTable<T>{
    private static final long serialVersionUID = 1L;
    
    /**
     * 
     * @param id
     * @param columns
     * @param dataProvider
     * @param rowsPerPage 
     */
    public ZhDataDable(String id, IColumn<T>[] columns,
            ISortableDataProvider<T> dataProvider, int rowsPerPage) {
        super(id, columns, dataProvider, rowsPerPage);

        addTopToolbar(new ZhNavigationToolbar(this));
        addTopToolbar(new HeadersToolbar(this, dataProvider));
        addBottomToolbar(new NoRecordsToolbar(this));
    }

    @Override
    protected Item<T> newRowItem(String id, int index, IModel<T> model) {
        return new OddEvenItem<T>(id, index, model);
    }

}
