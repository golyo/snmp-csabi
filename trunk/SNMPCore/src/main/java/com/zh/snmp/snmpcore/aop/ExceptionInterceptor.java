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

import com.zh.snmp.snmpcore.exception.ApplicationException;
import com.zh.snmp.snmpcore.exception.DaoException;
import com.zh.snmp.snmpcore.exception.ExceptionCodesEnum;
import com.zh.snmp.snmpcore.exception.JpaVersionException;
import com.zh.snmp.snmpcore.exception.K11BaseException;
import com.zh.snmp.snmpcore.exception.RmiException;
import com.zh.snmp.snmpcore.exception.SystemException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.UUID;
import javax.persistence.PersistenceException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AOP-s interceptor, ami a service retegbol szarmazo kiveteleket kezeli:
 * <p/>
 *
 * A kivétel kezelő aspektus garantálja,
 * hogy minden service hívás try-catch-ben fut,
 * a dobott kivételekről ellenőrzi, hogy ApplicationException-e
 * (ekkor már tudunk róla és csak az egyedi azonosításáról
 * és a naplózásáról kell gondoskodni) vagy nem.
 * <p/>
 * 
 * Amennyiben nem, feltesszük, hogy SystemException, olyan hiba,
 * ahonnan a felhasználó ismételt adatjavítással nem tud visszatérni,
 * ekkor wrap-elni kell a hibát SystemException egy leszármazottjába
 * (mapping konfiguráció pl. minden SQLException -> saját DAOException-re, saját kóddal)
 *
 * @author Joe
 */
@Aspect
public class ExceptionInterceptor extends BaseInterceptor {

    protected transient Logger LOG = LoggerFactory.getLogger(getClass());

    /** A naplóban megjelenő üzenet.  */
    private static final String EXCEPTION_MSG = "*** Exception occured here: ";

    /** A naplóban megjelenő üzenet második része, ami a kivétel egyedi azonosítóját tartalmazza. */
    private static final String EXCEPTION_ID_MSG = "Exception id: ";

    /** A naplóban a rövid osztály nevet használja-e a teljes helyett. */
    private static final boolean USE_SIMPLE_CLASS_NAME = true;

    // ------------------------------------------------------------------------
    // Interceptor methods
    // ------------------------------------------------------------------------

    /**
     * Try-catch blokkba csomagolja a service réteg hivásait.
     * <p/>
     * A catch ágakban kezeljük a kivételeket. Lsd az osztály szintű kommentet.
     * Szükség esetén új kivétel típusokkal bővíthető.
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    public Object invokeAround(ProceedingJoinPoint pjp) throws Throwable{

        Object returnValue = null;
        try{
            //service method invocation
            returnValue = pjp.proceed();         
        }
        catch(javax.persistence.OptimisticLockException ex) {
            throw new JpaVersionException(ex);
        }
        catch(ApplicationException appEx){
            addIdLogAndThrow(appEx, pjp);
        }
        catch(SystemException sysEx){
            addIdLogAndThrow(sysEx, pjp);
        }
        catch(RemoteException remoteEx){
            addIdLogAndThrow(new RmiException(
                                    ExceptionCodesEnum.RemoteException, remoteEx), pjp);
        }
        catch(PersistenceException perEx){
             addIdLogAndThrow(new DaoException(
                                    ExceptionCodesEnum.PersistenceException, perEx), pjp);
        }
        catch(SQLException sqlEx){
             addIdLogAndThrow(new DaoException(
                                    ExceptionCodesEnum.SQLException, sqlEx), pjp);
        }
        catch(JpaVersionException jpaEx){
            //FIXME: for cserepj
            throw jpaEx;
        }
        catch(Throwable ex){
             addIdLogAndThrow(new SystemException(
                                    ExceptionCodesEnum.UnknownExceptionType, ex), pjp);
        }
        return returnValue;
    }

    // ------------------------------------------------------------------------
    // Private methods
    // ------------------------------------------------------------------------

    /**
     * Beallit egy egyedi azonositot a kivetelnek,
     * logozza, majd tovabbdobja.
     *
     * @param ex
     * @param joinPoint
     */
    private void addIdLogAndThrow(K11BaseException ex, final JoinPoint jp){
        ex.setId(generateId());
        doLogging(ex, jp);
        throw ex;
    }

    /**
     * Egyedi azonosító generalása a konkrét hibaesethez.
     * <p/>    
     * Egy 4-es tipusú (véletlen) UUID-t ad vissza, amit
     * egy pszeudo véletlen generátor állít elő.
     * 
     * @return egy véletlen uuid
     */
    private String generateId(){        
        return UUID.randomUUID().toString();
    }

    /**
     * A kivétel logozása a beállított logger-re, error szinten.
     *
     * @param ex
     * @param joinPoint
     */
    private void doLogging(K11BaseException ex, final JoinPoint joinPoint){
        StringBuilder result = new StringBuilder();
        result.append(EXCEPTION_MSG);
        result.append(getInvocationTarget(joinPoint, USE_SIMPLE_CLASS_NAME));
        result.append(DELIMITER);
        result.append(appendAgrList(joinPoint));
        result.append(DELIMITER);
        result.append(EXCEPTION_ID_MSG);
        result.append(ex.getId());

        if(LOG.isErrorEnabled()){
            LOG.error(result.toString(), ex);
        }
    }

    // ------------------------------------------------------------------------
    // Setters / getters 
    // ------------------------------------------------------------------------
        
}
