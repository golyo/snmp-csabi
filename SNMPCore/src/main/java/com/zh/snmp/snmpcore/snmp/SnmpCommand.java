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
package com.zh.snmp.snmpcore.snmp;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import org.snmp4j.smi.VariableBinding;

/**
 *
 * @author Golyo
 */
public class SnmpCommand implements Serializable {
    private List<VariableBinding> bindings;
    
    public SnmpCommand() {
        bindings = new LinkedList<VariableBinding>();
    }

    public void addBinding(VariableBinding binding) {
        bindings.add(binding);
    }
    
    public List<VariableBinding> getBindings() {
        return bindings;
    }
    
    void setBindings(List<VariableBinding> bindings) {
        this.bindings = bindings;
    }
    
    @Override
    public String toString() {
        return bindings.toString();
    }
}
