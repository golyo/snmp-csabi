package com.zh.snmp.snmpweb.components;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author Golyo
 */
public class EnumPropertyColumn<T> extends AbstractColumn<T> {

    public EnumPropertyColumn(IModel<String> resourceModel, String property) {
        super(resourceModel, property);
    }
    
    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(new EnumLabel(componentId, new PropertyModel(rowModel.getObject(), getSortProperty())));
    }
    
}
