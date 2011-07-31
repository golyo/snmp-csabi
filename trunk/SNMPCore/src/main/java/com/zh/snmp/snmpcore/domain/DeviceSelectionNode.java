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
public class DeviceSelectionNode extends DefaultNode implements Serializable {
    private boolean selected;
    private String code;
    private List<DinamicValue> dinamics;
    
    public DeviceSelectionNode() {
        super();
        dinamics = new LinkedList<DinamicValue>();
    }
    
    public DeviceSelectionNode(ConfigNode node) {
        this();
        code = node.getCode();
        dinamics = node.getDinamics();
        for (ConfigNode child: node.getChildren()) {
            getChildren().add(new DeviceSelectionNode(child));
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
                ((DeviceSelectionNode)parent).setSelected(selected);
            }            
        } else {
            for (DefaultNode child: children) {
                ((DeviceSelectionNode)child).setSelected(selected);
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
    public List<DeviceSelectionNode> getChildren() {
        return (List<DeviceSelectionNode>)children;
    }

    public void setChildren(List<DeviceSelectionNode> children) {
        this.children = children;
    }
   
    @XmlElement(name="dinamic")
    public List<DinamicValue> getDinamics() {
        return dinamics;
    }

    public void setDinamics(List<DinamicValue> dinamics) {
        this.dinamics = dinamics;
    }
    
    public DeviceSelectionNode findChild(String code) {
        for (DeviceSelectionNode child: getChildren()) {
            if (child.getCode().equals(code)) {
                return child;
            }
        }
        return null;
    }
    
    public DeviceSelectionNode findChainChild(Deque<String> codes) {
        String find = codes.pop();
        DeviceSelectionNode node = findChild(find);
        if (codes.isEmpty()) {
            return node;
        } else if (node != null) {
            return node.findChainChild(codes);
        } else {
            return null;
        }
    }
    
    @XmlTransient
    public Map<String, String> getDinamicValues() {
        Map<String, String> values = new HashMap<String, String>();
        appendDinamicValues(values);
        return values;
    }
    
    @XmlTransient
    public List<String> getSelectedChildList() {
        List<String> ret = new LinkedList<String>();
        appendSelectedChildList(ret);
        return ret;
    }
    
    private void appendSelectedChildList(List<String> childList) {
        boolean found = false;
        for (DeviceSelectionNode child: getChildren()) {
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
            ((DeviceSelectionNode)parent).appendPath(sb);
        }
    }
    private void appendDinamicValues(Map<String, String> values) {
        for (DinamicValue dv: dinamics) {
            values.put(dv.getCode(), dv.getValue());
        }
        for (DeviceSelectionNode child: getChildren()) {
            if (child.isSelected()) {
                child.appendDinamicValues(values);
            }
        }
    }
}
