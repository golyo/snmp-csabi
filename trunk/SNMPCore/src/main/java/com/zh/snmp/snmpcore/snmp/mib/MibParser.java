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

import com.zh.snmp.snmpcore.snmp.SnmpCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import org.jsmiparser.smi.SmiMib;
import org.jsmiparser.smi.SmiModule;
import org.jsmiparser.smi.SmiOidValue;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class MibParser {
    //private static final Logger LOGGER = LoggerFactory.getLogger(MibParser.class);
    
    @Autowired
    private SmiMib mibMap;
    
    private static final char PACKAGE_DELIMITER = ':';
    private static final char OID_DELIMITER = '.';
    private static final char NEW_COMMANDS_CHAR = '*';
    private static final String COMMAND_SPLITTER = " ";
    
    public List<SnmpCommand> parseCommands(Reader reader) throws IOException {
        String line;
        BufferedReader lineReader = new BufferedReader(reader);
        List<SnmpCommand> commands = new LinkedList<SnmpCommand>();
        SnmpCommand act = null;
        boolean isNew = true;
        while ((line = lineReader.readLine()) != null) {
            if (isNewCommand(line)) {
                line = line.substring(1);
                isNew = true;
            }
            if (isNew) {
                act = new SnmpCommand();
                commands.add(act);
                isNew = false;
            }
            act.addBinding(parseLine(line));
        }
        return commands;
    } 
    
    protected boolean isNewCommand(String line) {
        return line.charAt(0) == NEW_COMMANDS_CHAR;
    }
    
    protected VariableBinding parseLine(String line) throws IOException {
        String[] vals = line.split(COMMAND_SPLITTER);
        if (vals.length != 3) {
            throw new IOException("The line must contain mibDescription, type and value segments");
        }
        OID oid = parseMib(vals[0]); 
        Variable var = parseVariable(vals[1], vals[2]);
        //LOGGER.debug("Parse line succes to " + line + " :" + oid + ", " + var);
        return new VariableBinding(oid, var);
    }
    
    protected OID parseMib(String mibDescription) throws IOException {
        int p = mibDescription.indexOf(PACKAGE_DELIMITER);
        if (p<0) {
            throw new IOException("Not found package delimiter in line " + mibDescription);
        }
        String pack = mibDescription.substring(0, p);
        String oidvar = mibDescription.substring(p+1);
        String oidPostFix = null;
        p = oidvar.indexOf(OID_DELIMITER);
        if (p >= 0) {
            oidPostFix = oidvar.substring(p); 
            oidvar = oidvar.substring(0, p);
        }
        SmiModule module = mibMap.findModule(pack);
        if (module == null) {
            throw new IOException("Mib package not defined '" + pack + "'");
        }
        SmiOidValue oid = module.findOidValue(oidvar);
        if (oid == null) {
            throw new IOException("Mib variable not defined '" + oidvar + "' in package " + pack);
        }
        String oidVal = oid.getOidStr();
        if (oidPostFix != null) {
            oidVal += oidPostFix;
        }
        //LOGGER.debug("Parse oid succes to " + mibDescription + " :" + oidVal);
        return new OID(oidVal);        
        
    }
    
    private static final String INTEGER32 = "i";
    private static final String HEX_STRING = "x";
    private static final String STRING = "s";
    private static final char QUOT = '"';
    protected static Variable parseVariable(String type, String value) throws IOException {
        if (INTEGER32.equals(type)) {
            return new Integer32(Integer.valueOf(value));
        } else if (HEX_STRING.equals(type)) {
            value = cutQuote(value);
            return OctetString.fromHexString(value);
        } else if (STRING.equals(type)) {
            value = cutQuote(value);
            return new OctetString(value);
        } else {
            throw new IOException("Unknown type " + type);
        }
    }    
    
    private static String cutQuote(String value) throws IOException {
        if (value.charAt(0) == QUOT) {
            if (value.charAt(value.length()-1) != QUOT) {
                throw new IOException("Quote not found end of value " + value);
            }
            return value.substring(1, value.length()-1);
        } else {
            return value;
        }        
    }
}
