package com.zh.snmp.snmpcore.services.impl;

import com.zh.snmp.snmpcore.BaseTest;
import com.zh.snmp.snmpcore.entities.UserEntity;
import com.zh.snmp.snmpcore.services.AuthenticationService;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;

public class AuthenticationServiceImplTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImplTest.class);
    @Autowired
    private AuthenticationService authService;

    public AuthenticationServiceImplTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getLoggedInPlayer method, of class AuthenticationServiceImpl.
     */
    @Test
    public void testPlayerService() {
        /*
        assertNull(authService.getLoggedInPlayer());
        UserEntity pe = new UserEntity();
        pe.setEmail("testEmail");
        pe.setName("testName");
        assertTrue(authService.register(pe, "testPassword"));
        assertNull(authService.findPlayer("testName", "wrongPassword"));
        assertNull(authService.findPlayer("wrongName", "testPassword"));
        UserEntity pdb = authService.findPlayer("testName", "testPassword");
        //TODO
        //assertNotNull(pdb);
        //assertEquals(pe, pdb);
        LOGGER.info("AuthenticationService test succes");
         * 
         */
    }

    @Test
    public void testFormat() {
    }
}