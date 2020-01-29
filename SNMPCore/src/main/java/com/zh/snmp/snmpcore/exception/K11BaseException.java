package com.zh.snmp.snmpcore.exception;

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
