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

import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.util.List;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author Golyo
 */
public class DetachableDeviceConfigListModel extends LoadableDetachableModel<List<? extends DeviceConfigEntity>> {
    public static final IChoiceRenderer<DeviceConfigEntity> DEVICE_CONFIG_RENDERER = new IChoiceRenderer<DeviceConfigEntity>() {

        @Override
        public Object getDisplayValue(DeviceConfigEntity object) {
            return object != null ? object.getId() + " - " + object.getName() : " - ";
        }

        @Override
        public String getIdValue(DeviceConfigEntity object, int index) {
            return object != null ? object.getId() : null;
        }
    };
    
    @SpringBean
    private ConfigService service;
    private DeviceConfigEntity filter;
    
    public DetachableDeviceConfigListModel() {
        this(new DeviceConfigEntity());
    }
    
    public DetachableDeviceConfigListModel(DeviceConfigEntity filter) {
        this.filter = filter;
        InjectorHolder.getInjector().inject(this);
    }
    
    @Override
    protected List<DeviceConfigEntity> load() {
        return service.findDeviceConfigByFilter(filter, null, 0, -1);
    }
}
