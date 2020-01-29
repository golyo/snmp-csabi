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
public class SnmpCommand implements Serializable, Comparable<SnmpCommand>, Cloneable {
    private int priority;
    private String name;
    private boolean preCondition;
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

    @XmlAttribute
    public boolean isPreCondition() {
        return preCondition;
    }

    public void setPreCondition(boolean preCondition) {
        this.preCondition = preCondition;
    }
    
    @Override
    public SnmpCommand clone() {
        SnmpCommand ret = cloneEmpty();        
        ret.setCommands(cloneCommands(commands));
        return ret;
    }
    
    public SnmpCommand cloneEmpty() {
        SnmpCommand ret = new SnmpCommand();
        ret.setBefore(cloneCommands(before));
        ret.setAfter(cloneCommands(after));
        ret.setPriority(priority);
        ret.setName(name);
        ret.setPreCondition(preCondition);
        return ret;
    }
    
    @Override
    public int compareTo(SnmpCommand command) {
        return priority - command.priority;
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
    
    public OidCommand setNewOidValue(OidCommand cmd) {
        for (OidCommand oids: commands) {
            if (oids.getOid().equals(cmd.getOid())) {
                oids.setValue(cmd.getValue());
                return oids;
            }
        }
        return null;
    }
}
