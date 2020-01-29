package com.zh.snmp.snmpweb.model;

import org.apache.wicket.model.IModel;

public interface IServiceableModel<T> extends IModel<T> {
    public void save();
    public void delete();
    //public JBetServiceProvider getServiceProvider();
}
