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
package com.zh.snmp.snmpcore.domain;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

/**
 *
 * @author Golyo
 */
@XmlRootElement(name="oid")
public class OidCommand implements Serializable, Cloneable {    
    private String name;
    private String oid;
    private OidType type;
    private String value;
    private String expectedValue;
    private String dinamicName;
    private ValueConverter valueConverter;
    
    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    @XmlAttribute
    public OidType getType() {
        return type;
    }

    public void setType(OidType type) {
        this.type = type;
    }
    
    @XmlAttribute
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @XmlTransient
    public boolean isDinamic() {        
        return dinamicName != null;
    }

    @XmlAttribute
    public String getDinamicName() {
        return dinamicName;
    }

    public void setDinamicName(String dinamicName) {
        this.dinamicName = dinamicName;
    }

    @XmlAttribute
    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    @XmlAttribute
    public ValueConverter getValueConverter() {
        return valueConverter;
    }

    public void setValueConverter(ValueConverter valueConverter) {
        this.valueConverter = valueConverter;
    }

    public VariableBinding createVariable() {
        return new VariableBinding(new OID(oid), type.createVariable(value));
    }
    
    public boolean equalsVariable(VariableBinding variable) {
        if (oid.equals(variable.getOid().toString())) {
            String expected = expectedValue != null ? expectedValue : value;
            if (valueConverter != null) {
                expected = valueConverter.convert(value);
            }
            return expected.equals(variable.toValueString());           
            //Extras
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return name + " (" + oid + "): " + value;
    }
    
    @Override
    public OidCommand clone() {
        OidCommand cmd = new OidCommand();
        cmd.setDinamicName(dinamicName);
        cmd.setName(name);
        cmd.setOid(oid);
        cmd.setType(type);
        cmd.setValue(value);
        cmd.setExpectedValue(expectedValue);
        cmd.setValueConverter(valueConverter);
        return cmd;
    }    
}
