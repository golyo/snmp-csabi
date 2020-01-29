package com.zh.snmp.snmpcore.message;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Golyo
 */
public interface MessageAppender extends Serializable {
    public List<ZhMessage> getMessages();
    
    public void addMessage(String key);    
    //public <T extends Serializable> void addMessage(String key, T object); 
    public void addMessage(String key, Object... params);
    
    public boolean isFinished();
    public void finish();
    public void start();
}
