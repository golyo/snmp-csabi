
package com.zh.snmp.snmpclient.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deviceState.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="deviceState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NOT_FOUND"/>
 *     &lt;enumeration value="NEW"/>
 *     &lt;enumeration value="RUNNING"/>
 *     &lt;enumeration value="CONFIGURED"/>
 *     &lt;enumeration value="ERROR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "deviceState")
@XmlEnum
public enum DeviceState {

    NOT_FOUND,
    NEW,
    RUNNING,
    CONFIGURED,
    ERROR;

    public String value() {
        return name();
    }

    public static DeviceState fromValue(String v) {
        return valueOf(v);
    }

}
