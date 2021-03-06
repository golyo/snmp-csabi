package com.zh.snmp.snmpcore.domain;

import com.zh.snmp.snmpcore.exception.ExceptionCodesEnum;
import com.zh.snmp.snmpcore.exception.SystemException;
import com.zh.snmp.snmpcore.util.CoreUtil;
import org.snmp4j.smi.OctetString;

/**
 *
 * @author Golyo
 */
public enum ValueChecker {
    SHA1(new Sha1Checker()),
    FAIL(new FailChecker());
    
    private IValueChecker checker;
    private ValueChecker(IValueChecker checker) {
        this.checker = checker;
    }
    
    public boolean check(String value, String snmpValue) {
        return checker.check(value, snmpValue);
    }   

    public static interface IValueChecker {
        public boolean check(String value, String snmpValue);
    }
    
    private static class Sha1Checker implements IValueChecker {
        @Override
        public boolean check(String value, String snmpValue) {
            try {
                String expected =  CoreUtil.sha1(value);
                expected = OctetString.fromString(expected, 16).toHexString();
                return expected.equals(snmpValue);
            } catch (Exception e) {
                throw new SystemException(ExceptionCodesEnum.ConfigurationException, e);
            }            
        }
    }
    
    private static class FailChecker implements IValueChecker {
        @Override
        public boolean check(String value, String snmpValue) {
            if (value.equals(snmpValue)) {
                return true;
            } else {
                throw new SystemException(ExceptionCodesEnum.WrongDeviceType, "Bad device configuration type");
            }
        }
        
    }
}
