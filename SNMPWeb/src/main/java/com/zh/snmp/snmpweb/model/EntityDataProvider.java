package com.zh.snmp.snmpweb.model;

import com.zh.snmp.snmpcore.entities.BaseEntity;
import java.util.Iterator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

public abstract class EntityDataProvider<T extends BaseEntity> extends SortableDataProvider<T> implements IFilterStateLocator{
    private T filter;

    public EntityDataProvider(T filter) {
        this.filter = filter;
    }

    @Override
    public T getFilterState() {
        return filter;
    }

    @Override
    public void setFilterState(Object state) {
        filter = (T)state;
    }
    
    public String getSortParam() {
        return getSort() != null ? getSort().getProperty() : null;
    }
    
    public boolean isSortAscending() {
        return getSort() != null ? getSort().isAscending() : Boolean.FALSE;
    }
}
