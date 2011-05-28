/*
 *  Copyright 2010 Tomi.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.zh.snmp.snmpweb.pages;

import com.zh.snmp.snmpweb.menu.MenuConfig;
import com.zh.snmp.snmpweb.pages.snmp.DeviceConfigListPanel;
import com.zh.snmp.snmpweb.pages.snmp.DeviceListPanel;
import com.zh.snmp.snmpweb.pages.snmp.SnmpPanel;
import org.apache.wicket.PageParameters;

/**
 *
 * @author golyo
 */
@MenuConfig(context={DeviceConfigListPanel.class, DeviceListPanel.class})
public class SnmpPage extends BasePage {
    private static final long serialVersionUID = 1L;

    public SnmpPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    public BasePanel buildContent(String id, final PageParameters parameters) {
        return new SnmpPanel(id, null);
    }
}
