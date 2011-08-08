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
package com.zh.snmp.snmpcore.util;

import com.zh.snmp.snmpcore.domain.ValueConverter;
import com.zh.snmp.snmpcore.domain.OidType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import static org.junit.Assert.*;

/**
 *
 * @author Golyo
 */
public class CoreUtilTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreUtilTest.class);
    
    public CoreUtilTest() {
    }
    /**
     * Test of sha1 method, of class CoreUtil.
     */
    @Test
    public void testSha1() throws Exception {
        String pwd = "3618782825";
        String ret = ValueConverter.SHA1.convert(pwd);
        LOGGER.debug(pwd + ": '" + ret + "'");
    }
    
    @Test
    public void testOctetString() throws Exception {
        String start = "a0:a0";
        Variable val = OidType.HEX.createVariable(start);
        LOGGER.debug(val + ": '" + val.toString() + "'");
        
        String start2 = "7e7f3abee8044186775006983fc5900e71b2e4a6";
        OctetString val2 = OctetString.fromString(start2, 16);
        LOGGER.debug(val2 + ": '" + val2.toHexString() + "'");
    }
}
