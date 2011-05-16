package com.zh.snmp.snmpweb;

import com.zh.snmp.snmpweb.pages.GamePage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see zh.Start#main(String[])
 */
//public class BaseApplication extends AuthenticatedWebApplication implements ApplicationContextAware {
public class BaseApplication extends WebApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseApplication.class);

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";


    /**
     * Constructor
     */
    private ApplicationContext applicationContext;

    public BaseApplication() {
    }
/*
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        LOGGER.debug("Application context setted");
    }
*/
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    @Override
    public Class getHomePage() {
        return GamePage.class;
    }

    @Override
    protected void internalInit() {
        super.internalInit();
        if (applicationContext == null) {
            //applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            LOGGER.debug("Application context initialized");
        }
    }

    @Override
    protected void init() {
        
        LOGGER.debug("JBET started");
        super.init();

        	//add(new AnnotationsRoleAuthorizationStrategy(roleCheckingStrategy));
		//add(new MetaDataRoleAuthorizationStrategy(roleCheckingStrategy));
        //getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));
        //getSecuritySettings().setUnauthorizedComponentInstantiationListener(this);
        //setupSpring();
        /*
        AuthenticationService authsrv = ((AuthenticationService) applicationContext.getBean("AuthenticationService"));
        Player p = authsrv.findPlayer("test", "test");
        if (p == null) {
            p = new Player();
            p.setName("test");
            p.setEmail("test@test.hu");
            List<String> roles = new ArrayList<String>();
            roles.add(BaseSession.ROLE_ADMIN);
            roles.add(BaseSession.ROLE_PLAYER);
            p.setRoles(roles);
            authsrv.register(p, "test");
            LOGGER.debug("Test user created");
        }
        //Player
         * 
         */
    }

/*
    @Override
    protected void internalInit() {
        super.internalInit();
        getApplicationSettings().setAccessDeniedPage(SignInPage.class);
    }
*/
    protected void setupSpring() {
        addComponentInstantiationListener(new SpringComponentInjector(this));
    }

    /*
    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return SignInPage.class;
    }

    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return BaseSession.class;
    }
*/
    /*
    @Override
    public boolean hasAnyRole(Roles roles) {
        final Roles sessionRoles = BaseSession.get().getRoles();
        return sessionRoles != null && sessionRoles.hasAnyRole(roles);
    }

    @Override
    public void onUnauthorizedInstantiation(Component component) {
        if (component instanceof Page) {
            if (!BaseSession.get().isSignedIn()) {
                throw new RestartResponseAtInterceptPageException(SignInPage.class);
            }
        }
        throw new UnauthorizedInstantiationException(component.getClass());
    }
     * 
     */
     public static BaseApplication get() {
         return (BaseApplication)WebApplication.get();
     }
}
