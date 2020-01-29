package com.zh.snmp.snmpcore.message;

/**
 *
 * @author Golyo
 */
public class MaxMessageAppender extends SimpleMessageAppender {
    private int maxNo;
    
    public MaxMessageAppender(int maxNo) {
        super();
        this.maxNo = maxNo;
    }

    @Override
    protected void addOwnMessage(ZhMessage message) {
        if (getMessages().size() >= maxNo) {
            getMessages().pop();
        }
        super.addOwnMessage(message);
    }

}
