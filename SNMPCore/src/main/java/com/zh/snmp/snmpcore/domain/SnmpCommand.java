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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Golyo
 */
@XmlRootElement(name="command")
@XmlSeeAlso({
    OidCommand.class
})
public class SnmpCommand implements Serializable, Comparable<SnmpCommand> {
    private int priority;
    private String name;
    private List<OidCommand> commands;
    private List<OidCommand> before;
    private List<OidCommand> after;
    
    public SnmpCommand() {
        commands = new LinkedList<OidCommand>();
    }
    
    @XmlElement(name="variable")
    public List<OidCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<OidCommand> commands) {
        this.commands = commands;
    }

    @XmlElement(name="after")
    public List<OidCommand> getAfter() {
        return after;
    }

    public void setAfter(List<OidCommand> after) {
        this.after = after;
    }

    @XmlElement(name="before")
    public List<OidCommand> getBefore() {
        return before;
    }

    public void setBefore(List<OidCommand> before) {
        this.before = before;
    }

    @XmlAttribute
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /*
    public SnmpCommand clone(boolean onlyDinamic) {
        SnmpCommand ret = cloneEmpty();
        for (OidCommand cmd: commands) {
            if (cmd.isIsDinamic()) {
                ret.commands.add(cmd);                    
            }
        }  
        return ret;
    }
    */
    public SnmpCommand cloneEmpty() {
        SnmpCommand ret = new SnmpCommand();
        ret.setBefore(cloneCommands(before));
        ret.setAfter(cloneCommands(after));
        ret.setPriority(priority);
        ret.setName(name);
        return ret;
    }
    
    @Override
    public int compareTo(SnmpCommand command) {
        return priority - command.priority;
    }    
    
    public void updateCommandValues(SnmpCommand mergeCmd) {
        if (mergeCmd.getPriority() == priority) {
            for(OidCommand mergeOid: mergeCmd.commands) {
                if (!setNewOidValue(mergeOid)) {
                    commands.add(mergeOid.clone());
                }
            }            
        }
    }
    
    @Override
    public String toString() {
        return priority + ": " + commands.toString();
    }
    
    private List<OidCommand> cloneCommands(List<OidCommand> cloneable) {
        if (cloneable == null) {
            return null;
        } else {
            List<OidCommand> ret = new LinkedList<OidCommand>();
            for (OidCommand cmd: cloneable) {
                ret.add(cmd.clone());
            }
            return ret;
        }
    }
    
    private boolean setNewOidValue(OidCommand cmd) {
        int idx = 0;
        for (OidCommand oids: commands) {
            if (oids.getOid().equals(cmd.getOid())) {
                oids.setValue(cmd.getValue());
                return true;
            }
            idx++;
        }
        return false;
    }
}
