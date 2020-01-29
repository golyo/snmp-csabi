package com.zh.snmp.snmpweb.monitoring;

import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.message.SimpleMessageAppender;
import com.zh.snmp.snmpcore.message.ZhMessage;
import com.zh.snmp.snmpweb.pages.BasePanel;
import java.text.SimpleDateFormat;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.time.Duration;

/**
 *
 * @author Golyo
 */
public class MonitorPanel<T> extends BasePanel<T> {
    private static final String NL = "\n";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yy.MM.dd hh:mm:ss");
    
    protected MessageAppender appender;
    protected Label progressLabel;
    
    public MonitorPanel(String id, IModel<T> model) {
        this(id, model, null);
    }
    public MonitorPanel(String id, IModel<T> model, MessageAppender appender) {
        super(id, model);
        this.appender = appender != null ? appender : createMessageAppender(model);
        progressLabel = new Label("progressLabel", new ResourceModel("process.running"));
        progressLabel.setVisible(!this.appender.isFinished());
        add(progressLabel);
        if (!this.appender.isFinished()) {
            startSelfUpdatingProcess();           
        }
        add(new MultiLineLabel("processResult", new Model<String>() {
            @Override
            public String getObject() {
                return getMultiLineString();
            }
        }));
    }
    
    private void startSelfUpdatingProcess() {
        add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)) {
            @Override
            protected void onPostProcessTarget(AjaxRequestTarget target) {
                progressLabel.setVisible(!MonitorPanel.this.appender.isFinished());
                if (!canRestart()) {
                    this.stop();                    
                }
                super.onPostProcessTarget(target);
            }
        });
    }
    protected String getMultiLineString() {
        StringBuilder sb = new StringBuilder();
        for (ZhMessage mess: MonitorPanel.this.appender.getMessages()) {
            sb.append(SDF.format(mess.getDate())).append(": ");
            sb.append(new StringResourceModel(mess.getResourceKey(), this, null, mess.getParams()).getString());
            sb.append(NL);
        }
        return sb.toString();         
    }
    
    protected MessageAppender createMessageAppender(IModel<T> model) {
        return new SimpleMessageAppender();
    }
    
    protected boolean canRestart() {
        return false;
    }
}
