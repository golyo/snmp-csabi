package com.zh.snmp.snmpweb.components;

import com.zh.snmp.snmpweb.BaseSession;
import com.zh.snmp.snmpweb.util.DateUtils;
import java.text.DateFormat;
import java.util.Date;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.PropertyResolver;

public class DatePropertyColumn<T> extends PropertyColumn<T> {

    private int style;
    private boolean widthTime;

    public DatePropertyColumn(IModel<String> displayModel, String sortProperty, String property, boolean widthTime) {
        this(displayModel, sortProperty, property, DateFormat.SHORT, widthTime);
    }

    public DatePropertyColumn(IModel<String> displayModel, String sortProperty, String property, int style, boolean widthTime) {
        super(displayModel, sortProperty, property);
        this.style = style;
        this.widthTime = widthTime;
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
        Date d = (Date)PropertyResolver.getValue(getPropertyExpression(), rowModel.getObject());
        item.add(new Label(componentId, DateUtils.parseDate(d, style, BaseSession.get().getLocale(), widthTime)));
    }
}
