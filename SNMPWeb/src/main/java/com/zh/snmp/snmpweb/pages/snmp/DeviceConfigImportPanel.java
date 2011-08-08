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
package com.zh.snmp.snmpweb.pages.snmp;

import com.zh.snmp.snmpcore.message.SimpleMessageAppender;
import com.zh.snmp.snmpcore.message.ZhMessage;
import com.zh.snmp.snmpcore.services.ConfigService;
import com.zh.snmp.snmpweb.components.JBetButton;
import com.zh.snmp.snmpweb.pages.BasePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Golyo
 */
public class DeviceConfigImportPanel extends Panel {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceConfigImportPanel.class);
    
    @SpringBean
    private ConfigService service;
    
    private FileUploadField file;
    private ModalWindow modal;

    public DeviceConfigImportPanel(final ModalWindow modal) {
        super(modal.getContentId());
        this.modal = modal;
        Form form = new Form("form");
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        form.add(feedback);
        feedback.setOutputMarkupId(true);
        form.add(file = new FileUploadField("uploadFile", new Model<FileUpload>()));
        add(form);
        form.add(new JBetButton("upload", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                FileUpload upload = file.getFileUpload();
                SimpleMessageAppender appender = new SimpleMessageAppender();
                try {
                    service.importConfiguration(upload.getInputStream(), appender);                   
                } catch (Exception e) {
                    appender.addMessage("error.import.config");
                    LOGGER.error("Hiba konfig upload közben", e);
                }                
                if (appender.getMessages().isEmpty()) {
                    getBasePage().refreshPanel(target);
                    modal.close(target);                    
                } else {
                    for (ZhMessage mess: appender.getMessages()) {
                        error(new StringResourceModel(mess.getResourceKey(), this, null, mess.getParams()).getString());
                    }
                    target.addComponent(feedback);                                        
                }
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.addComponent(feedback);
            }
        });
        form.add(new JBetButton("cancel", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                modal.close(target);
            }
        }.setDefaultFormProcessing(false));
    }

    public void show(AjaxRequestTarget target) {
        if (modal.isShown()) {
            return;
        }
        modal.setContent(this);
        String titleKey = getTitleKey();
        modal.setTitle(getString(titleKey, null, titleKey));
        modal.setInitialHeight(450);
        modal.setInitialWidth(650);
        modal.setContent(this);
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

    public BasePage getBasePage() {
        return (BasePage)getPage();
    }
    
}
