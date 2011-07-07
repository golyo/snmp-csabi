
package com.zh.snmp.snmpclient.generated;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.7-b01-
 * Generated source version: 2.1
 * 
 */
@WebService(name = "SnmpWebService", targetNamespace = "http://service.snmpweb.snmp.zh.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SnmpWebService {


    /**
     * 
     */
    @WebMethod
    @RequestWrapper(localName = "init", targetNamespace = "http://service.snmpweb.snmp.zh.com/", className = "com.zh.snmp.snmpclient.generated.Init")
    @ResponseWrapper(localName = "initResponse", targetNamespace = "http://service.snmpweb.snmp.zh.com/", className = "com.zh.snmp.snmpclient.generated.InitResponse")
    public void init();

    /**
     * 
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getConfigurations", targetNamespace = "http://service.snmpweb.snmp.zh.com/", className = "com.zh.snmp.snmpclient.generated.GetConfigurations")
    @ResponseWrapper(localName = "getConfigurationsResponse", targetNamespace = "http://service.snmpweb.snmp.zh.com/", className = "com.zh.snmp.snmpclient.generated.GetConfigurationsResponse")
    public List<String> getConfigurations();

    /**
     * 
     * @param nodeId
     * @param arg2
     * @param configPath
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "setDeviceConfig", targetNamespace = "http://service.snmpweb.snmp.zh.com/", className = "com.zh.snmp.snmpclient.generated.SetDeviceConfig")
    @ResponseWrapper(localName = "setDeviceConfigResponse", targetNamespace = "http://service.snmpweb.snmp.zh.com/", className = "com.zh.snmp.snmpclient.generated.SetDeviceConfigResponse")
    public Boolean setDeviceConfig(
        @WebParam(name = "nodeId", targetNamespace = "")
        String nodeId,
        @WebParam(name = "configPath", targetNamespace = "")
        String configPath,
        @WebParam(name = "arg2", targetNamespace = "")
        int arg2);

    /**
     * 
     * @param nodeId
     * @param arg2
     * @param configPath
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "setDinamicConfigValue", targetNamespace = "http://service.snmpweb.snmp.zh.com/", className = "com.zh.snmp.snmpclient.generated.SetDeviceConfig")
    @ResponseWrapper(localName = "setDinamicConfigValueResponse", targetNamespace = "http://service.snmpweb.snmp.zh.com/", className = "com.zh.snmp.snmpclient.generated.SetDeviceConfigResponse")
    public Boolean setDinamicConfigValue(
        @WebParam(name = "nodeId", targetNamespace = "")
        String nodeId,
        @WebParam(name = "configPath", targetNamespace = "")
        String configPath,
        @WebParam(name = "arg2", targetNamespace = "")
        int arg2);

    /**
     * 
     * @param nodeId
     * @param configCode
     * @param macAddress
     * @param ipAddress
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "createDevice", targetNamespace = "http://service.snmpweb.snmp.zh.com/", className = "com.zh.snmp.snmpclient.generated.CreateDevice")
    @ResponseWrapper(localName = "createDeviceResponse", targetNamespace = "http://service.snmpweb.snmp.zh.com/", className = "com.zh.snmp.snmpclient.generated.CreateDeviceResponse")
    public Boolean createDevice(
        @WebParam(name = "configCode", targetNamespace = "")
        String configCode,
        @WebParam(name = "nodeId", targetNamespace = "")
        String nodeId,
        @WebParam(name = "ipAddress", targetNamespace = "")
        String ipAddress,
        @WebParam(name = "macAddress", targetNamespace = "")
        String macAddress);

}
