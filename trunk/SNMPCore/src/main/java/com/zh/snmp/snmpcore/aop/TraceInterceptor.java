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

package com.zh.snmp.snmpcore.aop;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AOP-s interceptor, ami naplozza a metodus hivasokat.
 * Rogzitett informaciok: be- es kilepes, argumentumlista,
 * es a metodusban eltoltott ido.
 *
 * @author Joe
 */
@Aspect
public class TraceInterceptor extends BaseInterceptor {

    protected transient Logger LOG = LoggerFactory.getLogger(getClass());

    /** A log-ra az osztalyok rovid vagy teljes nevet irja-e. Default: false */
    private boolean useSimpleClassName = false;

    /** A logra kerulo datum alapertelmezett formatuma. */
    private static final String DEFAULT_DATE_PATTERN = "yyyy.MM.dd.HH:mm:ss.S";

    private SimpleDateFormat sdf;

    /*********************************************
     * A logra kerulo uzenetek fix fragmentumai
     */
    
    private static final String ENTERING_PREFIX = "*** Entering";
    private static final String EXITING_PREFIX = "*** Exiting";
    private static final String RETURNED_VALUE_MSG = "return value: ";
    private static final String EXCEPTION_PREFIX = "*** Exception in";
    private static final String RUNNING_TIME_MSG_PREFIX = "*** Running time";
    private static final String RUNNING_TIME_MSG_POSTFIX = " ms";
    private static final String COLLECTION_WITH_SIZE = " collection with size: ";
    private static final String ARRAY_WITH_SIZE = " array with size: ";

    public TraceInterceptor(){
         sdf = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
    }



    // ------------------------------------------------------------------------
    // Interceptor methods
    // ------------------------------------------------------------------------

    /**
     * Logozast vegrehajto metodus.
     * Naplozza a hivast, folytatja a megszakitott hivast,
     * majd naplozza a kilepest, a visszateresi erteket, es
     * az esetleges kiveteleket, valamint a futas idejet.
     * <p/>
     * Ha kivetel dobas tortent, azt minden esetben tovabbdobja.
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    public Object invokeAround(ProceedingJoinPoint pjp) throws Throwable {
        final String target = getInvocationTarget(pjp, useSimpleClassName);
        if (LOG.isTraceEnabled()) {
            LOG.trace(appendEnteringMsg(target, pjp));
        }

        final long startTime = System.currentTimeMillis();
        try {
            final Object returnValue = pjp.proceed();
            if (LOG.isTraceEnabled()) {
                LOG.trace(appendExitingMsg(target, returnValue));
            }
            return returnValue;

        } catch (Throwable ex) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(appendExceptionMsg(target));
            }
            throw ex;

        } finally {
            final long stopTime = System.currentTimeMillis();
            if (LOG.isTraceEnabled()) {
                LOG.trace(appendPerformanceMsg(startTime, stopTime, target));
            }
        }
    }
    
    // ------------------------------------------------------------------------
    // Private methods
    // ------------------------------------------------------------------------

    /**
     * Osszeallitja a belepesi uzenetet a logozas szamara.    
     *
     * @param invocationTarget
     * @param pjp
     * @return
     */
    private String appendEnteringMsg(final String invocationTarget, final ProceedingJoinPoint pjp){
        StringBuilder result = new StringBuilder();
        result.append(ENTERING_PREFIX);
        result.append(DELIMITER);
        result.append(sdf.format(new Date()));
        result.append(DELIMITER);
        result.append(invocationTarget);
        result.append(DELIMITER);
        result.append(appendAgrList(pjp));

        return result.toString();
    }

    /**
     * Összeállítja a kilépési üzenetet a logozás számára.
     * <p/>
     * Ha a visszatérési érték egy Collection, akkor csak jelzi, hogy az
     * és a lista hosszát írja ki.
     *
     *
     * @param invocationTarget
     * @param pjp
     * @return
     */
    private String appendExitingMsg(final String invocationTarget, final Object returnValue){
        StringBuilder result = new StringBuilder();
        result.append(EXITING_PREFIX);
        result.append(DELIMITER);
        result.append(invocationTarget);
        result.append(DELIMITER);
        result.append(RETURNED_VALUE_MSG);
        
        if (returnValue instanceof Collection){
            result.append(COLLECTION_WITH_SIZE);
            result.append(((Collection)returnValue).size());
        }else if (returnValue instanceof Object[]){
            result.append(ARRAY_WITH_SIZE);
            result.append(((Object[])returnValue).length);
        }else{
            result.append(returnValue);
        }              
        return result.toString();
    }

    /**
     * Osszefuzi a performance uzenetet a logozas szamara.
     *
     * @param startTime
     * @param stopTime
     * @param invocationTarget
     * @return
     */
    private String appendPerformanceMsg(final long startTime, final long stopTime,
                final String invocationTarget){

        final long interval = stopTime - startTime;

        StringBuilder result = new StringBuilder();
        result.append(RUNNING_TIME_MSG_PREFIX);
        result.append(DELIMITER);
        result.append(invocationTarget);
        result.append(DELIMITER);
        result.append(interval);
        result.append(RUNNING_TIME_MSG_POSTFIX);

        return result.toString();
    }

    /**
     * Osszefuzi az exception uzenetet a logozas szamara.
     *
     * @param invocationTarget
     * @return
     */
    private String appendExceptionMsg(final String invocationTarget){
        StringBuilder result = new StringBuilder();
        result.append(EXCEPTION_PREFIX);
        result.append(DELIMITER);
        result.append(invocationTarget);
        
        return result.toString();
    }

    // ------------------------------------------------------------------------
    // Setters / getters
    // ------------------------------------------------------------------------

    public void setUseSimpleClassName(boolean useSimpleClassName) {
        this.useSimpleClassName = useSimpleClassName;
    }
}
