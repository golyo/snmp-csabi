package com.zh.snmp.snmpweb.components;

import com.zh.snmp.snmpcore.entities.BaseEntity;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Golyo
 */
public abstract class ModalEditEntityPanel<T extends BaseEntity> extends ModalEditPanel<T> {
    public ModalEditEntityPanel(final ModalWindow modal, IModel<T> model) {
        super(modal, model, model.getObject().getId() != null);
    }        
}
