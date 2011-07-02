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
        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        SnmpWebService_Service service = new SnmpWebService_Service();
        SnmpWebService srv = service.getSnmpWebServicePort();
        
        boolean succes = srv.createDevice("testCreate1", "testIpAddr1", "testMacAddr1");
        //List<String> confd = srv.getConfigurations(); 
        //System.out.println("++++++++++++" + confd);
        
        
        //boolean succes = srv.setDeviceConfig("nodeidTest", "aa"); 
        System.out.println("++++++++++++" + succes);
    }
}
