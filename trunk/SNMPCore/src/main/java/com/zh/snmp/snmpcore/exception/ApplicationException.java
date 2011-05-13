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
 * Az ApplicationException-t (és leszarmazottait) a service csomag
 * publikus metodusai expliciten dobjak. Olyan hibak, amikbol a felhasznalo
 * ismetelt adatjavitassal helyes mukodest tud elerni.
 * <p/>
 * Ilyenek pl: bizonyos validacios vagy konzisztencia vagy authorizacios hibak
 *
 * @author Joe
 */
public class ApplicationException extends K11BaseException {

    /**
     *
     * @param code a hiba tipusat azonosito kod
     */
    public ApplicationException(ExceptionCodesEnum code) {
        super(code);
    }

    /**     
     * @param code a hiba tipusat azonosito kod
     * @param cause a hibat kivalto eredeti kivetel
     */
    public ApplicationException(ExceptionCodesEnum code, Throwable cause) {
        super(code, cause);
    }


    /**
     * @param code a hiba tipusat azonosito kod
     * @param cause a hibat kivalto eredeti kivetel
     */
    public ApplicationException(ExceptionCodesEnum code, String message) {
        super(code, message);
    }

    /**
     * @param code a hiba tipusat azonosito kod
     * @param cause a hibat kivalto eredeti kivetel
     */
    public ApplicationException(ExceptionCodesEnum code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
