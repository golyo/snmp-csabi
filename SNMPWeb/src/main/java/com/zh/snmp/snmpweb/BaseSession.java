/*
 *  Copyright 2010 sonrisa.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.zh.snmp.snmpweb;

import com.zh.snmp.snmpcore.entities.UserEntity;
import com.zh.snmp.snmpcore.services.AuthenticationService;
import java.util.LinkedList;
import java.util.List;
import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author golyo
 */
public class BaseSession extends AuthenticatedWebSession {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSession.class);
    
    public enum SnmpRoles {
        ADMIN,
        USER
    }
    
    @SpringBean
    private AuthenticationService authService;

    private UserEntity user;
    private Roles roles;
    /**
     * A Spring konfigban be�ll�tott authentik�ci�s provider
     */
    public BaseSession(Request request) {
        super(request);
        InjectorHolder.getInjector().inject(this);
        user = authService.getLoggedInPlayer();
        if (!setRoles()) {
            roles = null;
        }
        //signIn(true);
    }

    public static BaseSession get() {
        return (BaseSession)WebSession.get();
    }

    @Override
    public Roles getRoles() {
        return roles;
    }

    protected boolean setRoles() {
        if (user != null) {
            roles = new Roles(user.getRoles().toArray(new String[user.getRoles().size()]));
            LOGGER.debug("SET ROLES " + roles);
            return true;
        } else {
            return false;
        }
    }

    public UserEntity getUser() {
        return user;
    }

    @Override
    public void signOut() {
        super.signOut();
        authService.logout();
        user = null;
        roles = null;
    }

    private static final String ADMIN_ACC = "Admin";
    @Override
    public boolean authenticate(String username, String password) {
        user = authService.findPlayer(username, password);
        if (user == null && ADMIN_ACC.equals(username) && ADMIN_ACC.equals(password)) {
            user = createDefaultAdmin();
        }
        return setRoles();
    }

    private UserEntity createDefaultAdmin() {
        UserEntity ret = new UserEntity();
        ret.setName(ADMIN_ACC);
        ret.setRoles(getAllRoles());
        return ret;
    }
    
    private static List<String> getAllRoles() {
        List<String> ret = new LinkedList<String>();
        for (SnmpRoles r: SnmpRoles.values()) {
            ret.add(r.toString());
        }
        return ret;
    }
}
