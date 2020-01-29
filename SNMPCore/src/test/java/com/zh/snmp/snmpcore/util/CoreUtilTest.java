package com.zh.snmp.snmpcore.util;

import com.zh.snmp.snmpcore.domain.ValueChecker;
import com.zh.snmp.snmpcore.domain.OidType;
import com.zh.snmp.snmpcore.snmp.UpgradeConfig;
import java.io.File;
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
    public void testOctetString() throws Exception {
        String start = "a0:a0";
        Variable val = OidType.HEX.createVariable(start);
        LOGGER.debug(val + ": '" + val.toString() + "'");
        
        String start2 = "7e7f3abee8044186775006983fc5900e71b2e4a6";
        OctetString val2 = OctetString.fromString(start2, 16);
        LOGGER.debug(val2 + ": '" + val2.toHexString() + "'");
    }
    
    @Test
    public void testUserHome() {
        String userHome = System.getProperty("user.home");
        File f = new File(userHome);
        System.out.println(f.getAbsolutePath());
        File snmp = new File(f, ".snmp");   
        System.out.println(f.exists() + "; " + snmp.exists() + "; " + userHome);
        
        System.out.println(snmp.getAbsolutePath());
    }
}
