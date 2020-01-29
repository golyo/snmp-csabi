package com.zh.snmp.snmpcore.aop;

import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class BaseInterceptor {

    /** Log üzenetben az üzenet egyes szegmentumait elválasztó karakter. */
    protected static final String DELIMITER = " - ";

    /** Log üzenetben az argumentumok listája elé kerülő prefix. */
    private static final String ARGUMENTS_LIST_PREFIX = "arg(s): ";

    /** Log üzenetben az argumentumok indexe és értéke közé kerülő karakter.
       Például ha az első argumentum értéke "bar", akkor: 0=bar */
    private static final String ARGUMENT_INDEX_AND_VALUE_DELIMITER = "=";

    /** Log üzenetben több argumentum esetén az argumentumok közé kerülő elválasztó karakter. */
    private static final String ARGUMENTS_DELIMITER = "; ";

    /** Log üzenetben a metódusok neve után írt zárójelet, a metódusok könnyebb láthatósága érdekében. */
    private static final String ROUND_BRACKETS = "()";

    /** Log üzenetben az osztály és metódus név közé írt pont karakter. */
    private static final String DOT = ".";

    /** Log üzenetben az argumentumok listáját ilyen zárójelek közé írjuk ki. */
    private static final String OPEN_SQUARE_BRACKET = "[";

    /** Log üzenetben az argumentumok listáját ilyen zárójelek közé írjuk ki. */
    private static final String CLOSE_SQUARE_BRACKET = "]";

    /** Log üzenetben akkor írjuk ki ezt, ha nincs a metódusnak argumentuma. */
    private static final String NO_ARGS_MSG = "[no args]";


    // -------------------------------------------------------------------------
    // ~ Protected methods
    // -------------------------------------------------------------------------

    /**
     * Visszaadja a meghívott metódus argumentumainak nevét és értékét egy
     * Srting-ben.
     *
     * @param joinPoint kapcsolódási pont arra a metódusra, amit az aspektus figyel
     * @return
     */
    protected String appendAgrList(final JoinPoint joinPoint){
        StringBuilder str = new StringBuilder();
        str.append(ARGUMENTS_LIST_PREFIX);

        Object[] argValues = joinPoint.getArgs();

        //ha nincs argumentum
        if (argValues == null || argValues.length == 0){
            str.append(NO_ARGS_MSG);
            return str.toString();
        }

        //argumentum sorszám és értek összefűzése
        str.append(OPEN_SQUARE_BRACKET);
        for(int i = 0; i<argValues.length; i++){            
            str.append(i);
            str.append(ARGUMENT_INDEX_AND_VALUE_DELIMITER);
            str.append(argValues[i]);
            if (i < argValues.length - 1){
                str.append(ARGUMENTS_DELIMITER);
            }
        }
        str.append(CLOSE_SQUARE_BRACKET);
        return str.toString();
    }

    /**
     * Visszaadja, hogy melyik osztály, melyik metódusa került meghívásra
     * szöveges reprezentációban.
     * <p/>
     *
     * A <code>useSimpleClassName</code> értékétől függően az osztályokra
     * azok rövid vagy teljes nevével hivatkozik.
     * <p/>
     * 
     * Peldaul:  <br/>
     * foo.bar.example.MyClass.myMethod() <br/>
     * vagy <br/>
     * MyClass.myMethod()
     *
     * @param joinPoint kapcsolódási pont arra a metódusra, amit az aspektus figyel
     * @param useSimpleClassName osztályok rövid vagy teljes nevét használja-e
     */
    protected String getInvocationTarget(final JoinPoint joinPoint, final boolean useSimpleClassName) {
        String className;

        if (useSimpleClassName){
            className = joinPoint.getTarget().getClass().getSimpleName();
        }else{
            className = joinPoint.getTarget().getClass().getName();
        }

        return appendClassWithMethod(className, joinPoint.getSignature().getName());
    }

    /**
     * Visszaadja a hívott metódust, mint reflexiós objektum.
     *
     * @param joinPoint  kapcsolódási pont arra a metódusra, amit az aspektus figyel
     * @return
     */
    protected Method retrieveTargetMethodFrom(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        return methodSignature.getMethod();
    }

    // -------------------------------------------------------------------------
    // ~ Private methods
    // -------------------------------------------------------------------------

    /**
     * Összefűzi az osztálynevet a metódusnévvel,
     * ilyen formaban:
     * <p/>
     *
     * className.methodName()
     *
     * @param className osztály neve
     * @param methodName metódus neve
     *
     */
    private String appendClassWithMethod(final String className,
            final String methodName){
        StringBuilder str = new StringBuilder();
        str.append(className);
        str.append(DOT);
        str.append(methodName);
        str.append(ROUND_BRACKETS);
        return str.toString();
    }


}
