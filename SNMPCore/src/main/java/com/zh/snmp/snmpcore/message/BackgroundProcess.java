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
