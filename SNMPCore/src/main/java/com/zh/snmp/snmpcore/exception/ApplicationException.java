package com.zh.snmp.snmpcore.exception;

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
