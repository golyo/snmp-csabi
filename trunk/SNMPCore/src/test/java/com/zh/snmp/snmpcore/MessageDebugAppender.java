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
package com.zh.snmp.snmpcore;

import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.message.ZhMessage;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Golyo
 */
public class MessageDebugAppender implements MessageAppender {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDebugAppender.class);
    
    @Override
    public <T> void addMessage(String key) {
        LOGGER.debug("Message added: " + key);
    }

    @Override
    public <T> void addMessage(String key, T object) {
        LOGGER.debug("Message added: " + key + ", object: " + object);
    }

    @Override
    public List<ZhMessage> getMessages() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
