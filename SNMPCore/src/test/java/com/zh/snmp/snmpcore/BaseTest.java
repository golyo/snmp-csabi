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

package com.zh.snmp.snmpcore;

import java.sql.SQLException;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sonrisa
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:applicationContext.xml",
    "classpath:dao-applicationContext.xml",
    "classpath:test-dao-partial-applicationContext.xml",
    "classpath:security-applicationContext.xml",
    "classpath:service-applicationContext.xml"})
@Transactional
public abstract class BaseTest {
    @BeforeClass
    public static void setUpBaseClass() throws NamingException, SQLException {
    }


    @Before
    public void setUpBase() {
        System.out.println("beforebase");
    }

    @After
    public void tearDownBase() {
    }    
}
