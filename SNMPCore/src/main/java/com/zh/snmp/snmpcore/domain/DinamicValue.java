package com.zh.snmp.snmpcore.domain;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Golyo
 */
@XmlRootElement(name="dinamic")
public class DinamicValue implements Serializable, Cloneable {
    private String code;
    private String value;

    public DinamicValue() {
        
    }
    
    public DinamicValue(String code) {
        this.code = code;
    }
    
    public DinamicValue(String code, String value) {
        this.code = code;
        this.value = value;
    }
    
    @XmlAttribute
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlAttribute
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public DinamicValue clone() {
        return new DinamicValue(code, value);
    }
}
