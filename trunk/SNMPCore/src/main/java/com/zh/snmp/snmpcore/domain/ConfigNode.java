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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Golyo
 */
@XmlRootElement(name = "config")
@XmlSeeAlso({
    SnmpCommand.class
})
public class ConfigNode extends DefaultNode implements Serializable {
    private String code;
    private String description;
    private List<SnmpCommand> commands;
    private Boolean restartDevice;

    @XmlElement(name = "node")
    public List<ConfigNode> getChildren() {
        return (List<ConfigNode>)children;
    }

    public void setChildren(List<ConfigNode> children) {
        this.children = children;
    }

    @XmlAttribute
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlElement
    public List<SnmpCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<SnmpCommand> commands) {
        this.commands = commands;
    }

    @XmlAttribute
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }    
    
    @XmlTransient
    public boolean isEmpty() {
        return children == null || children.isEmpty();
    }
    
    public ConfigNode findChildByCode(String code) {
        if (children != null) {
            for (ConfigNode node: getChildren()) {
                if (code.equals(node.getCode())) {
                    return node;
                }
            }            
        }
        return null;
    }
    
    @Override
    public String toString() {
        return code + (description != null ? " - " + description : "");
    }
    
    public ConfigNode findChildByPath(LinkedList<String> path) {
        if (path.isEmpty()) {
            return null;
        }
        String val = path.pop();
        ConfigNode child = findChildByCode(val);
        if (child != null) {
            if (path.isEmpty()) {
                return child;
            } else {
                return child.findChildByPath(path);                
            }
        }
        return null;
    }
    
    @XmlTransient
    public List<DinamicValue> getDinamics() {
        List<DinamicValue> ret = new LinkedList<DinamicValue>();
        if (commands != null) {
            for (SnmpCommand cmd: commands) {
                for (OidCommand oidc: cmd.getCommands()) {
                    if (oidc.isDinamic()) {
                        ret.add(new DinamicValue(oidc.getDinamicName(), oidc.getValue()));
                    }
                }
            }            
        }
        return ret;
    }
    
    @XmlAttribute
    public Boolean isRestartDevice() {
        return restartDevice;
    }

    public void setRestartDevice(Boolean restartDevice) {
        this.restartDevice = restartDevice;
    }
    
    public SnmpCommand findCommand(int priority) {
        for (SnmpCommand cmd: commands) {
            if (cmd.getPriority() == priority) {
                return cmd;
            }
        }
        return null;
    }
    
    /*
    public void appendCommands(CommandAppender appender, DeviceMap map) {
        if (commands != null) {
            //Maybe not here
            Collections.sort(commands);
            appender.appendCommand(commands);
        }
        if (map != null) {
            for (DeviceMap child: map.getChildren()) {
                ConfigNode confChild = findChildByCode(child.getCode());
                if (confChild == null) {
                    throw new SystemException(ExceptionCodesEnum.Unsupported);
                }                
                confChild.appendCommands(appender, child);
            }
        }        
    }   
    */

}
