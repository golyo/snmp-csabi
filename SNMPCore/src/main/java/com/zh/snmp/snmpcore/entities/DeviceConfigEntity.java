package com.zh.snmp.snmpcore.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 *
 * @author Golyo
 */
@Entity
@Table(name = "DEVICECONFIG")
public class DeviceConfigEntity implements BaseEntity<String>, Serializable {
    private String code;
    private String name;
    private String snmpDescriptor;
    private Boolean active = Boolean.TRUE;


    @Basic
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Id
    @Column(name = "CODE")
    @Override
    public String getId() {
        return code;
    }

    public void setId(String code) {
        this.code = code;
    }

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "SNMPDESCRIPTOR", columnDefinition = "CLOB NOT NULL")
    public String getSnmpDescriptor() {
        return snmpDescriptor;
    }

    public void setSnmpDescriptor(String snmpDescriptor) {
        this.snmpDescriptor = snmpDescriptor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DeviceConfigEntity other = (DeviceConfigEntity) obj;
        if ((this.code == null) ? (other.code != null) : !this.code.equals(other.code)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.code != null ? this.code.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return "code: " + code;
    }
}
