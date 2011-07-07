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

import com.zh.snmp.snmpcore.entities.HistoryEntity;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.util.Collections;
import java.util.Iterator;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class HistoryProvider extends EntityDataProvider<HistoryEntity> {
    @SpringBean
    private SnmpService srv;
    
    public HistoryProvider() {
        super(new HistoryEntity());
        InjectorHolder.getInjector().inject(this);
    }
    
    @Override
    public Iterator<? extends HistoryEntity> iterator(int first, int count) {
        
        return Collections.EMPTY_LIST.iterator();
        //return srv.findHistoryByFilter(getFilterState(), getSortParam(), first, count).iterator();
    }

    @Override
    public IModel<HistoryEntity> model(HistoryEntity object) {
        return Model.of(object);
    }

    @Override
    public int size() {
        return 0;
        //return srv.countHistory(getFilterState());
    }
    
}

