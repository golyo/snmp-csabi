package com.zh.snmp.snmpcore.message;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Golyo
 */
public class ZhMessage implements Serializable {
    private String resourceKey;
    private Object[] params;
    private Date date;

    public ZhMessage(String resourceKey) {
        this.resourceKey = resourceKey;
        this.date = new Date();
    }
    
    public ZhMessage(String resourceKey, Object... params) {
        this.resourceKey = resourceKey;
        this.params = params;
        this.date = new Date();
    }
    
    public Object[] getParams() {
        return params;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    @Override
    public String toString() {
        return resourceKey + ((params != null) ? Arrays.toString(params) : "");
    }
    
    public Date getDate() {
        return date;
    }
}
