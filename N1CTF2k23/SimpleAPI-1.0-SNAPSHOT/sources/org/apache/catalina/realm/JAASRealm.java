package org.apache.catalina.realm;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.Container;
import org.apache.catalina.LifecycleException;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/realm/JAASRealm.class */
public class JAASRealm extends RealmBase {
    private static final Log log = LogFactory.getLog(JAASRealm.class);
    protected String configFile;
    protected volatile Configuration jaasConfiguration;
    protected String appName = null;
    protected final List<String> roleClasses = new ArrayList();
    protected final List<String> userClasses = new ArrayList();
    protected boolean useContextClassLoader = true;
    protected volatile boolean jaasConfigurationLoaded = false;
    private volatile boolean invocationSuccess = true;
    protected String roleClassNames = null;
    protected String userClassNames = null;

    public String getConfigFile() {
        return this.configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public void setAppName(String name) {
        this.appName = name;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setUseContextClassLoader(boolean useContext) {
        this.useContextClassLoader = useContext;
    }

    public boolean isUseContextClassLoader() {
        return this.useContextClassLoader;
    }

    @Override // org.apache.catalina.realm.RealmBase, org.apache.catalina.Contained
    public void setContainer(Container container) {
        super.setContainer(container);
        if (this.appName == null) {
            this.appName = makeLegalForJAAS(container.getName());
            log.info(sm.getString("jaasRealm.appName", this.appName));
        }
    }

    public String getRoleClassNames() {
        return this.roleClassNames;
    }

    public void setRoleClassNames(String roleClassNames) {
        this.roleClassNames = roleClassNames;
    }

    protected void parseClassNames(String classNamesString, List<String> classNamesList) {
        classNamesList.clear();
        if (classNamesString == null) {
            return;
        }
        ClassLoader loader = getClass().getClassLoader();
        if (isUseContextClassLoader()) {
            loader = Thread.currentThread().getContextClassLoader();
        }
        String[] classNames = classNamesString.split("[ ]*,[ ]*");
        for (String className : classNames) {
            if (className.length() != 0) {
                try {
                    Class<?> principalClass = Class.forName(className, false, loader);
                    if (Principal.class.isAssignableFrom(principalClass)) {
                        classNamesList.add(className);
                    } else {
                        log.error(sm.getString("jaasRealm.notPrincipal", className));
                    }
                } catch (ClassNotFoundException e) {
                    log.error(sm.getString("jaasRealm.classNotFound", className));
                }
            }
        }
    }

    public String getUserClassNames() {
        return this.userClassNames;
    }

    public void setUserClassNames(String userClassNames) {
        this.userClassNames = userClassNames;
    }

    @Override // org.apache.catalina.realm.RealmBase, org.apache.catalina.Realm
    public Principal authenticate(String username, String credentials) {
        return authenticate(username, new JAASCallbackHandler(this, username, credentials));
    }

    @Override // org.apache.catalina.realm.RealmBase, org.apache.catalina.Realm
    public Principal authenticate(String username, String clientDigest, String nonce, String nc, String cnonce, String qop, String realmName, String md5a2) {
        return authenticate(username, new JAASCallbackHandler(this, username, clientDigest, nonce, nc, cnonce, qop, realmName, md5a2, HttpServletRequest.DIGEST_AUTH));
    }

    protected Principal authenticate(String username, CallbackHandler callbackHandler) {
        try {
            if (this.appName == null) {
                this.appName = "Tomcat";
            }
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("jaasRealm.beginLogin", username, this.appName));
            }
            ClassLoader ocl = null;
            if (!isUseContextClassLoader()) {
                ocl = Thread.currentThread().getContextClassLoader();
                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            }
            Configuration config = getConfig();
            LoginContext loginContext = new LoginContext(this.appName, (Subject) null, callbackHandler, config);
            if (!isUseContextClassLoader()) {
                Thread.currentThread().setContextClassLoader(ocl);
            }
            if (log.isDebugEnabled()) {
                log.debug("Login context created " + username);
            }
            try {
                try {
                    try {
                        try {
                            loginContext.login();
                            Subject subject = loginContext.getSubject();
                            this.invocationSuccess = true;
                            if (subject == null) {
                                if (log.isDebugEnabled()) {
                                    log.debug(sm.getString("jaasRealm.failedLogin", username));
                                    return null;
                                }
                                return null;
                            }
                            if (log.isDebugEnabled()) {
                                log.debug(sm.getString("jaasRealm.loginContextCreated", username));
                            }
                            Principal principal = createPrincipal(username, subject, loginContext);
                            if (principal == null) {
                                log.debug(sm.getString("jaasRealm.authenticateFailure", username));
                                return null;
                            }
                            if (log.isDebugEnabled()) {
                                log.debug(sm.getString("jaasRealm.authenticateSuccess", username, principal));
                            }
                            return principal;
                        } catch (CredentialExpiredException e) {
                            if (log.isDebugEnabled()) {
                                log.debug(sm.getString("jaasRealm.credentialExpired", username));
                            }
                            this.invocationSuccess = true;
                            return null;
                        }
                    } catch (LoginException e2) {
                        log.warn(sm.getString("jaasRealm.loginException", username), e2);
                        this.invocationSuccess = true;
                        return null;
                    }
                } catch (AccountExpiredException e3) {
                    if (log.isDebugEnabled()) {
                        log.debug(sm.getString("jaasRealm.accountExpired", username));
                    }
                    this.invocationSuccess = true;
                    return null;
                }
            } catch (FailedLoginException e4) {
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("jaasRealm.failedLogin", username));
                }
                this.invocationSuccess = true;
                return null;
            }
        } catch (Throwable t) {
            log.error("error ", t);
            this.invocationSuccess = false;
            return null;
        }
    }

    @Override // org.apache.catalina.realm.RealmBase
    protected String getPassword(String username) {
        return null;
    }

    @Override // org.apache.catalina.realm.RealmBase
    protected Principal getPrincipal(String username) {
        return authenticate(username, new JAASCallbackHandler(this, username, null, null, null, null, null, null, null, HttpServletRequest.CLIENT_CERT_AUTH));
    }

    protected Principal createPrincipal(String username, Subject subject, LoginContext loginContext) {
        List<String> roles = new ArrayList<>();
        Principal userPrincipal = null;
        for (Principal principal : subject.getPrincipals()) {
            String principalClass = principal.getClass().getName();
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("jaasRealm.checkPrincipal", principal, principalClass));
            }
            if (userPrincipal == null && this.userClasses.contains(principalClass)) {
                userPrincipal = principal;
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("jaasRealm.userPrincipalSuccess", principal.getName()));
                }
            }
            if (this.roleClasses.contains(principalClass)) {
                roles.add(principal.getName());
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("jaasRealm.rolePrincipalAdd", principal.getName()));
                }
            }
        }
        if (userPrincipal == null) {
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("jaasRealm.userPrincipalFailure"));
                log.debug(sm.getString("jaasRealm.rolePrincipalFailure"));
                return null;
            }
            return null;
        }
        if (roles.size() == 0 && log.isDebugEnabled()) {
            log.debug(sm.getString("jaasRealm.rolePrincipalFailure"));
        }
        return new GenericPrincipal(username, null, roles, userPrincipal, loginContext);
    }

    protected String makeLegalForJAAS(String src) {
        String result = src;
        if (result == null) {
            result = "other";
        }
        if (result.startsWith("/")) {
            result = result.substring(1);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.catalina.realm.RealmBase, org.apache.catalina.util.LifecycleBase
    public void startInternal() throws LifecycleException {
        parseClassNames(this.userClassNames, this.userClasses);
        parseClassNames(this.roleClassNames, this.roleClasses);
        super.startInternal();
    }

    protected Configuration getConfig() {
        String configFile = this.configFile;
        try {
            try {
                if (this.jaasConfigurationLoaded) {
                    return this.jaasConfiguration;
                }
                synchronized (this) {
                    if (configFile == null) {
                        this.jaasConfigurationLoaded = true;
                        return null;
                    }
                    URL resource = Thread.currentThread().getContextClassLoader().getResource(configFile);
                    URI uri = resource.toURI();
                    Configuration config = (Configuration) Class.forName("com.sun.security.auth.login.ConfigFile").getConstructor(URI.class).newInstance(uri);
                    this.jaasConfiguration = config;
                    this.jaasConfigurationLoaded = true;
                    return this.jaasConfiguration;
                }
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex.getCause());
            }
        } catch (IllegalArgumentException | ReflectiveOperationException | SecurityException | URISyntaxException ex2) {
            throw new RuntimeException(ex2);
        }
    }

    @Override // org.apache.catalina.Realm
    public boolean isAvailable() {
        return this.invocationSuccess;
    }
}
