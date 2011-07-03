package com.zh.snmp.snmpweb;

import com.zh.snmp.snmpweb.pages.SnmpPage;
import com.zh.snmp.snmpweb.pages.SignInPage;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see zh.Start#main(String[])
 */
public class BaseApplication extends AuthenticatedWebApplication implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseApplication.class);
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * Constructor
     */
    private static ApplicationContext ctx;

    public BaseApplication() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
        LOGGER.debug("Application context setted");
    }

    @Override
    public Class getHomePage() {
        return SnmpPage.class;
    }

    @Override
    protected void internalInit() {
        super.internalInit();
        getApplicationSettings().setAccessDeniedPage(SignInPage.class);
        /*
        if (applicationContext == null) {
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        LOGGER.debug("Application context initialized");
        }
         * 
         */
    }

    @Override
    protected void init() {

        LOGGER.debug("JBET started");
        super.init();

        //add(new AnnotationsRoleAuthorizationStrategy(roleCheckingStrategy));
        //add(new MetaDataRoleAuthorizationStrategy(roleCheckingStrategy));
        getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));
        getSecuritySettings().setUnauthorizedComponentInstantiationListener(this);
        setupSpring();
    }

    protected void setupSpring() {
        addComponentInstantiationListener(new SpringComponentInjector(this));
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return SignInPage.class;
    }

    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return BaseSession.class;
    }

    public static BaseApplication get() {
        return (BaseApplication) WebApplication.get();
    }

    public static ApplicationContext getCtx() {
        return ctx;
    }
}
