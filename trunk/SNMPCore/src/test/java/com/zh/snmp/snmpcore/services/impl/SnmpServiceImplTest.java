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
package com.zh.snmp.snmpcore.services.impl;

import com.zh.snmp.snmpcore.BaseTest;
import com.zh.snmp.snmpcore.entities.ClientEntity;
import com.zh.snmp.snmpcore.entities.HistoryEntity;
import com.zh.snmp.snmpcore.entities.SnmpTypeEntity;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;

/**
 *
 * @author Golyo
 */
public class SnmpServiceImplTest extends BaseTest {
    
    @Autowired
    private SnmpService snmpService;
    
    public SnmpServiceImplTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSnmpService() {
        SnmpTypeEntity type = new SnmpTypeEntity();
        type.setActive(true);
        type.setName("testName");
        type.setCode("testCode");
        type.setSnmpDescriptor("testDescriptor");
        SnmpTypeEntity saved = snmpService.saveSnmpType(type);
        assertNotNull(saved.getId());
        
        SnmpTypeEntity check = snmpService.findSnmpTypeById(saved.getId());
        assertNotNull(check);
        
        List<SnmpTypeEntity> ls = snmpService.findSnmpTypesByFilter(new SnmpTypeEntity(), null, 0, -1);
        assertEquals(ls.size(), 1);
        
        check = snmpService.findSnmpTypeByCode(type.getCode());
        assertNotNull(check);
        
        String ip = "ip";
        ClientEntity client = snmpService.setSnmpTypeToClient(ip, "unknowType");
        assertNull(client);
        
        client = snmpService.setSnmpTypeToClient(ip, type.getCode());
        assertNotNull(client);
        assertEquals(client.getIpAddress(), ip);
        
        List<HistoryEntity> histories = snmpService.getClientHistory(new HistoryEntity(), null, 0, -1);
        assertEquals(histories.size(), 1);
        
        client = snmpService.setSnmpTypeToClient(ip, null);
        assertNotNull(client);
        assertNull(client.getType());

        histories = snmpService.getClientHistory(new HistoryEntity(), null, 0, -1);
        assertEquals(histories.size(), 2);
    }

}
