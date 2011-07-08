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

import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.DeviceState;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.util.Arrays;
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

    private ConfigService configService;
    
    private DeviceService deviceService;
    
    public SnmpWebService() {
        
    }
    
    @WebMethod(operationName = "getConfigurations")
    public List<String> getConfigurations() {        
        init();
        return configService.getConfigCodes();
    }
    
    @WebMethod(operationName = "createDevice")
    public Boolean createDevice(@WebParam(name = "configCode") String configCode, @WebParam(name = "deviceId") String deviceId, @WebParam(name = "nodeId") String nodeId, @WebParam(name = "ipAddress") String ipAddress, @WebParam(name = "macAddress") String macAddress) {
        init();
        DeviceEntity de = deviceService.findDeviceEntityById(deviceId);
        if (de != null) {
            return false;           
        } else {
            de = new DeviceEntity();
            de.setConfigCode(configCode);
            de.setId(deviceId);
            de.setIpAddress(ipAddress);
            de.setMacAddress(macAddress);
            de = deviceService.saveEntity(de);
            return de != null;
        }
    }
    
    private static final String PATH_DELIM = "\\.";
    @WebMethod(operationName = "setDeviceConfig")
    public Boolean setDeviceConfig(@WebParam(name = "deviceId") String deviceId, @WebParam(name = "configPath") String configPath, int mode) {
        init();
        List<String> path = Arrays.asList(configPath.split(PATH_DELIM));
        return deviceService.setDeviceConfig(deviceId, path, mode);
        /*
        return service.setDeviceConfig(nodeId, configCode) != null;
         * 
         */
    }

    @WebMethod(operationName = "setDinamicConfigValue")
    public Boolean setDinamicConfigValue(@WebParam(name = "deviceId") String deviceId, @WebParam(name = "configPath") String configPath, String value) {
        init();
        return false;
        /*
        return service.setDeviceConfig(nodeId, configCode) != null;
         * 
         */
    }

    @WebMethod(operationName = "getDeviceState")
    public DeviceState setDinamicConfigValue(@WebParam(name = "deviceId") String deviceId) {
        init();
        return DeviceState.NOT_FOUND;
        /*
        return service.setDeviceConfig(nodeId, configCode) != null;
         * 
         */
    }

    
    public void init() {
        if (service == null) {
            ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
            WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            service = (SnmpService)wac.getBean("SnmpService");    
            deviceService = (DeviceService)wac.getBean("DeviceService");
            configService = (ConfigService)wac.getBean("ConfigService");
        }
    }
}
