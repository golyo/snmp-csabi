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

/**
 *
 * @author Golyo
 */
public abstract class BackgroundProcess implements Runnable, Serializable {
    private MessageAppender appender;
    private boolean finished;
    
    public BackgroundProcess(MessageAppender appender) {
        this.appender = appender;
        finished = false;
    }
    
    public void startProcess() {
        Thread thread = new Thread(this);
        thread.start();
    }
    
    protected abstract void doWork();
    
    @Override
    public final void run() {
        try {
            doWork();
        } catch (Exception e) {
            handleException(e);
        }
        finished = true;
        appender.finish();
    }

    protected abstract void handleException(Exception exception);
    
    public MessageAppender getAppender() {
        return appender;
    }

    public boolean isFinished() {
        return finished;
    }
    
    
}
