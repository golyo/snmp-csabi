/*
 *  Copyright 2010 sonrisa.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

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

/**
 *
 * @author sonrisa
 */

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
    }

    @Test
    public void testFormat() {
    }
}