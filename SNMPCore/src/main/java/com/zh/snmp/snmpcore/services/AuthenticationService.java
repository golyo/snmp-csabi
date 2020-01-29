package com.zh.snmp.snmpcore.services;

import com.zh.snmp.snmpcore.entities.UserEntity;

public interface AuthenticationService {
    public static final String ADMIN_ROLE = "ADMIN";
    
    public UserEntity getLoggedInPlayer();
    public UserEntity findPlayer(String userName, String password);
    public void logout();
    public boolean register(UserEntity player, String password);
}
