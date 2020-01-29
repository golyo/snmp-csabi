package com.zh.snmp.snmpcore.domain;

import java.io.Serializable;

/**
 *
 * @author Golyo
 */
public class Configuration implements Serializable {
    private String code;
    private String name;
    private ConfigNode root;
    private Boolean active = Boolean.TRUE;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConfigNode getRoot() {
        return root;
    }

    public void setRoot(ConfigNode root) {
        this.root = root;
    }
    
}
