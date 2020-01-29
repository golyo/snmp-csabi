package com.zh.snmp.snmpcore.message;

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
    public void addMessage(String key, Object[] params) {
        addOwnMessage(new ZhMessage(key, params));
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
    
    @Override
    public void start() {
        finished = false;
    }    
}
