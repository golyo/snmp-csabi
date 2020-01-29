package com.zh.snmp.snmpcore.dao;

import com.zh.snmp.snmpcore.entities.BaseEntity;
import com.zh.snmp.snmpcore.entities.DeviceEntity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Golyo
 */
public class TestDao {

    @PersistenceContext
    protected EntityManager entityManager;

    //    @Autowired
    public DeviceEntity save(DeviceEntity object) {
        return entityManager.merge(object);

        /*
        if (object.getId() != null) {
            return entityManager.merge(object);
        } else {
            entityManager.persist(object);
            return object;
        }
         * 
         */
    }
}