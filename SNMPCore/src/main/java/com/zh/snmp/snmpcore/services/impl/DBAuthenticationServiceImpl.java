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

package com.zh.snmp.snmpcore.services.impl;

import com.zh.snmp.snmpcore.dao.UserDao;
import com.zh.snmp.snmpcore.entities.UserEntity;
import com.zh.snmp.snmpcore.services.AuthenticationService;
import com.zh.snmp.snmpcore.util.CoreUtil;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author sonrisa
 */
public class DBAuthenticationServiceImpl implements AuthenticationService {
    private static final UserEntity DEFAULT_FILTER = new UserEntity();
    
    @Autowired
    private UserDao userDao;

    private String defaultUser;
    private String defaultPwd;
    
    public DBAuthenticationServiceImpl() {
    }

    @Override
    public UserEntity getLoggedInPlayer() {
        //No token
        return null;
    }

    @Override
    public UserEntity findPlayer(String userName, String password) {
        if (userDao.login(userName, CoreUtil.digest(password))) {
            return userDao.findByName(userName);
        } else if (defaultUser.equals(userName) && defaultPwd.equals(password)) {
            if (userDao.count(DEFAULT_FILTER) > 0) {
                return null;
            } else {
                return createDefaultUser();
            }
        } else {
            return null;
        }
    }

    @Override
    public void logout() {
    }

    @Override
    public boolean register(UserEntity player, String password) {
        if (userDao.findByName(player.getName()) == null) {
            userDao.register(player, CoreUtil.digest(password));
            userDao.flush();
            return true;
        } else {
            return false;
        }
    }

    public String getDefaultPwd() {
        return defaultPwd;
    }

    public void setDefaultPwd(String defaultPwd) {
        this.defaultPwd = defaultPwd;
    }

    public String getDefaultUser() {
        return defaultUser;
    }

    public void setDefaultUser(String defaultUser) {
        this.defaultUser = defaultUser;
    }
    
    private UserEntity createDefaultUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(ADMIN_ROLE);
        userEntity.setRoles(Arrays.asList(ADMIN_ROLE));
        return userEntity;
    }
}
