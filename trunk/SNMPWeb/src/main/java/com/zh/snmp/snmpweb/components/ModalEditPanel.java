/*
 *   Copyright (c) 2010 Sonrisa Informatikai Kft. All Rights Reserved.
 * 
 *  This software is the confidential and proprietary information of
 *  Sonrisa Informatikai Kft. ("Confidential Information").
 *  You shall not disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with Sonrisa.
 * 
 *  SONRISA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 *  THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SONRISA SHALL NOT BE LIABLE FOR
 *  ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 *  DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.zh.snmp.snmpweb.components;

import com.zh.snmp.snmpcore.entities.BaseEntity;
import com.zh.snmp.snmpweb.pages.BasePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author sonrisa
 */
public abstract class ModalEditPanel<T extends BaseEntity> extends Panel {
    private ModalWindow modal;
    protected static final String FORM = "form";
    protected Form form;
    protected FeedbackPanel feedback;
    protected boolean save;
    protected boolean delete;
    private ModalEditCloseListener listener;

    public ModalEditPanel(final ModalWindow modal, IModel<T> model) {
        this(modal, model, model.getObject().getId() != null);
    }
    public ModalEditPanel(final ModalWindow modal, IModel<T> model, boolean deletable) {
        super(modal.getContentId(), model);
        this.modal = modal;
        if (this instanceof ModalEditCloseListener) {
            listener = (ModalEditCloseListener)this;
        }
        form = new Form(FORM, new CompoundPropertyModel<T>(model));
        form.add(feedback = new FeedbackPanel("feedback"));
        feedback.setOutputMarkupId(true);
        add(form);
        form.add(new JBetButton("submit", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (onModalSave(target)) {
                    modal.close(target);
                    save = true;
                }
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.addComponent(feedback);
            }
        });
        form.add(new JBetButton("delete", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (onModalDelete(target)) {
                    modal.close(target);
                    delete = true;
                }
            }
        }.setDefaultFormProcessing(false).setVisible(deletable));
        form.add(new JBetButton("cancel", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                onModalCancel(target);
                modal.close(target);
            }
        }.setDefaultFormProcessing(false));
    }

    protected abstract boolean onModalSave(AjaxRequestTarget target);
    
    protected void onModalCancel(AjaxRequestTarget target) {
        
    }

    protected boolean onModalDelete(AjaxRequestTarget target) {
        return false;
    }

    public ModalEditCloseListener getEditCloseListener() {
        return listener;
    }

    public void setEditCloseListener(ModalEditCloseListener listener) {
        this.listener = listener;
    }

    public void show(AjaxRequestTarget target) {
        if (modal.isShown()) {
            return;
        }
        save = false;
        delete = false;
        modal.setContent(this);
        String titleKey = getTitleKey();
        modal.setTitle(getString(titleKey, null, titleKey));
        modal.setInitialHeight(450);
        modal.setInitialWidth(650);
        modal.setContent(this);
        if (listener != null) {
            modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                @Override
                public void onClose(AjaxRequestTarget target) {
                    modal.setWindowClosedCallback(null);
                    listener.onModalClose(target, save, delete);
                }
            });
        }
        modal.show(target);
    }

    protected String getTitleKey() {
        String s = getClass().getName();
        int p = s.lastIndexOf(".");
        s = s.substring(p+1);
        p = s.indexOf("$");
        if (p > 0) {
            s = s.substring(0, p);
        }
        return s + ".modalTitle";
    }

    public T getEntityObject() {
        return (T)getDefaultModelObject();
    }
    
    public BasePage getBasePage() {
        return (BasePage)getPage();
    }
    
}
