package com.zh.snmp.snmpcore.entities;

/**
 *
 * @author Golyo
 */
public enum DeviceState {
    NOT_FOUND(false, true),
    NEW(true, true),
    RUNNING(true, false),
    CONFIGURED(true, false),
    ERROR(false, true),
    UPDATED(false, true);
    
    private boolean canContinue;
    private boolean retryUpdate;
    private DeviceState(boolean canContinue, boolean retryUpdate) {
        this.canContinue = canContinue;
        this.retryUpdate = retryUpdate;
    }
    
    public boolean canContinue() {
        return canContinue;
    }
    
    public boolean isRetryUpdate() {
        return retryUpdate;
    }
}
