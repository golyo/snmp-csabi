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
package com.zh.snmp.snmpweb.service;

import com.zh.snmp.snmpcore.entities.DeviceConfigEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Golyo
 */
@WebService(serviceName = "SnmpWebService")
public class SnmpWebService {
    @Resource
    private WebServiceContext context;

    private SnmpService service;

    public SnmpWebService() {
        
    }
    
    @WebMethod(operationName = "getConfigurations")
    public List<String> getConfigurations() {
        init();
        List<DeviceConfigEntity> confs = service.findDeviceConfigByFilter(new DeviceConfigEntity(), null, 0, -1);
        List<String> ret = new LinkedList<String>();
        for (DeviceConfigEntity dc: confs) {
            ret.add(dc.getCode());
        }
        return ret;
    }
    
    @WebMethod(operationName = "createDevice")
    public Boolean createDevice(@WebParam(name = "nodeId") String nodeId, @WebParam(name = "ipAddress") String ipAddress, @WebParam(name = "macAddress") String macAddress) {
        init();
        DeviceEntity de = service.findDeviceByNodeId(nodeId);
        if (de != null) {
            return Boolean.FALSE;
        } else {
            de = new DeviceEntity();
            de.setNodeId(nodeId);
            de.setIpAddress(ipAddress);
            de.setMacAddress(macAddress);
            de = service.saveDevice(de);
            return Boolean.TRUE;
        }
    }
    
    @WebMethod(operationName = "setDeviceConfig")
    public Boolean setDeviceConfig(@WebParam(name = "nodeId") String nodeId, @WebParam(name = "configCode") String configCode) {
        init();
        return service.setDeviceConfig(nodeId, configCode) != null;
    }

    public void init() {
        if (service == null) {
            ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
            WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            service = (SnmpService)wac.getBean("SnmpService");            
        }
    }
}
