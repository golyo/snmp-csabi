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
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Golyo
 */
@XmlRootElement(name="deviceConfig")
public class DeviceNode extends DefaultNode implements Serializable {
    private boolean selected;
    private String code;
    private List<DinamicValue> dinamics;
    
    public DeviceNode() {
        super();
        dinamics = new LinkedList<DinamicValue>();
    }
    
    public DeviceNode(ConfigNode node) {
        this();
        code = node.getCode();
        dinamics = node.getDinamics();
        for (ConfigNode child: node.getChildren()) {
            getChildren().add(new DeviceNode(child));
        }
    }
        
    @XmlAttribute
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            if (parent != null) {
                ((DeviceNode)parent).setSelected(selected);
            }            
        } else {
            for (DefaultNode child: children) {
                ((DeviceNode)child).setSelected(selected);
            }
        }
    }

    @XmlAttribute
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlElement(name="deviceNode")
    public List<DeviceNode> getChildren() {
        return (List<DeviceNode>)children;
    }

    public void setChildren(List<DeviceNode> children) {
        this.children = children;
    }
   
    @XmlElement(name="dinamic")
    public List<DinamicValue> getDinamics() {
        return dinamics;
    }

    public void setDinamics(List<DinamicValue> dinamics) {
        this.dinamics = dinamics;
    }
    
    public DeviceNode findChild(String code) {
        for (DeviceNode child: getChildren()) {
            if (child.getCode().equals(code)) {
                return child;
            }
        }
        return null;
    }
    
    public DeviceNode findChainChild(Deque<String> codes) {
        String find = codes.pop();
        DeviceNode node = findChild(find);
        if (codes.isEmpty()) {
            return node;
        } else if (node != null) {
            return node.findChainChild(codes);
        } else {
            return null;
        }
    }
    
    @XmlTransient
    public List<String> getSelectedChildList() {
        List<String> ret = new LinkedList<String>();
        appendSelectedChildList(ret);
        return ret;
    }
    
    public boolean setDinamicValue(String code, String value) {
        DinamicValue val = findDinamic(code);
        if (val != null) {
            val.setValue(value);
            return true;
        } else {
            return false;
        }
    }
    
    public DinamicValue findDinamic(String code) {
        if (dinamics != null && !dinamics.isEmpty()) {
            for (DinamicValue val: dinamics) {
                if (val.getCode().equals(code)) {
                    return val;
                }
            }
        }        
        return null;
    }
    
    private void appendSelectedChildList(List<String> childList) {
        boolean found = false;
        for (DeviceNode child: getChildren()) {
            if (child.selected) {
                found = true;
                child.appendSelectedChildList(childList);
            }
        }
        if (!found) {
            childList.add(getNodePath());
        }
    }
    
    @XmlTransient
    public String getNodePath() {
        StringBuilder sb = new StringBuilder();
        appendPath(sb);
        return sb.toString();
    }
    
    private void appendPath(StringBuilder sb) {
        sb.insert(0, code);
        if (parent != null) {
            sb.insert(0, '.');
            ((DeviceNode)parent).appendPath(sb);
        }
    }
    private void appendDinamicValues(Map<String, String> values) {
        for (DinamicValue dv: dinamics) {
            values.put(dv.getCode(), dv.getValue());
        }
        for (DeviceNode child: getChildren()) {
            if (child.isSelected()) {
                child.appendDinamicValues(values);
            }
        }
    }
}
