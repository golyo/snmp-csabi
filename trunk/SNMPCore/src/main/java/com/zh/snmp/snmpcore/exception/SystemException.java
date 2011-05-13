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
 * SystemException-be és leszarmazottaiba kell csomagolni minden
 * olyan technikai hibat, ami alacsony layerbol jott.
 * <p/>
 * Pl: SQLException, JMSException, NamingException esetleg RemoteException stb.
 * <p/>
 * Ezek olyan hibak, amikbol a felhasznalo ismetelt adatjavitassal
 * nem tud visszaterni a helyes mukodeshez.
 *
 * @author Joe
 */
public class SystemException extends K11BaseException {

    /**
     *
     * @param code a hiba tipusat azonosito kod
     */
    public SystemException(ExceptionCodesEnum code) {
        super(code);
    }

    /**
     * @param code a hiba tipusat azonosito kod
     * @param cause a hibat kivalto eredeti kivetel
     */
    public SystemException(ExceptionCodesEnum code, Throwable cause) {
        super(code, cause);
    }

    /**
     * @param code a hiba tipusat azonosito kod
     * @param cause a hibat kivalto eredeti kivetel
     */
    public SystemException(ExceptionCodesEnum code, String message) {
        super(code, message);
    }

    /**
     * @param code a hiba tipusat azonosito kod
     * @param cause a hibat kivalto eredeti kivetel
     */
    public SystemException(ExceptionCodesEnum code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
