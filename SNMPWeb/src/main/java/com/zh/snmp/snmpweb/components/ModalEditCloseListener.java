package com.zh.snmp.snmpweb.components;

import java.io.Serializable;
import org.apache.wicket.ajax.AjaxRequestTarget;

public interface ModalEditCloseListener extends Serializable {
    public void onModalClose(AjaxRequestTarget target, boolean save, boolean delete);
}
