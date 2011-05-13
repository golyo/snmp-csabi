/*
 *  *  Copyright (c) 2010 Sonrisa Informatikai Kft. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Sonrisa Informatikai Kft. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sonrisa.
 *
 * SONRISA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SONRISA SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.zh.snmp.snmpcore.exception;

/**
 * Az alkalmazasban hasznalt ket fo kivetel tipus ososztalya,
 * ami a kozos reszeket (kod, egyedi azonosito, ...)
 * tartalmazza.
 *
 * @author Joe
 */
public class K11BaseException extends RuntimeException {

    // -----------------------------------------------------------------------
    // ~ Private members
    // -----------------------------------------------------------------------

    /**
     * Egyedi azonosito, ami a konkret hibaesetet azonositja.
     * A naplofajlban valo keresest segiti.
     */
    private String id;

    /**
     * A hiba tipusat azonostio kod.
     */
    private ExceptionCodesEnum code;
    

    // -----------------------------------------------------------------------
    // ~ Constructor
    // -----------------------------------------------------------------------

    public K11BaseException(ExceptionCodesEnum code) {
        this.code = code;
    }

    public K11BaseException(ExceptionCodesEnum code, Throwable cause) {
        super(cause);
        this.code = code;        
    }

    public K11BaseException(ExceptionCodesEnum code, String message) {
        super(message);
        this.code = code; 
    }

    public K11BaseException(ExceptionCodesEnum code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    // -----------------------------------------------------------------------
    // ~ Setters / getters
    // -----------------------------------------------------------------------


    public void setCode(ExceptionCodesEnum code) {
        this.code = code;
    }

    public ExceptionCodesEnum getCode() {
        return code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
