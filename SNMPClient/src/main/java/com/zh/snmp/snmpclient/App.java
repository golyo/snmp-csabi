package com.zh.snmp.snmpclient;

import com.zh.snmp.snmpclient.generated.TryWs;
import com.zh.snmp.snmpclient.generated.TryWs_Service;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
         TryWs_Service service = new TryWs_Service();
         TryWs srv = service.getTryWsPort();
         String s = srv.hello("alma");
         System.out.println(s);
    }
}
