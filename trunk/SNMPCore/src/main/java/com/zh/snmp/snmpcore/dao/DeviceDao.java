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

import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.entities.DeviceState;
import java.util.List;
import javax.persistence.Query;
import org.eclipse.persistence.queries.QueryByExamplePolicy;
import org.eclipse.persistence.queries.ReadObjectQuery;

/**
 *
 * @author Golyo
 */
public class DeviceDao extends BaseJpaDao<String, DeviceEntity> {
    private static final String RETRY_UPDATE_JPQL;
    static {
        StringBuilder sb = new StringBuilder();
        String prefix = DeviceState.class.getCanonicalName() + ".";
        sb.append("SELECT device FROM DeviceEntity device WHERE ");
        boolean first = true;
        for (DeviceState st: DeviceState.values()) {
            if (st.isRetryUpdate()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(" OR ");                
                }
                sb.append("device.configState = ").append(prefix).append(st.name());
            }
        }
        RETRY_UPDATE_JPQL = sb.toString();
    }
    
    public List<DeviceEntity> getRetryUpdateDevices() {
        Query q = getEntityManager().createQuery(RETRY_UPDATE_JPQL);
        return q.getResultList();
    }
    
    private static final String ERROR_UK_DEVICEID = "error.unique.deviceId";
    private static final String ERROR_UK_NODEID = "error.unique.nodeId";
    private static final String ERROR_UK_IPADDRESS = "error.unique.ipAddress";
    private static final String UNIQUE_DEVICE_JPQL = "SELECT device FROM DeviceEntity device WHERE " + 
            "device.id = :id OR device.nodeId = :nodeId OR device.ipAddress = :ipAddress";
    public String checkDevice(DeviceEntity device) {
        Query q = getEntityManager().createQuery(UNIQUE_DEVICE_JPQL);
        q.setParameter("id", device.getId());
        q.setParameter("nodeId", device.getNodeId());
        q.setParameter("ipAddress", device.getIpAddress());
        List<DeviceEntity> conflits = q.getResultList();
        if (conflits.isEmpty()) {
            return null;
        } else {
            DeviceEntity first = conflits.get(0);
            if (first.getId().equals(device.getId())) {
                return ERROR_UK_DEVICEID;
            } else if (first.getNodeId().equals(device.getNodeId())) {
                return ERROR_UK_NODEID;
            } else if (first.getIpAddress().equals(device.getIpAddress())) {
                return ERROR_UK_IPADDRESS;
            } else {
                throw new IllegalStateException("kell egyezzen az ip, node, vagy device id");                
            }
        }
    }
}
