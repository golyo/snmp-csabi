package com.zh.snmp.snmpweb.pages;

import com.zh.snmp.snmpweb.menu.MenuConfig;
import com.zh.snmp.snmpweb.signin.LocalizedSignInPanel;
import org.apache.wicket.PageParameters;
import org.apache.wicket.model.Model;

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
