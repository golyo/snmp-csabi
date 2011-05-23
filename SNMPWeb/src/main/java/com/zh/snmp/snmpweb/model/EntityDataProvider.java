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

package com.zh.snmp.snmpweb.model;

import com.zh.snmp.snmpcore.entities.BaseEntity;
import java.util.Iterator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

/**
 *
 * @author sonrisa
 */
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
