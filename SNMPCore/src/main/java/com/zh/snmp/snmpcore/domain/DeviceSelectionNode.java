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

/**
 *
 * @author Golyo
 */
public class DeviceSelectionNode extends DefaultNode implements Serializable {
    private boolean selected;
    private String code;
    
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DeviceSelectionNode> getChildren() {
        return (List<DeviceSelectionNode>)children;
    }

    public void setChildren(List<DeviceSelectionNode> children) {
        this.children = children;
    }
   
    public DeviceMap createMap() {
        DeviceMap ret = new DeviceMap();
        appendMap(ret);
        return ret;
    }
    
    protected void appendMap(DeviceMap map) {
        map.setCode(code);
        for (DeviceSelectionNode selChild: getChildren()) {
            if (selChild.isSelected()) {
                DeviceMap mapChild = new DeviceMap();
                selChild.appendMap(mapChild);   
                map.getChildren().add(mapChild);
            }
        }
    }
}
