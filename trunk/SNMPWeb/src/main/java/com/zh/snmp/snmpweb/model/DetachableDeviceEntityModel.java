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

import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class DetachableDeviceEntityModel extends LoadableDetachableModel<DeviceEntity> {
    @SpringBean
    private DeviceService service;
    private String id;
    
    public DetachableDeviceEntityModel(String id) {
        super();
        InjectorHolder.getInjector().inject(this);
        this.id = id;
    }
    
    public DetachableDeviceEntityModel(DeviceEntity object) {
        super(object);
        InjectorHolder.getInjector().inject(this);
        this.id = object.getId();
    }
    
    @Override
    protected DeviceEntity load() {
        return service.findDeviceEntityById(id);
    }
    
}