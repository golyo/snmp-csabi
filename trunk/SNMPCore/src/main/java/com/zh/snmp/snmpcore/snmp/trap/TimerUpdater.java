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
package com.zh.snmp.snmpcore.snmp.trap;

import com.zh.snmp.snmpcore.entities.DeviceEntity;
import com.zh.snmp.snmpcore.message.MaxMessageAppender;
import com.zh.snmp.snmpcore.message.MessageAppender;
import com.zh.snmp.snmpcore.services.DeviceService;
import com.zh.snmp.snmpcore.services.SnmpService;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Golyo
 */
public class TimerUpdater implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimerUpdater.class);
    
    @Autowired
    private SnmpService snmpService;    
    @Autowired
    private DeviceService deviceService;
    
    private Timer timer;
    private long periodInMinutes;// = 60*1000;
    private boolean running;
    private MessageAppender msgAppender;
    
    public TimerUpdater() {
        msgAppender = new MaxMessageAppender(100);
        msgAppender.finish();
    }
    public class DeviceUpdater extends TimerTask {

        @Override
        public void run() {
            msgAppender.start();
            List<DeviceEntity> devices = deviceService.getRetryUpdateDevices();
            if (devices.isEmpty()) {
                msgAppender.addMessage("TimerUpdater.updateStart", devices.size());
            } else {
                msgAppender.addMessage("TimerUpdater.updateableDeviceNotFound");
                for (DeviceEntity device: devices) {
                    snmpService.startSnmpBackgroundProcess(SnmpService.AUTO_UPDATE_USERNAME, device.getId(), msgAppender);
                }
                msgAppender.addMessage("TimerUpdater.updatefinished", devices.size());                
            }
            msgAppender.finish();
        }
        
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //start();
    }

    public long getPeriodInMinutes() {
        return periodInMinutes;
    }

    public void setPeriodInMinutes(long periodInMinutes) {
        this.periodInMinutes = periodInMinutes;
    }
 
    public void start() {
        if (running) {
            destroy();
        }
        timer = new Timer();
        long ms = periodInMinutes * 60 * 1000;
        timer.scheduleAtFixedRate(new DeviceUpdater(), ms, ms); 
        msgAppender.addMessage("TimerUpdater.start", periodInMinutes);
        running = true;
    }
    public void destroy() {
        if (timer != null) {
            timer.cancel();            
        }
        msgAppender.addMessage("TimerUpdater.finish", periodInMinutes);
        running = false;
    }
    
    public MessageAppender getMessageAppender() {
        return msgAppender;
    }
    
    public boolean isRunning() {
        return running;
    }
    
}
