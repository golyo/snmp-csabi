package com.zh.snmp.snmpcore.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.BasicCollection;
import org.eclipse.persistence.annotations.CollectionTable;

@Entity
@Table(name = "USERS")
public class UserEntity implements BaseEntity<Long>, Serializable {
    private Long id;
    private String name;
    private String email;
    private List<String>  roles;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return id;
    }
    protected void setId(Long id) {
        this.id = id;
    }

    @Basic
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Basic
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @BasicCollection (
        fetch=FetchType.EAGER,
        valueColumn=@Column(name="ROLE")
    )
    @CollectionTable (
        name="USERROLE",
        primaryKeyJoinColumns=
         {@PrimaryKeyJoinColumn(name="USERID", referencedColumnName="ID")}
    )
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserEntity other = (UserEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
