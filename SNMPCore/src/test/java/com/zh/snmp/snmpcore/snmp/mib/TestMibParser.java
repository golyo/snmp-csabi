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
package com.zh.snmp.snmpcore.snmp.mib;

import com.zh.snmp.snmpcore.snmp.SnmpCommandResult;
import com.zh.snmp.snmpcore.snmp.SnmpCommand;
import com.zh.snmp.snmpcore.snmp.SnmpFactory;
import com.zh.snmp.snmpcore.BaseTest;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.message.SimpleMessageAppender;
import com.zh.snmp.snmpcore.snmp.SnmpCommandManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 *
 * @author Golyo
 */
public class TestMibParser extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestMibParser.class); 
    //@Autowired
    //private SmiMib m_mib;
    
    @Autowired
    private MibParser parser;
    /*
    @Test
    public void testMibs() {
        //PACKETFRONT-DRG-MIB:snmpAtomicSet
        //PACKETFRONT-DRG-MIB:vlanStaticEgressPorts = 1.3.6.1.4.1.9303.4.3.1.8.3.1.1.5
        //PACKETFRONT-DRG-MIB:vlanStaticName = 1.3.6.1.4.1.9303.4.3.1.8.3.1.1.2
        //PACKETFRONT-DRG-MIB:vlanStaticRowStatus
        SmiModule module = getMib().findModule("PACKETFRONT-DRG-MIB");
        SmiOidValue sim = module.findOidValue("vlanStaticName");
        System.out.println("NODE: " + sim.getNode());
        System.out.println("OID NODE: " + sim.getOidStr());
        
    }
     * 
     */
    
    private static final String START_STATE = "PACKETFRONT-DRG-MIB:snmpAtomicSet.0 i 1";
    private static final String MOD_STATE = "PACKETFRONT-DRG-MIB:snmpAtomicSet.0 i 2";
    private static final String END_STATE = "PACKETFRONT-DRG-MIB:snmpAtomicSet.0 i 3";
    //@Test
    public void testParser() throws IOException {
        MessageAppender appender = new SimpleMessageAppender();
        Snmp snmp = SnmpFactory.createSnmp();
        SnmpCommandManager manager = new SnmpCommandManager(snmp, appender);
        CommunityTarget writeTarget = DeviceSettings.TEST_DEVICE.createTarget(true);
        CommunityTarget readTarget = DeviceSettings.TEST_DEVICE.createTarget(false);
        SnmpCommandResult result; 
        SnmpCommand command;
        
        command = parseFirstCommand(START_STATE);
        result = manager.checkCommand(DeviceSettings.TEST_DEVICE.createTarget(false), command);
        assertEquals(result.getType(), SnmpCommandResult.ResultType.SKIPED);
        
        command = parseFirstCommand(MOD_STATE);
        result = manager.checkCommand(readTarget, command);
        assertEquals(result.getType(), SnmpCommandResult.ResultType.SUCCES);
        result = manager.processCommand(writeTarget, command);
        assertEquals(result.getType(), SnmpCommandResult.ResultType.SUCCES);

        command = parseFirstCommand(START_STATE);
        result = manager.checkCommand(readTarget, command);
        assertEquals(result.getType(), SnmpCommandResult.ResultType.SUCCES);
        result = manager.processCommand(writeTarget, command);
        assertEquals(result.getType(), SnmpCommandResult.ResultType.FAILED);
        
        command = parseFirstCommand(END_STATE);
        result = manager.checkCommand(readTarget, command);
        assertEquals(result.getType(), SnmpCommandResult.ResultType.SUCCES);
        result = manager.processCommand(writeTarget, command);
        assertEquals(result.getType(), SnmpCommandResult.ResultType.SUCCES);

        snmp.close();
    }
    
    private static final String TEST_CATV = "PACKETFRONT-CATV-MIB::catvModuleAdminStatus.0";
    
    @Test
    public void testConfig() throws IOException {
        MessageAppender appender = new SimpleMessageAppender();
        Snmp snmp = SnmpFactory.createSnmp();
        SnmpCommandManager manager = new SnmpCommandManager(snmp, appender);
        
        CommunityTarget writeTarget = DeviceSettings.TEST_DEVICE.createTarget(true);
        CommunityTarget readTarget = DeviceSettings.TEST_DEVICE.createTarget(false);
        
        List<SnmpCommand> commands = parseCommands();
        boolean first = true;
        for (SnmpCommand c: commands) {
            LOGGER.debug("++++++++++++++++++++++++++++++++COMMAND");
            LOGGER.debug(c.toString());
            SnmpCommandResult readResult = manager.checkCommand(readTarget, c);
            LOGGER.debug("READ: " + readResult);
            if (readResult.getType() == SnmpCommandResult.ResultType.SUCCES) {
                SnmpCommandResult result = manager.processCommand(writeTarget, c);
                LOGGER.debug("WRITE: " + result.toString());

            }
        }
    }
    
    private SnmpCommand parseFirstCommand(String commandStr) throws IOException {
        StringReader sr = new StringReader(commandStr);
        List<SnmpCommand> commands = parser.parseCommands(sr);
        assertNotNull(commands);
        assertEquals(commands.size(), 1);        
        return commands.get(0);
        
    }
    
    private List<SnmpCommand> parseCommands() throws IOException {
        InputStream is = MibParser.class.getResourceAsStream(TEST_FILE);
        assertNotNull(is);
        InputStreamReader reader = new InputStreamReader(is);
        List<SnmpCommand> commands = parser.parseCommands(reader);
        reader.close();
        assertNotNull(commands);
        return commands;        
    }
}
