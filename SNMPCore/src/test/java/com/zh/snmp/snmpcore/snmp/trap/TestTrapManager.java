package com.zh.snmp.snmpcore.snmp.trap;

import com.zh.snmp.snmpcore.BaseTest;
import java.io.IOException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class TestTrapManager extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestTrapManager.class);

    @Autowired
    private TrapManager trapManager;
    
    @Test
    public void testManager() throws IOException, InterruptedException {
        /*
        trapManager.start();
        for (int i=0; i<1; i++) {
            LOGGER.debug("Wait " + i);
            Thread.sleep(1000);
        }
        trapManager.stop();
         * 
         */
    }        
}
