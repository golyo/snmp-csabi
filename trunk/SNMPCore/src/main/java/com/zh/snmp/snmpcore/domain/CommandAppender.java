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

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Golyo
 */
public class CommandAppender {
    private List<List<SnmpCommand>> commands;
    
    public CommandAppender() {
        commands = new LinkedList<List<SnmpCommand>>();
    }

    public void appendCommand(List<SnmpCommand> command) {
        //Todo make merge
        commands.add(command);
    }
    
    public List<List<SnmpCommand>> getCommands() {
        return commands;
    }
}