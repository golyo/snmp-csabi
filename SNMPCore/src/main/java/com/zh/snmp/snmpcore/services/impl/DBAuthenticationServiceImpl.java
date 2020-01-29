package com.zh.snmp.snmpcore.services.impl;

import com.zh.snmp.snmpcore.dao.UserDao;
import com.zh.snmp.snmpcore.entities.UserEntity;
import com.zh.snmp.snmpcore.services.AuthenticationService;
import com.zh.snmp.snmpcore.util.CoreUtil;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
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
