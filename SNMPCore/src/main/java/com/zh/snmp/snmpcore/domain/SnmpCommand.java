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
    private List<OidCommand> commands;

    @XmlElement(name="variable")
    public List<OidCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<OidCommand> commands) {
        this.commands = commands;
    }

    @XmlAttribute
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public SnmpCommand clone(boolean onlyDinamic) {
        SnmpCommand ret = new SnmpCommand();
        ret.setPriority(priority);
        if (commands != null) {
            List<OidCommand> cloneCmds = new LinkedList<OidCommand>();
            for (OidCommand cmd: commands) {
                if (cmd.isIsDinamic()) {
                    cloneCmds.add(cmd);                    
                }
            }  
            if (!cloneCmds.isEmpty()) {
                ret.setCommands(cloneCmds);                
            }
        }
        return ret;
    }
    
    @Override
    public int compareTo(SnmpCommand command) {
        return priority - command.priority;
    }
}
