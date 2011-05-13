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
package com.zh.snmp.snmpcore.dao;

import com.zh.snmp.snmpcore.entities.UserEntity;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Golyo
 */
public class UserDao extends BaseJpaDao<UserEntity> {
    private static final String Q_LOGIN = "SELECT 1 FROM USERS p WHERE p.name = ?1 AND p.password = ?2";
    private static final String Q_UPDATE_PWD = "UPDATE USERS SET PASSWORD = ?1 WHERE ID = ?2";
    private static final String Q_FIND_BY_NAME = "SELECT object(p) FROM UserEntity p WHERE p.name = ?1";

    public boolean login(String userName, String password) {
        Query q = entityManager.createNativeQuery(Q_LOGIN);
        q.setParameter(1, userName);
        q.setParameter(2, password);
        List r = q.getResultList();
        return !r.isEmpty();
    }

    public void register(UserEntity player, String password) {
        entityManager.persist(player);
        Query q = entityManager.createNativeQuery(Q_UPDATE_PWD);
        q.setParameter(1, password);
        q.setParameter(2, player.getId());
        q.executeUpdate();
    }

    public UserEntity findByName(String name) {
        Query q = entityManager.createQuery(Q_FIND_BY_NAME);
        q.setParameter(1, name);
        List<UserEntity> ret = q.getResultList();
        return ret.isEmpty() ? null : ret.get(0);
    }
    
}
