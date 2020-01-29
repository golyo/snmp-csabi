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

public abstract class ModalEditPanel<T> extends Panel {
    private ModalWindow modal;
    protected static final String FORM = "form";
    protected Form form;
    protected FeedbackPanel feedback;
    protected boolean save;
    protected boolean delete;
    private ModalEditCloseListener listener;

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
                } else {
                    target.addComponent(feedback);
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
