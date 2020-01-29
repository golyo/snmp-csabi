package com.zh.snmp.snmpcore.exception;

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
