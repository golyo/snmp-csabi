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
package com.zh.snmp.snmpcore.services;

import com.zh.snmp.snmpcore.entities.ClientEntity;
import com.zh.snmp.snmpcore.entities.HistoryEntity;
import com.zh.snmp.snmpcore.entities.SnmpTypeEntity;
import java.util.List;

/**
 *
 * @author Golyo
 */
public interface SnmpService {
    public List<SnmpTypeEntity> findSnmpTypesByFilter(SnmpTypeEntity filter, String sort, int start, int count);
    public SnmpTypeEntity saveSnmpType(SnmpTypeEntity type);
    public SnmpTypeEntity findSnmpTypeByCode(String code);
    public SnmpTypeEntity findSnmpTypeById(Long id);
    
    public ClientEntity setSnmpTypeToClient(String ipAddress, String typeCode);
    public int changeTypeToAllClient(String oldTypeCode, String newTypeCode);
    
    public List<HistoryEntity> getClientHistory(HistoryEntity filter, String sort, int start, int count);
}
