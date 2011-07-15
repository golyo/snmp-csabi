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

/**
 *
 * @author Golyo
 */
@XmlRootElement(name="device")
public class DeviceMap extends DefaultNode implements Serializable {
    private String code;

    @XmlElement(name="node")
    public List<DeviceMap> getChildren() {
        return (List<DeviceMap>)children;
    }

    public void setChildren(List<DeviceMap> children) {
        this.children = children;
    }

    @XmlAttribute
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public void setByPath(LinkedList<String> path, int mode) {
        String act = path.pop();
        DeviceMap child = findChild(act);
        if (child == null) {
            if (mode == 1) {
                child = new DeviceMap();
                child.setCode(act);
                if (children == null) {
                    children = new LinkedList<DeviceMap>();                
                }
                getChildren().add(child);                
            }
        } else {
            if (mode == 0 && path.isEmpty()) {
                children.remove(child);
                child = null;
            }
        }
        if (child != null && !path.isEmpty()) {
            child.setByPath(path, mode);
        }
    }
    
    public DeviceMap findChild(String code) {
        if (children != null) {
            for (DeviceMap child: getChildren()) {
                if (code.equals(child.code)) {
                    return child;
                }
            }
        }
        return null;
    }
    
    @Override
    public String toString() {  
        return code;
    }
    
    public String toMultilineString() {
        StringBuilder sb = new StringBuilder();
        append("", sb);
        return sb.toString();        
    }
    
    private static final String NL = "\n";
    private void append(String prefix, StringBuilder sb) {
        sb.append(prefix).append(code).append(NL);
        if (children != null) {
            prefix = prefix + "\t";
            for (DeviceMap dm: getChildren()) {
                dm.append(prefix, sb);
            }
        }
    }
    
    public List<SnmpCommand> cloneCommands(ConfigNode node) {
        List<SnmpCommand> commands = new LinkedList<SnmpCommand>();
        for (SnmpCommand cmd: node.getCommands()) {
            commands.add(cmd);
        }
        return commands;
    }
    
    public void changeConfigCommands(ConfigNode node, List<SnmpCommand> commands) {
        for (DeviceMap child: getChildren()) {
            ConfigNode childConf = node.findChildByCode(child.getCode());
            if (childConf != null) {
                for (SnmpCommand cmd: childConf.getCommands()) {
                    
                }
            }
        }
    }
}
