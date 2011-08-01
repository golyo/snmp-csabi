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
package com.zh.snmp.snmpcore.snmp;

import com.zh.snmp.snmpcore.domain.ConfigNode;
import com.zh.snmp.snmpcore.domain.SnmpCommand;
import com.zh.snmp.snmpcore.exception.ExceptionCodesEnum;
import com.zh.snmp.snmpcore.exception.SystemException;
import com.zh.snmp.snmpcore.message.SimpleMessageAppender;
import com.zh.snmp.snmpcore.snmp.mib.MibParser;
import com.zh.snmp.snmpcore.util.JAXBUtil;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class UpgradeConfig implements InitializingBean {
    private static final String CONFIG_FILE = "upgradeConfig.xml";
    
    @Autowired
    private MibParser mibParser;
    
    private ConfigNode upgrades;
    
    @Override
    public void afterPropertiesSet() {
        InputStream stream = UpgradeConfig.class.getResourceAsStream(CONFIG_FILE);
        upgrades = JAXBUtil.unmarshalTyped(new InputStreamReader(stream), ConfigNode.class);
        SimpleMessageAppender appender = new SimpleMessageAppender();
        if (!mibParser.parseAndSetMibValues(upgrades, appender)) {
            throw new SystemException(ExceptionCodesEnum.ConfigurationException, "Wrong upgrade command " + appender.toString());  
        }
    }

    public List<SnmpCommand> getUpgradeCommands(String config) {
        ConfigNode act = upgrades.findChildByCode(config);
        return act != null ? act.getCommands() : Collections.EMPTY_LIST;
    }    
}
