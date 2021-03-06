package com.zh.snmp.snmpweb.service;

import com.zh.snmp.snmpcore.domain.Device;
import com.zh.snmp.snmpcore.domain.DeviceNode;
import com.zh.snmp.snmpcore.domain.DinamicValue;
import com.zh.snmp.snmpcore.entities.ChangeLogEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.DeviceState;
import com.zh.snmp.snmpcore.message.SimpleMessageAppender;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import com.zh.snmp.snmpcore.services.impl.SnmpBackgroundProcess;
import java.util.Arrays;
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
    public String createDevice(@WebParam(name = "configCode") String configCode, @WebParam(name = "deviceId") String deviceId, @WebParam(name = "nodeId") String nodeId, @WebParam(name = "ipAddress") String ipAddress, @WebParam(name = "macAddress") String macAddress) {
        init();
        DeviceEntity de = deviceService.findDeviceEntityById(deviceId);
        de = new DeviceEntity();
        de.setConfigCode(configCode);
        de.setId(deviceId);
        de.setIpAddress(ipAddress);
        de.setMacAddress(macAddress);
        de.setNodeId(nodeId);
        String ret = deviceService.saveEntity(de);
        return ret;
    }
    
    private static final String PATH_DELIM = "\\.";
    @WebMethod(operationName = "setDeviceConfig")
    public Long setDeviceConfig(@WebParam(name = "deviceId") String deviceId, @WebParam(name = "configPath") String configPath, @WebParam(name = "dinamicValues") List<DinamicValue> dinamicValues, @WebParam(name = "mode") int mode) {
        init();
        List<String> path = Arrays.asList(configPath.split(PATH_DELIM));
        Device device = deviceService.setDeviceConfig(deviceId, path, dinamicValues, mode);
        if (device != null) {
            SnmpBackgroundProcess process = service.startSnmpBackgroundProcess(SnmpService.WEBSERVICE_USERNAME, device.getDeviceId(), new SimpleMessageAppender());
            return process != null ? process.getLog().getId() : null;
        } else {
            return null;
        }
    }

    @WebMethod(operationName = "getTransactionState")
    public DeviceState getTransactionState(@WebParam(name = "transactionId") Long changeLogId) {
        init();
        ChangeLogEntity log = deviceService.findLog(changeLogId);
        return log != null ? log.getStateAfter() : null;
    }

    @WebMethod(operationName = "deleteDevice")
    public boolean deleteDevice(@WebParam(name = "deviceId") String deviceId) {
        init();
        return deviceService.deleteDevice(deviceId);
    }
   
    @WebMethod(operationName = "checkDevice")
    public boolean checkDevice(@WebParam(name = "deviceId") String deviceId) {
        init();
        Device device = deviceService.findDeviceByDeviceId(deviceId);        
        return service.checkDevice(device, new SimpleMessageAppender());
    }
    
    @WebMethod(operationName = "getDeviceConfig")
    public List<String> getDeviceConfig(@WebParam(name = "deviceId") String deviceId) {
        init();
        Device device = deviceService.findDeviceByDeviceId(deviceId);
        if (device != null) {
            return device.getConfigMap().getSelectedChildList();
        } else {
            return null;            
        }
    }
    
    @WebMethod(operationName = "getDinamicValues")
    public List<DinamicValue> getDinamicValues(@WebParam(name = "deviceId") String deviceId, @WebParam(name = "configPath") String configPath) {
        init();
        Device device = deviceService.findDeviceByDeviceId(deviceId);
        List<String> path = Arrays.asList(configPath.split(PATH_DELIM));
        if (path.size() > 1) {
            LinkedList<String> pathl = new LinkedList<String>(path);
            pathl.pop(); //config code
            DeviceNode dn = device.getConfigMap().findChainChild(pathl);
            if (dn != null) {
                return dn.getDinamics();
            }
        }
        return null;
    }
    
    @WebMethod(operationName = "getDeviceState")
    public DeviceState getDeviceState(@WebParam(name = "deviceId") String deviceId) {
        init();
        DeviceEntity entity = deviceService.findDeviceEntityById(deviceId);
        return entity != null ? entity.getConfigState() : null;
    }

    
    public void init() {
        if (service == null) {
            ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
            WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            service = (SnmpService)wac.getBean("snmpService");    
            deviceService = (DeviceService)wac.getBean("deviceService");
            configService = (ConfigService)wac.getBean("configService");
        }
    }
}
