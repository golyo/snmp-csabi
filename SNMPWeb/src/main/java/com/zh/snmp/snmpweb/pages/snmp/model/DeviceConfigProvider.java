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
package com.zh.snmp.snmpweb.pages.snmp.model;

import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import java.util.Iterator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Golyo
 */
public class DeviceConfigProvider implements ISortableDataProvider<DeviceConfigEntity> {

    @Override
    public Iterator<? extends DeviceConfigEntity> iterator(int first, int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IModel<DeviceConfigEntity> model(DeviceConfigEntity object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void detach() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ISortState getSortState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSortState(ISortState state) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
