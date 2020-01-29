package com.zh.snmp.snmpcore.domain;

import com.zh.snmp.snmpcore.exception.ExceptionCodesEnum;
import com.zh.snmp.snmpcore.exception.SystemException;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

/**
 *
 * @author Golyo
 */
public enum OidType {
    INT,
    STR,
    HEX;
    public Variable createVariable(String value) {
        return createVariable(this, value);
    }
    
    private static final char HEX_DELIM = ':';
    
    private static Variable createVariable(OidType type, String value) {
        switch (type) {
            case HEX: 
                return OctetString.fromHexString(value, HEX_DELIM);
            case STR: 
                return new OctetString(value);
            case INT: 
                return new Integer32(Integer.parseInt(value));
            default:
                throw new SystemException(ExceptionCodesEnum.Unsupported);
        }
    }    
}

