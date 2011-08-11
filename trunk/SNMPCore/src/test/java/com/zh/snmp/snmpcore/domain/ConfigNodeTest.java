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

import com.zh.snmp.snmpcore.domain.OidCommand;
import com.zh.snmp.snmpcore.domain.SnmpCommand;
import com.zh.snmp.snmpcore.domain.ConfigNode;
import com.zh.snmp.snmpcore.domain.OidType;
import com.zh.snmp.snmpcore.snmp.UpgradeConfig;
import com.zh.snmp.snmpcore.util.JAXBUtil;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
public class ConfigNodeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigNodeTest.class);
    
    //@Test
    public void testMarshaller() {
        //ConfigNode testNode = createConfigNode("X", 2, 2);
        ConfigNode testNode = createAccesNode();
        StringWriter sw = new StringWriter();
        
        JAXBUtil.marshal(testNode, sw, true);
        LOGGER.debug("XXXXXXXXXXXXXXXXXXX");
        LOGGER.debug(sw.toString());
        
        ConfigNode test = JAXBUtil.unmarshalTyped(new StringReader(sw.toString()), ConfigNode.class);
        
        Variable v = OctetString.fromHexString("80 80", ' ');
        System.out.println("+++++++++++++++++++++++++++++" + v);
    }
    
    //@Test
    public void testParse() throws UnsupportedEncodingException {
        InputStream stream = ConfigNodeTest.class.getResourceAsStream("accessConfig.xml");
        assertNotNull(stream);
        ConfigNode node = JAXBUtil.unmarshalTyped(new InputStreamReader(stream, "UTF8"), ConfigNode.class);
        LOGGER.debug("+++" + node.toString());
    }
    
    @Test
    public void testUpgrade()  throws UnsupportedEncodingException {
        InputStream stream = UpgradeConfig.class.getResourceAsStream("upgradeConfig.xml");
        assertNotNull(stream);
        ConfigNode node = JAXBUtil.unmarshalTyped(new InputStreamReader(stream, "UTF8"), ConfigNode.class);
        LOGGER.debug("+++" + node.getCommands().get(0).isPreCondition());
        node.getCommands().get(0).setPreCondition(true);
        StringWriter sw = new StringWriter();
        
        JAXBUtil.marshal(node, sw, true);
        LOGGER.debug("XXXXXXXXXXXXXXXXXXX");
        
        
    }
    
    //@Test
    public void testOidMarshaller() {
        SnmpCommand scmd = new SnmpCommand();
        OidCommand cmd = new OidCommand();
        cmd.setName("testName");
        cmd.setType(OidType.INT);
        cmd.setValueConverter(ValueChecker.SHA1);
        cmd.setValue("00");
        
        scmd.setName("test");
        scmd.setCommands(Arrays.asList(cmd));
        
        String snmpxml = JAXBUtil.marshal(scmd, true);

        LOGGER.debug("+++" + snmpxml.toString());
        
        SnmpCommand parsed = JAXBUtil.unmarshalTyped(new StringReader(snmpxml), SnmpCommand.class);
        int i = 0;
        int j = i;
    }
    /*
    private ConfigNode createConfigNode(String prefix, int childNo, int depth) {
        ConfigNode node = new ConfigNode();
        node.setCode(prefix);
        node.setDescription(prefix + " desc");
        node.setCommands(createSnmpCommand(prefix, 2));
        if (depth > 0) {
            List<ConfigNode> children = new LinkedList<ConfigNode>();
            for (int i=0; i<childNo; i++) {
                children.add(createConfigNode(prefix + "_" + depth, childNo, depth-1));
            }            
            node.setChildren(children);
        }
        return node;
    }
    */
    /*
    private List<SnmpCommand> createSnmpCommand(String prefix, int no) {
        List<SnmpCommand> ret = new LinkedList<SnmpCommand>();

        for (int i=0; i<no; i++) {
            SnmpCommand command = new SnmpCommand();
            command.setBefore(createOids(prefix, 1));
            command.setAfter(createOids(prefix, 1));
            command.setCommands(createOids(prefix, no));
            ret.add(command);
        }
        return ret;
    }
    */
    
    private List<OidCommand> createOids(String prefix, int no) {
        List<OidCommand> oids = new LinkedList<OidCommand>();
        for (int i=0; i<no; i++) {
            OidCommand cmd = new OidCommand();
            cmd.setName("oidname" + prefix + "_" + i);
            cmd.setOid("oidkey" + prefix + "_" + i);
            cmd.setType(OidType.STR);
            cmd.setValue("oidVal" + prefix + "_" + i);
            oids.add(cmd);
        }
        return oids;
    }
    public ConfigNode createAccesNode() {
        ConfigNode acces = new ConfigNode();
        acces.setCode("acces");
        acces.setDescription("acces konfiguráció");
        acces.setCommands(createAccesCommands());
        
        List<ConfigNode> accesChildren = new LinkedList<ConfigNode>();
        ConfigNode internet = new ConfigNode();
        internet.setCode("internet");
        accesChildren.add(internet);
        ConfigNode catv = new ConfigNode();
        catv.setCode("catv");
        accesChildren.add(catv);
        acces.setChildren(accesChildren);
        
        
        return acces;
    }
    
    private List<SnmpCommand> createAccesCommands() {
        List<SnmpCommand> commands = new LinkedList<SnmpCommand>();
        commands.add(createCommand(OidType.INT, "PACKETFRONT-DRG-MIB:snmpAtomicSet.0", "2", 1));
        commands.add(createCommand(OidType.INT, "PACKETFRONT-DRG-MIB:snmpAtomicSet.0", "3", 2));
        return commands;
    }

    private SnmpCommand createCommand(OidType type, String descriptor, String value, int priority) {
        SnmpCommand command = new SnmpCommand();
        command.setPriority(priority);
        command.setCommands(createOids("tt", 3));
        command.setBefore(createOids("bt", 1));
        command.setAfter(createOids("at", 1));
        return command;
    }
}
