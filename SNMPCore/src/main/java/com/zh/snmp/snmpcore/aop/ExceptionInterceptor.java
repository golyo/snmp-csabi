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
