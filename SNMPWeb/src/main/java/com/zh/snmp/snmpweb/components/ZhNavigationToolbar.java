package com.zh.snmp.snmpweb.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

public class ZhNavigationToolbar extends AbstractToolbar{

    private static final long serialVersionUID = 1L;

	private final DataTable<?> table;

	/**
	 * Constructor
	 *
	 * @param table
	 *            data table this toolbar will be attached to
	 */
	public ZhNavigationToolbar(final DataTable<?> table)
	{
		super(table);
		this.table = table;

		WebMarkupContainer span = new WebMarkupContainer("span");
		add(span);
		span.add(new AttributeModifier("colspan", true, new Model<String>(
			String.valueOf(table.getColumns().length))));

		span.add(newPagingNavigator("navigator", table));
		span.add(newNavigatorLabel("navigatorLabel", table));
	}

	/**
	 * Factory method used to create the paging navigator that will be used by the datatable
	 *
	 * @param navigatorId
	 *            component id the navigator should be created with
	 * @param table
	 *            dataview used by datatable
	 * @return paging navigator that will be used to navigate the data table
	 */
	protected ZhPagingNavigator newPagingNavigator(String navigatorId, final DataTable<?> table)
	{
		return new ZhPagingNavigator(navigatorId, table);
	}

	/**
	 * Factory method used to create the navigator label that will be used by the datatable
	 *
	 * @param navigatorId
	 *            component id navigator label should be created with
	 * @param table
	 *            dataview used by datatable
	 * @return navigator label that will be used to navigate the data table
	 *
	 */
	protected WebComponent newNavigatorLabel(String navigatorId, final DataTable<?> table)
	{
		return new ZhNavigatorLabel(navigatorId, table);
	}

	/**
	 * @see org.apache.wicket.Component#callOnBeforeRenderIfNotVisible()
	 */
	@Override
	protected boolean callOnBeforeRenderIfNotVisible()
	{
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected void onBeforeRender()
	{
		setVisible(table.getPageCount() > 1);
		super.onBeforeRender();
	}
}
