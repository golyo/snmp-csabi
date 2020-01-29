package com.zh.snmp.snmpweb.menu;

import com.zh.snmp.snmpweb.pages.BasePanel;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MenuConfig {
    Class<? extends BasePanel>[] context();
    int depth = 0;
}
