package com.zh.snmp.snmpcore.snmp.mib;

import com.zh.snmp.snmpcore.domain.ConfigNode;
import com.zh.snmp.snmpcore.domain.OidCommand;
import com.zh.snmp.snmpcore.domain.SnmpCommand;
import com.zh.snmp.snmpcore.message.MessageAppender;
import java.io.IOException;
import java.util.List;
import org.jsmiparser.smi.SmiMib;
import org.jsmiparser.smi.SmiModule;
import org.jsmiparser.smi.SmiOidValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(MibParser.class);
    
    @Autowired
    private SmiMib mibMap;
    
    private static final char PACKAGE_DELIMITER = ':';
    private static final char OID_DELIMITER = '.';
    private static final char NEW_COMMANDS_CHAR = '*';
    private static final String COMMAND_SPLITTER = " ";
    /*
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
    */
    private boolean isNewCommand(String line) {
        return line.charAt(0) == NEW_COMMANDS_CHAR;
    }
    
    private VariableBinding parseLine(String line) throws IOException {
        String[] vals = line.split(COMMAND_SPLITTER);
        if (vals.length != 3) {
            throw new IOException("The line must contain mibDescription, type and value segments");
        }
        OID oid = parseMib(vals[0]); 
        Variable var = parseVariable(vals[1], vals[2]);
        //LOGGER.debug("Parse line succes to " + line + " :" + oid + ", " + var);
        return new VariableBinding(oid, var);
    }
    
    
    public boolean parseAndSetMibValues(SnmpCommand command, MessageAppender appender) {
        boolean ret = true;
        if (command.getBefore() != null) {
            ret = parseAndSetOidCommands(command.getBefore(), appender) && ret;
        }
        if (command.getCommands() != null) {
            ret = parseAndSetOidCommands(command.getCommands(), appender) && ret;            
        }
        if (command.getAfter() != null) {
            ret = parseAndSetOidCommands(command.getAfter(), appender) && ret;            
        }
        return ret;
    }
    
    public boolean parseAndSetMibValues(ConfigNode node, MessageAppender appender) {
        List<SnmpCommand> commands = node.getCommands();
        boolean ret = true;
        if (commands != null) {
            for (SnmpCommand command: commands) {
                ret = parseAndSetMibValues(command, appender) && ret;
            }                        
        }
        for (ConfigNode child: node.getChildren()) {
            ret = parseAndSetMibValues(child, appender) && ret;
        }
        return ret;
    }

    private boolean parseAndSetOidCommands(List<OidCommand> oids, MessageAppender appender) {
        boolean ret = true;
        if (oids != null) {
            for (OidCommand oidc: oids) {
                try {
                    OID oid = parseMib(oidc.getName());
                    oidc.setOid(oid.toString());
                } catch (IOException e) {
                    appender.addMessage("error.parse.mib", oidc);
                    LOGGER.error("Error while parse command " + oidc.getName(), e);
                    ret = false;
                }
            }                   
        }
        return ret;        
    }
    
    public OID parseMib(String mibDescription) throws IOException {
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
