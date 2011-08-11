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

import com.zh.snmp.snmpcore.domain.DeviceNode;
import com.zh.snmp.snmpcore.domain.DinamicValue;
import java.util.Properties;

/**
 *
 * @author Golyo
 */
public class UpgradeNode extends DeviceNode {
    private static final String PRODUCTNAME = "productName";
    private static final String PRODUCTSWIMAGEREV = "productSwImageRev";
    private static final String DOWNLOADSERVER = "downloadServer";
    private static final String DOWNLOADFILE = "downloadFile";
    
    public UpgradeNode(String configCode, Properties properties) {
        super();
        setCode(configCode);
        getDinamics().add(new DinamicValue(PRODUCTNAME, properties.getProperty(PRODUCTNAME)));
        getDinamics().add(new DinamicValue(PRODUCTSWIMAGEREV, properties.getProperty(PRODUCTSWIMAGEREV)));
        getDinamics().add(new DinamicValue(DOWNLOADSERVER, properties.getProperty(DOWNLOADSERVER)));
        getDinamics().add(new DinamicValue(DOWNLOADFILE, properties.getProperty(DOWNLOADFILE)));
    }
}
