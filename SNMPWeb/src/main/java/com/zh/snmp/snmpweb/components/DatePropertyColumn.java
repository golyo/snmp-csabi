/*
 *  Copyright (c) 2010 Sonrisa Informatikai Kft. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Sonrisa Informatikai Kft. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sonrisa.
 *
 * SONRISA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SONRISA SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
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

/**
 * Formázott dátum property
 * @author cserepj
 */
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
