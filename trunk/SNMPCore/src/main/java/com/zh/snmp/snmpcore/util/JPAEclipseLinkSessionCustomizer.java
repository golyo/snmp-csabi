/*
 *   Copyright (c) 2010 Sonrisa Informatikai Kft. All Rights Reserved.
 * 
 *  This software is the confidential and proprietary information of
 *  Sonrisa Informatikai Kft. ("Confidential Information").
 *  You shall not disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with Sonrisa.
 * 
 *  SONRISA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 *  THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SONRISA SHALL NOT BE LIABLE FOR
 *  ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 *  DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.zh.snmp.snmpcore.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ServerSession;

/**
 *
 * @author Golyo
 */
public class JPAEclipseLinkSessionCustomizer
        implements SessionCustomizer {

    /**
     * Get a dataSource connection and set it on the session with lookupType=STRING_LOOKUP
     */
    @Override
    public void customize(Session session) throws Exception {
        JNDIConnector connector = null;
        Context context = null;
        try {
            context = new InitialContext();
            if (null != context) {
                connector = (JNDIConnector) session.getLogin().getConnector(); // possible CCE
                // Change from COMPOSITE_NAME_LOOKUP to STRING_LOOKUP
                // Note: if both jta and non-jta elements exist this will only change the first one - and may still result in
                // the COMPOSITE_NAME_LOOKUP being set
                // Make sure only jta-data-source is in persistence.xml with no non-jta-data-source property set
                connector.setLookupType(JNDIConnector.STRING_LOOKUP);

                // Or, if you are specifying both JTA and non-JTA in your persistence.xml then set both connectors to be safe
                JNDIConnector writeConnector = (JNDIConnector) session.getLogin().getConnector();
                writeConnector.setLookupType(JNDIConnector.STRING_LOOKUP);
                JNDIConnector readConnector =
                        (JNDIConnector) ((DatabaseLogin) ((ServerSession) session).getReadConnectionPool().getLogin()).getConnector();
                readConnector.setLookupType(JNDIConnector.STRING_LOOKUP);

                System.out.println("_JPAEclipseLinkSessionCustomizer: configured " + connector.getName());
            } else {
                throw new Exception("_JPAEclipseLinkSessionCustomizer: Context is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}