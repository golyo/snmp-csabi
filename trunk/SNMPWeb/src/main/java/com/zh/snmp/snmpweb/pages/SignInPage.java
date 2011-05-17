/*
 *  Copyright 2010 sonrisa.
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
import com.zh.snmp.snmpweb.signin.LocalizedSignInPanel;
import org.apache.wicket.PageParameters;
import org.apache.wicket.model.Model;
/**
 *
 * @author sonrisa
 */
@MenuConfig(context={LocalizedSignInPanel.class})
public class SignInPage extends BasePage {
    private static final long serialVersionUID = 1L;

    public SignInPage(final PageParameters parameters)
    {
        super(parameters);
    }

    @Override
    public BasePanel buildContent(String id, final PageParameters parameters) {
        return new LocalizedSignInPanel(id, new Model<String>(""), true);
    }
}
