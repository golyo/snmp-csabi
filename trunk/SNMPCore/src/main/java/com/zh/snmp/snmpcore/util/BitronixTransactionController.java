package com.zh.snmp.snmpcore.util;

import javax.transaction.TransactionManager;
import org.eclipse.persistence.sessions.ExternalTransactionController;
import org.eclipse.persistence.transaction.JTATransactionController;


/**
 * @author Palesz
 */
public class BitronixTransactionController extends JTATransactionController implements ExternalTransactionController {

    public static final String JNDI_TRANSACTION_MANAGER_NAME = "java:comp/UserTransaction";

    /**
     * INTERNAL:
     * Obtain and return the JTA TransactionManager on this platform
     */
    @Override
    protected TransactionManager acquireTransactionManager() throws Exception {
        return (TransactionManager) jndiLookup(JNDI_TRANSACTION_MANAGER_NAME);
    }
}
