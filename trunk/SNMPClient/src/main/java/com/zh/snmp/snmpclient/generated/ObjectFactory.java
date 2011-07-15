
package com.zh.snmp.snmpclient.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.zh.snmp.snmpclient.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CreateDevice_QNAME = new QName("http://service.snmpweb.snmp.zh.com/", "createDevice");
    private final static QName _GetDeviceState_QNAME = new QName("http://service.snmpweb.snmp.zh.com/", "getDeviceState");
    private final static QName _SetDinamicConfigValueResponse_QNAME = new QName("http://service.snmpweb.snmp.zh.com/", "setDinamicConfigValueResponse");
    private final static QName _SetDinamicConfigValue_QNAME = new QName("http://service.snmpweb.snmp.zh.com/", "setDinamicConfigValue");
    private final static QName _CreateDeviceResponse_QNAME = new QName("http://service.snmpweb.snmp.zh.com/", "createDeviceResponse");
    private final static QName _SetDeviceConfigResponse_QNAME = new QName("http://service.snmpweb.snmp.zh.com/", "setDeviceConfigResponse");
    private final static QName _SetDeviceConfig_QNAME = new QName("http://service.snmpweb.snmp.zh.com/", "setDeviceConfig");
    private final static QName _GetConfigurations_QNAME = new QName("http://service.snmpweb.snmp.zh.com/", "getConfigurations");
    private final static QName _Init_QNAME = new QName("http://service.snmpweb.snmp.zh.com/", "init");
    private final static QName _GetConfigurationsResponse_QNAME = new QName("http://service.snmpweb.snmp.zh.com/", "getConfigurationsResponse");
    private final static QName _InitResponse_QNAME = new QName("http://service.snmpweb.snmp.zh.com/", "initResponse");
    private final static QName _GetDeviceStateResponse_QNAME = new QName("http://service.snmpweb.snmp.zh.com/", "getDeviceStateResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.zh.snmp.snmpclient.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetConfigurations }
     * 
     */
    public GetConfigurations createGetConfigurations() {
        return new GetConfigurations();
    }

    /**
     * Create an instance of {@link SetDeviceConfigResponse }
     * 
     */
    public SetDeviceConfigResponse createSetDeviceConfigResponse() {
        return new SetDeviceConfigResponse();
    }

    /**
     * Create an instance of {@link SetDeviceConfig }
     * 
     */
    public SetDeviceConfig createSetDeviceConfig() {
        return new SetDeviceConfig();
    }

    /**
     * Create an instance of {@link GetConfigurationsResponse }
     * 
     */
    public GetConfigurationsResponse createGetConfigurationsResponse() {
        return new GetConfigurationsResponse();
    }

    /**
     * Create an instance of {@link CreateDeviceResponse }
     * 
     */
    public CreateDeviceResponse createCreateDeviceResponse() {
        return new CreateDeviceResponse();
    }

    /**
     * Create an instance of {@link SetDinamicConfigValueResponse }
     * 
     */
    public SetDinamicConfigValueResponse createSetDinamicConfigValueResponse() {
        return new SetDinamicConfigValueResponse();
    }

    /**
     * Create an instance of {@link Init }
     * 
     */
    public Init createInit() {
        return new Init();
    }

    /**
     * Create an instance of {@link InitResponse }
     * 
     */
    public InitResponse createInitResponse() {
        return new InitResponse();
    }

    /**
     * Create an instance of {@link SetDinamicConfigValue }
     * 
     */
    public SetDinamicConfigValue createSetDinamicConfigValue() {
        return new SetDinamicConfigValue();
    }

    /**
     * Create an instance of {@link GetDeviceStateResponse }
     * 
     */
    public GetDeviceStateResponse createGetDeviceStateResponse() {
        return new GetDeviceStateResponse();
    }

    /**
     * Create an instance of {@link CreateDevice }
     * 
     */
    public CreateDevice createCreateDevice() {
        return new CreateDevice();
    }

    /**
     * Create an instance of {@link GetDeviceState }
     * 
     */
    public GetDeviceState createGetDeviceState() {
        return new GetDeviceState();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateDevice }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.snmpweb.snmp.zh.com/", name = "createDevice")
    public JAXBElement<CreateDevice> createCreateDevice(CreateDevice value) {
        return new JAXBElement<CreateDevice>(_CreateDevice_QNAME, CreateDevice.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDeviceState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.snmpweb.snmp.zh.com/", name = "getDeviceState")
    public JAXBElement<GetDeviceState> createGetDeviceState(GetDeviceState value) {
        return new JAXBElement<GetDeviceState>(_GetDeviceState_QNAME, GetDeviceState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetDinamicConfigValueResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.snmpweb.snmp.zh.com/", name = "setDinamicConfigValueResponse")
    public JAXBElement<SetDinamicConfigValueResponse> createSetDinamicConfigValueResponse(SetDinamicConfigValueResponse value) {
        return new JAXBElement<SetDinamicConfigValueResponse>(_SetDinamicConfigValueResponse_QNAME, SetDinamicConfigValueResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetDinamicConfigValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.snmpweb.snmp.zh.com/", name = "setDinamicConfigValue")
    public JAXBElement<SetDinamicConfigValue> createSetDinamicConfigValue(SetDinamicConfigValue value) {
        return new JAXBElement<SetDinamicConfigValue>(_SetDinamicConfigValue_QNAME, SetDinamicConfigValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateDeviceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.snmpweb.snmp.zh.com/", name = "createDeviceResponse")
    public JAXBElement<CreateDeviceResponse> createCreateDeviceResponse(CreateDeviceResponse value) {
        return new JAXBElement<CreateDeviceResponse>(_CreateDeviceResponse_QNAME, CreateDeviceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetDeviceConfigResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.snmpweb.snmp.zh.com/", name = "setDeviceConfigResponse")
    public JAXBElement<SetDeviceConfigResponse> createSetDeviceConfigResponse(SetDeviceConfigResponse value) {
        return new JAXBElement<SetDeviceConfigResponse>(_SetDeviceConfigResponse_QNAME, SetDeviceConfigResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetDeviceConfig }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.snmpweb.snmp.zh.com/", name = "setDeviceConfig")
    public JAXBElement<SetDeviceConfig> createSetDeviceConfig(SetDeviceConfig value) {
        return new JAXBElement<SetDeviceConfig>(_SetDeviceConfig_QNAME, SetDeviceConfig.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConfigurations }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.snmpweb.snmp.zh.com/", name = "getConfigurations")
    public JAXBElement<GetConfigurations> createGetConfigurations(GetConfigurations value) {
        return new JAXBElement<GetConfigurations>(_GetConfigurations_QNAME, GetConfigurations.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Init }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.snmpweb.snmp.zh.com/", name = "init")
    public JAXBElement<Init> createInit(Init value) {
        return new JAXBElement<Init>(_Init_QNAME, Init.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetConfigurationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.snmpweb.snmp.zh.com/", name = "getConfigurationsResponse")
    public JAXBElement<GetConfigurationsResponse> createGetConfigurationsResponse(GetConfigurationsResponse value) {
        return new JAXBElement<GetConfigurationsResponse>(_GetConfigurationsResponse_QNAME, GetConfigurationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.snmpweb.snmp.zh.com/", name = "initResponse")
    public JAXBElement<InitResponse> createInitResponse(InitResponse value) {
        return new JAXBElement<InitResponse>(_InitResponse_QNAME, InitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDeviceStateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.snmpweb.snmp.zh.com/", name = "getDeviceStateResponse")
    public JAXBElement<GetDeviceStateResponse> createGetDeviceStateResponse(GetDeviceStateResponse value) {
        return new JAXBElement<GetDeviceStateResponse>(_GetDeviceStateResponse_QNAME, GetDeviceStateResponse.class, null, value);
    }

}
