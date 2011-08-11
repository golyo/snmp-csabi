package com.zh.snmp.snmpclient;

import com.sun.xml.ws.policy.spi.AssertionCreationException;
import com.zh.snmp.snmpclient.generated.DinamicValue;
import com.zh.snmp.snmpclient.generated.SnmpWebService;
import com.zh.snmp.snmpclient.generated.SnmpWebService_Service;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.management.ValueExp;

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
        
        //testAcces("dddd", srv);
        
        testVoip("Voip Teszt", srv);
    }

    private static void testAcces(String deviceId, SnmpWebService srv) {
        //boolean succes = srv.createDevice("acces", "clientNode0", "clientIp0", "clientMac0");
        boolean succes = srv.setDeviceConfig(deviceId, "access.internet", null, 1);
        System.out.println("++++++++++++" + succes);
        
        List<String> confs = srv.getDeviceConfig(deviceId);
        System.out.println("++++++++++++" + confs);
        
        List<DinamicValue> values = srv.getDinamicValues(deviceId, "access.internet");
        printDinamicValues(values);
        
        boolean same = srv.checkDevice(deviceId);
        System.out.println("++++++++++++SAME:" + same);
        
    }
    
    
    private static void testVoip(String deviceId, SnmpWebService srv) {
        
        List<String> confs = srv.getDeviceConfig(deviceId);
        System.out.println("++++++++++++" + confs);        

        List<DinamicValue> values = srv.getDinamicValues(deviceId, "voip.line1");
        printDinamicValues(values);
        
        setDinamicValues(values);
        printDinamicValues(values);

        boolean succes = srv.setDeviceConfig(deviceId, "voip.line1", values, 1);
        System.out.println("++++++++++++" + succes);
        
        boolean same = srv.checkDevice(deviceId);
        System.out.println("++++++++++++SAME:" + same);
    
    }
    
    private static void setDinamicValues(List<DinamicValue> values) {
        List<DinamicValue> dinamics = new LinkedList<DinamicValue>();
        setDinamicValue("voipIfAuthUser", "testwebserviceUser3", values);
        setDinamicValue("voipIfAuthPasswd", "testwebservicePwd3", values);
    }
    
    private static boolean setDinamicValue(String code, String value, List<DinamicValue> values) {
        for (DinamicValue val: values) {
            if (val.getCode().equals(code)) {
                val.setValue(value);
                return true;
            }
        }
        throw new RuntimeException("Unkonown code " + code + "; " + values.size());
    }
    
    private static DinamicValue createDinamic(String code, String value) {
        DinamicValue dv = new DinamicValue();
        dv.setCode(code);
        dv.setValue(value);
        return dv;
    } 
    
    private static void printDinamicValues(List<DinamicValue> values) {
        if (values != null) {
            System.out.println("+++++++++++DinamicSize:" + values.size());
            for (DinamicValue val: values) {
                System.out.println("+++++++++++" + val.getCode() + ":" + val.getValue());
            }
        } else {
            System.out.println("++++++++++++++Dinamic values null");
        }
    }
}
