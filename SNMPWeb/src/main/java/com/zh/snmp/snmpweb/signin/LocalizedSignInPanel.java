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

package com.zh.snmp.snmpweb.signin;

import com.zh.snmp.snmpweb.BaseSession;
import com.zh.snmp.snmpweb.pages.BasePanel;
import org.apache.wicket.authentication.panel.SignInPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sonrisa
 */
public class LocalizedSignInPanel extends BasePanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalizedSignInPanel.class);

    private static final long serialVersionUID = 1L;
    /** Field for password. */
    private PasswordTextField password;

    /** True if the user should be remembered via form persistence (cookies) */
    private boolean rememberMe = true;

    /** Field for user name. */
    private TextField<String> username;
    private final ValueMap properties = new ValueMap();

    /**
     * @param id
     *            See Component constructor
     * @param includeRememberMe
     *            True if form should include a remember-me checkbox
     * @see org.apache.wicket.Component#Component(String)
     */
    public LocalizedSignInPanel(String id, IModel<String> model, boolean includeRememberMe) {
        super(id, model);
        // Attach textfield components that edit properties map
        // in lieu of a formal beans model
        add(username = new TextField<String>("username", new PropertyModel<String>(properties,
                "username")));
        add(password = new PasswordTextField("password", new PropertyModel<String>(properties,
                "password")));

        username.setType(String.class);
        password.setType(String.class);

        // MarkupContainer row for remember me checkbox
        final WebMarkupContainer rememberMeRow = new WebMarkupContainer("rememberMeRow");
        add(rememberMeRow);

        // Add rememberMe checkbox
        rememberMeRow.add(new CheckBox("rememberMe", new PropertyModel<Boolean>(
                LocalizedSignInPanel.this, "rememberMe")));

        // Make form values persistent
        setPersistent(rememberMe);

        // Show remember me checkbox?
        rememberMeRow.setVisible(includeRememberMe);
    }

    public boolean isFormStateless() {
        return true;
    }

    @Override
    public void onSubmitForm() {
        if (signIn(getUsername(), getPassword())) {
            onSignInSucceeded();
        } else {
            onSignInFailed();
        }
    }


    /**
     * Removes persisted form data for the signin panel (forget me)
     */
    public final void forgetMe()
    {
        // Remove persisted user data. Search for child component
        // of type SignInForm and remove its related persistence values.
        getPage().removePersistedFormData(SignInPanel.SignInForm.class, true);
    }

    /**
     * Convenience method to access the password.
     *
     * @return The password
     */
    public String getPassword()
    {
        return password.getInput();
    }

    /**
     * Get model object of the rememberMe checkbox
     *
     * @return True if user should be remembered in the future
     */
    public boolean getRememberMe()
    {
        return rememberMe;
    }

    /**
     * Convenience method to access the username.
     *
     * @return The user name
     */
    public String getUsername()
    {
        return username.getDefaultModelObjectAsString();
    }

    /**
     * Convenience method set persistence for username and password.
     *
     * @param enable
     *            Whether the fields should be persistent
     */
    public void setPersistent(final boolean enable)
    {
        username.setPersistent(enable);
    }

    /**
     * Set model object for rememberMe checkbox
     *
     * @param rememberMe
     */
    public void setRememberMe(final boolean rememberMe)
    {
        this.rememberMe = rememberMe;
        setPersistent(rememberMe);
    }

    /**
     * Sign in user if possible.
     *
     * @param username
     *            The username
     * @param password
     *            The password
     * @return True if signin was successful
     */
    public boolean signIn(String username, String password)
    {
        return BaseSession.get().signIn(username, password);
    }

    protected void onSignInFailed()
    {
        error(getLocalizer().getString("signInFailed", this, "Sign in failed"));
    }

    protected void onSignInSucceeded()
    {
        if (!continueToOriginalDestination())
        {
            setResponsePage(getApplication().getHomePage());
        }
    }
}
