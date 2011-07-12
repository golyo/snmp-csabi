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
package com.zh.snmp.snmpcore.message;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Golyo
 */
public class SimpleMessageAppender implements MessageAppender {
    private LinkedList<ZhMessage> messages;
    private boolean finished;
    
    public SimpleMessageAppender() {
        messages = new LinkedList<ZhMessage>();
    }

    @Override
    public void addMessage(String key) {
        addOwnMessage(new ZhMessage(key));
    }
    
    @Override
    public <T extends Serializable> void addMessage(String key, T object) {
        addOwnMessage(new ZhMessage(key, object));
    }
    
    @Override
    public LinkedList<ZhMessage> getMessages() {
        return messages;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
    
    @Override
    public void finish() {
        finished = true;
    }
    
    protected void addOwnMessage(ZhMessage message) {
        messages.add(message);        
    }
    
    @Override
    public String toString() {
        return messages.toString();
    }
}
