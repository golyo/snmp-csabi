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
package com.zh.snmp.snmpcore.snmp;

import com.zh.snmp.snmpcore.snmp.SnmpCommand;

/**
 *
 * @author Golyo
 */
public class SnmpCommandResult {
    private SnmpCommand result;
    private ResultType type;
    private String errorStr;        
    
    public SnmpCommandResult(SnmpCommand command) {
        this.result = command;
        this.type = ResultType.SUCCES;
    }

    public void resultFailed(String errorString) {
        this.errorStr = errorString;
        this.type = ResultType.FAILED;
    }
    
    public void resultSkip() {
        this.type = ResultType.SKIPED;
    }
    
    public SnmpCommand getCommand() {
        return result;
    }

    public String getErrorStr() {
        return errorStr;
    }

    public ResultType getType() {
        return type;
    }
    
    public enum ResultType {
        SUCCES,
        FAILED,
        SKIPED
    }
    
    @Override
    public String toString() {
        return type + ": " + errorStr + ", " + result;
    }    
}
