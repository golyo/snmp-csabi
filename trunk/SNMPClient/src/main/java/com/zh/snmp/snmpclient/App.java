package com.zh.snmp.snmpclient;

import com.zh.snmp.snmpclient.generated.SnmpWebService;
import com.zh.snmp.snmpclient.generated.SnmpWebService_Service;
import java.util.List;

//import com.zh.snmp.snmpclient.generated.TryWs_Service;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SnmpWebService_Service service = new SnmpWebService_Service();
        SnmpWebService srv = service.getSnmpWebServicePort();
        
        List<String> confd = srv.getConfigurations(); 
        System.out.println("++++++++++++" + confd);
        
        srv.setDeviceConfig("node", "aa");
    }
}
