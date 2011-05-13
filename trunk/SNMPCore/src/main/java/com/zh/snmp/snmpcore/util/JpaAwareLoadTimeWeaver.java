package com.zh.snmp.snmpcore.util;

import org.springframework.instrument.classloading.SimpleLoadTimeWeaver;

/**
 * @author Palesz
 */
public class JpaAwareLoadTimeWeaver extends SimpleLoadTimeWeaver {
    @Override
    public ClassLoader getInstrumentableClassLoader() {
        ClassLoader instrumentableClassLoader = super.getInstrumentableClassLoader();
        if (instrumentableClassLoader.getClass().getName().endsWith("SimpleInstrumentableClassLoader")) {
            return instrumentableClassLoader.getParent();
        } else {
            return instrumentableClassLoader;
        }
    }
}
