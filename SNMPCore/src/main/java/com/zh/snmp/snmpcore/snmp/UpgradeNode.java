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
