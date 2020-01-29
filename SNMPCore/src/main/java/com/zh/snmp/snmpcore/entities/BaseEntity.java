package com.zh.snmp.snmpcore.entities;

import java.io.Serializable;

public interface BaseEntity<T> extends Serializable {
    public T getId();
}
