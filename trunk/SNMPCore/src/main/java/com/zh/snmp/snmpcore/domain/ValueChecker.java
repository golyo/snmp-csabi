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
