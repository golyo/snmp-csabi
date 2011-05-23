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

import org.apache.wicket.IClusterable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

/**
 *
 * @author monusa
 */
public class ZhNavigatorLabel extends Label {

    private static final long serialVersionUID = 1L;

    // TODO Factor this interface out and let dataview/datatable implement it
    private static interface PageableComponent extends IClusterable {

        /**
         * @return total number of rows across all pages
         */
        int getRowCount();

        /**
         * @return current page
         */
        int getCurrentPage();

        /**
         * @return rows per page
         */
        int getRowsPerPage();
    }

    /**
     * @param id
     *            component id
     * @param table
     *            table
     */
    public ZhNavigatorLabel(final String id, final DataTable<?> table) {
        this(id, new PageableComponent() {

            private static final long serialVersionUID = 1L;

            @Override
            public int getCurrentPage() {
                return table.getCurrentPage();
            }

            @Override
            public int getRowCount() {
                return table.getRowCount();
            }

            @Override
            public int getRowsPerPage() {
                return table.getRowsPerPage();
            }
        });

    }

    /**
     * @param id
     *            component id
     * @param list
     *            listview
     */
    public ZhNavigatorLabel(final String id, final PageableListView<?> list) {
        this(id, new PageableComponent() {

            private static final long serialVersionUID = 1L;

            @Override
            public int getCurrentPage() {
                return list.getCurrentPage();
            }

            @Override
            public int getRowCount() {
                return list.getModelObject().size();
            }

            @Override
            public int getRowsPerPage() {
                return list.getRowsPerPage();
            }
        });

    }

    /**
     * @param id
     *            component id
     * @param table
     *            pageable view
     */
    public ZhNavigatorLabel(final String id, final DataView<?> table) {
        this(id, new PageableComponent() {

            private static final long serialVersionUID = 1L;

            @Override
            public int getCurrentPage() {
                return table.getCurrentPage();
            }

            @Override
            public int getRowCount() {
                return table.getRowCount();
            }

            @Override
            public int getRowsPerPage() {
                return table.getItemsPerPage();
            }
        });

    }

    private ZhNavigatorLabel(final String id, final PageableComponent table) {
        super(id);
        setDefaultModel(new StringResourceModel("NavigatorLabel", this,
                new Model<LabelModelObject>(new LabelModelObject(table)),
                "${from}-${to}. a ${of} találatból"));
    }

    private class LabelModelObject implements IClusterable {

        private static final long serialVersionUID = 1L;
        private final PageableComponent table;

        /**
         * Construct.
         *
         * @param table
         */
        public LabelModelObject(PageableComponent table) {
            this.table = table;
        }

        /**
         * @return "z" in "Showing x to y of z"
         */
        public int getOf() {
            return table.getRowCount();
        }

        /**
         * @return "x" in "Showing x to y of z"
         */
        public int getFrom() {
            if (getOf() == 0) {
                return 0;
            }
            return (table.getCurrentPage() * table.getRowsPerPage()) + 1;
        }

        /**
         * @return "y" in "Showing x to y of z"
         */
        public int getTo() {
            if (getOf() == 0) {
                return 0;
            }
            return Math.min(getOf(), getFrom() + table.getRowsPerPage() - 1);
        }
    }
}
