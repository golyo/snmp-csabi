package com.zh.snmp.snmpcore.snmp;

import java.io.IOException;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 *
 * @author Golyo
 */
public class SnmpFactory {

    private static int SNMP_VERSION = SnmpConstants.version2c;
    private static String PORT = "161";

    public static Snmp createSnmp() throws IOException {
        TransportMapping transport = new DefaultUdpTransportMapping();
        transport.listen();
        return new Snmp(transport);
    }

    private static final String TESTIP = "192.168.2.253/161";
    public static CommunityTarget createTarget(String ip, String community) {
        CommunityTarget comtarget = new CommunityTarget();
        comtarget.setCommunity(new OctetString(community));
        comtarget.setVersion(SNMP_VERSION);
        comtarget.setAddress(new UdpAddress(ip + "/" + PORT));
        comtarget.setRetries(2);
        comtarget.setTimeout(1000);
        return comtarget;
    }
}
