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
