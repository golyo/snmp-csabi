package com.zh.snmp.snmpcore;

import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.message.ZhMessage;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Golyo
 */
public class MessageDebugAppender implements MessageAppender {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDebugAppender.class);
    private boolean finished = false;
    
    @Override
    public void addMessage(String key) {
        LOGGER.debug("Message added: " + key);
    }

    @Override
    public void addMessage(String key, Object[] params) {
        LOGGER.debug("Message added: " + key + ", params: " + (params != null ? Arrays.toString(params) : "null"));
    }

    @Override
    public List<ZhMessage> getMessages() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void finish() {
        finished = true;
        LOGGER.debug("Finished");
    }

    @Override
    public void start() {
        finished = false;
        LOGGER.debug("Started");
    }

    @Override
    public boolean isFinished() {
        return finished;
    }        
            
}
