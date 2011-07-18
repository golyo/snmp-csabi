package com.zh.snmp.snmpclient;

import com.zh.snmp.snmpclient.generated.SnmpWebService;
import com.zh.snmp.snmpclient.generated.SnmpWebService_Service;
import java.util.Arrays;
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
        /*
        String s = "acces.internet";
        String[] d = s.split("\\."); 
        System.out.println("++++++++++++" + Arrays.toString(d));
         * 
         */
        SnmpWebService_Service service = new SnmpWebService_Service();
        SnmpWebService srv = service.getSnmpWebServicePort();
        
        List<String> configs = srv.getConfigurations();
        System.out.println("++++++++++++" + configs);
        
        //boolean succes = srv.createDevice("acces", "clientNode0", "clientIp0", "clientMac0");
        boolean succes = srv.setDeviceConfig("ddd", "access.catv", 1);
        System.out.println("++++++++++++" + succes);
        
        List<String> confs = srv.getDeviceConfig("ddd");
        System.out.println("++++++++++++" + confs);
        
        //boolean succes = srv.createDevice("testCreate1", "testIpAddr1", "testMacAddr1");
        //List<String> confd = srv.getConfigurations(); 
        //System.out.println("++++++++++++" + confd);
        
        
        //boolean succes = srv.setDeviceConfig("nodeidTest", "aa"); 
    }
}
