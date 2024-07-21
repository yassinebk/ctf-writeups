package org.apache.catalina.startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.annotation.WebServlet;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Globals;
import org.apache.catalina.Host;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Realm;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.Wrapper;
import org.apache.catalina.authenticator.NonLoginAuthenticator;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.ContainerBase;
import org.apache.catalina.core.NamingContextListener;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.core.StandardService;
import org.apache.catalina.core.StandardWrapper;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.RealmBase;
import org.apache.catalina.security.SecurityClassLoad;
import org.apache.catalina.util.ContextName;
import org.apache.catalina.util.IOTools;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.buf.UriUtil;
import org.apache.tomcat.util.compat.JreCompat;
import org.apache.tomcat.util.descriptor.web.LoginConfig;
import org.apache.tomcat.util.file.ConfigFileLoader;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.apache.tomcat.util.modeler.Registry;
import org.apache.tomcat.util.res.StringManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/startup/Tomcat.class */
public class Tomcat {
    protected Server server;
    protected String basedir;
    private static final StringManager sm = StringManager.getManager(Tomcat.class);
    static final String[] silences = {"org.apache.coyote.http11.Http11NioProtocol", "org.apache.catalina.core.StandardService", "org.apache.catalina.core.StandardEngine", "org.apache.catalina.startup.ContextConfig", "org.apache.catalina.core.ApplicationContext", "org.apache.catalina.core.AprLifecycleListener"};
    private final Map<String, Logger> pinnedLoggers = new HashMap();
    protected int port = 8080;
    protected String hostname = "localhost";
    private final Map<String, String> userPass = new HashMap();
    private final Map<String, List<String>> userRoles = new HashMap();
    private final Map<String, Principal> userPrincipals = new HashMap();
    private boolean addDefaultWebXmlToWebapp = true;
    private boolean silent = false;

    static {
        if (JreCompat.isGraalAvailable()) {
            try {
                InputStream is = new FileInputStream(new File(System.getProperty("java.util.logging.config.file", "conf/logging.properties")));
                LogManager.getLogManager().readConfiguration(is);
                if (is != null) {
                    if (0 != 0) {
                        is.close();
                    } else {
                        is.close();
                    }
                }
            } catch (IOException | SecurityException e) {
            }
        }
    }

    public Tomcat() {
        ExceptionUtils.preload();
    }

    public void setBaseDir(String basedir) {
        this.basedir = basedir;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHostname(String s) {
        this.hostname = s;
    }

    public Context addWebapp(String contextPath, String docBase) {
        return addWebapp(getHost(), contextPath, docBase);
    }

    public Context addWebapp(String contextPath, URL source) throws IOException {
        ContextName cn = new ContextName(contextPath, (String) null);
        Host h = getHost();
        if (h.findChild(cn.getName()) != null) {
            throw new IllegalArgumentException(sm.getString("tomcat.addWebapp.conflictChild", source, contextPath, cn.getName()));
        }
        File targetWar = new File(h.getAppBaseFile(), cn.getBaseName() + ".war");
        File targetDir = new File(h.getAppBaseFile(), cn.getBaseName());
        if (targetWar.exists()) {
            throw new IllegalArgumentException(sm.getString("tomcat.addWebapp.conflictFile", source, contextPath, targetWar.getAbsolutePath()));
        }
        if (targetDir.exists()) {
            throw new IllegalArgumentException(sm.getString("tomcat.addWebapp.conflictFile", source, contextPath, targetDir.getAbsolutePath()));
        }
        URLConnection uConn = source.openConnection();
        InputStream is = uConn.getInputStream();
        Throwable th = null;
        try {
            OutputStream os = new FileOutputStream(targetWar);
            IOTools.flow(is, os);
            if (os != null) {
                if (0 != 0) {
                    os.close();
                } else {
                    os.close();
                }
            }
            if (is != null) {
                if (0 != 0) {
                    try {
                        is.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    is.close();
                }
            }
            return addWebapp(contextPath, targetWar.getAbsolutePath());
        } finally {
        }
    }

    public Context addContext(String contextPath, String docBase) {
        return addContext(getHost(), contextPath, docBase);
    }

    public Wrapper addServlet(String contextPath, String servletName, String servletClass) {
        Container ctx = getHost().findChild(contextPath);
        return addServlet((Context) ctx, servletName, servletClass);
    }

    public static Wrapper addServlet(Context ctx, String servletName, String servletClass) {
        Wrapper sw = ctx.createWrapper();
        sw.setServletClass(servletClass);
        sw.setName(servletName);
        ctx.addChild(sw);
        return sw;
    }

    public Wrapper addServlet(String contextPath, String servletName, Servlet servlet) {
        Container ctx = getHost().findChild(contextPath);
        return addServlet((Context) ctx, servletName, servlet);
    }

    public static Wrapper addServlet(Context ctx, String servletName, Servlet servlet) {
        Wrapper sw = new ExistingStandardWrapper(servlet);
        sw.setName(servletName);
        ctx.addChild(sw);
        return sw;
    }

    public void init(ConfigurationSource source) {
        ConfigFileLoader.setSource(source);
        this.addDefaultWebXmlToWebapp = false;
        Catalina catalina = new Catalina();
        catalina.load();
        this.server = catalina.getServer();
    }

    public void init() throws LifecycleException {
        getServer();
        this.server.init();
    }

    public void start() throws LifecycleException {
        getServer();
        this.server.start();
    }

    public void stop() throws LifecycleException {
        getServer();
        this.server.stop();
    }

    public void destroy() throws LifecycleException {
        getServer();
        this.server.destroy();
    }

    public void addUser(String user, String pass) {
        this.userPass.put(user, pass);
    }

    public void addRole(String user, String role) {
        List<String> roles = this.userRoles.get(user);
        if (roles == null) {
            roles = new ArrayList<>();
            this.userRoles.put(user, roles);
        }
        roles.add(role);
    }

    public Connector getConnector() {
        Service service = getService();
        if (service.findConnectors().length > 0) {
            return service.findConnectors()[0];
        }
        Connector connector = new Connector(org.apache.coyote.http11.Constants.HTTP_11);
        connector.setPort(this.port);
        service.addConnector(connector);
        return connector;
    }

    public void setConnector(Connector connector) {
        Service service = getService();
        boolean found = false;
        Connector[] findConnectors = service.findConnectors();
        int length = findConnectors.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            Connector serviceConnector = findConnectors[i];
            if (connector != serviceConnector) {
                i++;
            } else {
                found = true;
                break;
            }
        }
        if (!found) {
            service.addConnector(connector);
        }
    }

    public Service getService() {
        return getServer().findServices()[0];
    }

    public void setHost(Host host) {
        Engine engine = getEngine();
        boolean found = false;
        Container[] findChildren = engine.findChildren();
        int length = findChildren.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            Container engineHost = findChildren[i];
            if (engineHost != host) {
                i++;
            } else {
                found = true;
                break;
            }
        }
        if (!found) {
            engine.addChild(host);
        }
    }

    public Host getHost() {
        Engine engine = getEngine();
        if (engine.findChildren().length > 0) {
            return (Host) engine.findChildren()[0];
        }
        Host host = new StandardHost();
        host.setName(this.hostname);
        getEngine().addChild(host);
        return host;
    }

    public Engine getEngine() {
        Service service = getServer().findServices()[0];
        if (service.getContainer() != null) {
            return service.getContainer();
        }
        Engine engine = new StandardEngine();
        engine.setName("Tomcat");
        engine.setDefaultHost(this.hostname);
        engine.setRealm(createDefaultRealm());
        service.setContainer(engine);
        return engine;
    }

    public Server getServer() {
        if (this.server != null) {
            return this.server;
        }
        System.setProperty("catalina.useNaming", "false");
        this.server = new StandardServer();
        initBaseDir();
        ConfigFileLoader.setSource(new CatalinaBaseConfigurationSource(new File(this.basedir), null));
        this.server.setPort(-1);
        Service service = new StandardService();
        service.setName("Tomcat");
        this.server.addService(service);
        return this.server;
    }

    public Context addContext(Host host, String contextPath, String dir) {
        return addContext(host, contextPath, contextPath, dir);
    }

    public Context addContext(Host host, String contextPath, String contextName, String dir) {
        silence(host, contextName);
        Context ctx = createContext(host, contextPath);
        ctx.setName(contextName);
        ctx.setPath(contextPath);
        ctx.setDocBase(dir);
        ctx.addLifecycleListener(new FixContextListener());
        if (host == null) {
            getHost().addChild(ctx);
        } else {
            host.addChild(ctx);
        }
        return ctx;
    }

    public Context addWebapp(Host host, String contextPath, String docBase) {
        try {
            Class<?> clazz = Class.forName(getHost().getConfigClass());
            LifecycleListener listener = (LifecycleListener) clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
            return addWebapp(host, contextPath, docBase, listener);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Context addWebapp(Host host, String contextPath, String docBase, LifecycleListener config) {
        silence(host, contextPath);
        Context ctx = createContext(host, contextPath);
        ctx.setPath(contextPath);
        ctx.setDocBase(docBase);
        if (this.addDefaultWebXmlToWebapp) {
            ctx.addLifecycleListener(getDefaultWebXmlListener());
        }
        ctx.setConfigFile(getWebappConfigFile(docBase, contextPath));
        ctx.addLifecycleListener(config);
        if (this.addDefaultWebXmlToWebapp && (config instanceof ContextConfig)) {
            ((ContextConfig) config).setDefaultWebXml(noDefaultWebXmlPath());
        }
        if (host == null) {
            getHost().addChild(ctx);
        } else {
            host.addChild(ctx);
        }
        return ctx;
    }

    public LifecycleListener getDefaultWebXmlListener() {
        return new DefaultWebXmlListener();
    }

    public String noDefaultWebXmlPath() {
        return Constants.NoDefaultWebXml;
    }

    protected Realm createDefaultRealm() {
        return new SimpleRealm();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/startup/Tomcat$SimpleRealm.class */
    public class SimpleRealm extends RealmBase {
        private SimpleRealm() {
        }

        @Override // org.apache.catalina.realm.RealmBase
        protected String getPassword(String username) {
            return (String) Tomcat.this.userPass.get(username);
        }

        @Override // org.apache.catalina.realm.RealmBase
        protected Principal getPrincipal(String username) {
            String pass;
            GenericPrincipal p = (Principal) Tomcat.this.userPrincipals.get(username);
            if (p == null && (pass = (String) Tomcat.this.userPass.get(username)) != null) {
                p = new GenericPrincipal(username, pass, (List) Tomcat.this.userRoles.get(username));
                Tomcat.this.userPrincipals.put(username, p);
            }
            return p;
        }
    }

    protected void initBaseDir() {
        String catalinaHome = System.getProperty(Globals.CATALINA_HOME_PROP);
        if (this.basedir == null) {
            this.basedir = System.getProperty("catalina.base");
        }
        if (this.basedir == null) {
            this.basedir = catalinaHome;
        }
        if (this.basedir == null) {
            this.basedir = System.getProperty("user.dir") + "/tomcat." + this.port;
        }
        File baseFile = new File(this.basedir);
        if (baseFile.exists()) {
            if (!baseFile.isDirectory()) {
                throw new IllegalArgumentException(sm.getString("tomcat.baseDirNotDir", baseFile));
            }
        } else if (!baseFile.mkdirs()) {
            throw new IllegalStateException(sm.getString("tomcat.baseDirMakeFail", baseFile));
        }
        try {
            baseFile = baseFile.getCanonicalFile();
        } catch (IOException e) {
            baseFile = baseFile.getAbsoluteFile();
        }
        this.server.setCatalinaBase(baseFile);
        System.setProperty("catalina.base", baseFile.getPath());
        this.basedir = baseFile.getPath();
        if (catalinaHome == null) {
            this.server.setCatalinaHome(baseFile);
        } else {
            File homeFile = new File(catalinaHome);
            if (!homeFile.isDirectory() && !homeFile.mkdirs()) {
                throw new IllegalStateException(sm.getString("tomcat.homeDirMakeFail", homeFile));
            }
            try {
                homeFile = homeFile.getCanonicalFile();
            } catch (IOException e2) {
                homeFile = homeFile.getAbsoluteFile();
            }
            this.server.setCatalinaHome(homeFile);
        }
        System.setProperty(Globals.CATALINA_HOME_PROP, this.server.getCatalinaHome().getPath());
    }

    public void setSilent(boolean silent) {
        String[] strArr;
        this.silent = silent;
        for (String s : silences) {
            Logger logger = Logger.getLogger(s);
            this.pinnedLoggers.put(s, logger);
            if (silent) {
                logger.setLevel(Level.WARNING);
            } else {
                logger.setLevel(Level.INFO);
            }
        }
    }

    private void silence(Host host, String contextPath) {
        String loggerName = getLoggerName(host, contextPath);
        Logger logger = Logger.getLogger(loggerName);
        this.pinnedLoggers.put(loggerName, logger);
        if (this.silent) {
            logger.setLevel(Level.WARNING);
        } else {
            logger.setLevel(Level.INFO);
        }
    }

    public void setAddDefaultWebXmlToWebapp(boolean addDefaultWebXmlToWebapp) {
        this.addDefaultWebXmlToWebapp = addDefaultWebXmlToWebapp;
    }

    private String getLoggerName(Host host, String contextName) {
        if (host == null) {
            host = getHost();
        }
        StringBuilder loggerName = new StringBuilder();
        loggerName.append(ContainerBase.class.getName());
        loggerName.append(".[");
        loggerName.append(host.getParent().getName());
        loggerName.append("].[");
        loggerName.append(host.getName());
        loggerName.append("].[");
        if (contextName == null || contextName.equals("")) {
            loggerName.append("/");
        } else if (contextName.startsWith("##")) {
            loggerName.append("/");
            loggerName.append(contextName);
        }
        loggerName.append(']');
        return loggerName.toString();
    }

    private Context createContext(Host host, String url) {
        String contextClass = StandardContext.class.getName();
        if (host == null) {
            host = getHost();
        }
        if (host instanceof StandardHost) {
            contextClass = ((StandardHost) host).getContextClass();
        }
        try {
            return (Context) Class.forName(contextClass).getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (IllegalArgumentException | ReflectiveOperationException | SecurityException e) {
            throw new IllegalArgumentException(sm.getString("tomcat.noContextClass", contextClass, host, url), e);
        }
    }

    public void enableNaming() {
        getServer();
        this.server.addLifecycleListener(new NamingContextListener());
        System.setProperty("catalina.useNaming", "true");
        String value = "org.apache.naming";
        String oldValue = System.getProperty("java.naming.factory.url.pkgs");
        if (oldValue != null) {
            if (oldValue.contains(value)) {
                value = oldValue;
            } else {
                value = value + ":" + oldValue;
            }
        }
        System.setProperty("java.naming.factory.url.pkgs", value);
        if (System.getProperty("java.naming.factory.initial") == null) {
            System.setProperty("java.naming.factory.initial", "org.apache.naming.java.javaURLContextFactory");
        }
    }

    public void initWebappDefaults(String contextPath) {
        Container ctx = getHost().findChild(contextPath);
        initWebappDefaults((Context) ctx);
    }

    public static void initWebappDefaults(Context ctx) {
        Wrapper servlet = addServlet(ctx, "default", "org.apache.catalina.servlets.DefaultServlet");
        servlet.setLoadOnStartup(1);
        servlet.setOverridable(true);
        Wrapper servlet2 = addServlet(ctx, "jsp", org.apache.catalina.core.Constants.JSP_SERVLET_CLASS);
        servlet2.addInitParameter("fork", "false");
        servlet2.setLoadOnStartup(3);
        servlet2.setOverridable(true);
        ctx.addServletMappingDecoded("/", "default");
        ctx.addServletMappingDecoded("*.jsp", "jsp");
        ctx.addServletMappingDecoded("*.jspx", "jsp");
        ctx.setSessionTimeout(30);
        addDefaultMimeTypeMappings(ctx);
        ctx.addWelcomeFile("index.html");
        ctx.addWelcomeFile("index.htm");
        ctx.addWelcomeFile("index.jsp");
    }

    public static void addDefaultMimeTypeMappings(Context context) {
        Properties defaultMimeMappings = new Properties();
        try {
            InputStream is = Tomcat.class.getResourceAsStream("MimeTypeMappings.properties");
            defaultMimeMappings.load(is);
            for (Map.Entry<Object, Object> entry : defaultMimeMappings.entrySet()) {
                context.addMimeMapping((String) entry.getKey(), (String) entry.getValue());
            }
            if (is != null) {
                if (0 != 0) {
                    is.close();
                } else {
                    is.close();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(sm.getString("tomcat.defaultMimeTypeMappingsFail"), e);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/startup/Tomcat$FixContextListener.class */
    public static class FixContextListener implements LifecycleListener {
        @Override // org.apache.catalina.LifecycleListener
        public void lifecycleEvent(LifecycleEvent event) {
            try {
                Context context = (Context) event.getLifecycle();
                if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
                    context.setConfigured(true);
                    WebAnnotationSet.loadApplicationAnnotations(context);
                    if (context.getLoginConfig() == null) {
                        context.setLoginConfig(new LoginConfig("NONE", null, null, null));
                        context.getPipeline().addValve(new NonLoginAuthenticator());
                    }
                }
            } catch (ClassCastException e) {
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/startup/Tomcat$DefaultWebXmlListener.class */
    public static class DefaultWebXmlListener implements LifecycleListener {
        @Override // org.apache.catalina.LifecycleListener
        public void lifecycleEvent(LifecycleEvent event) {
            if (Lifecycle.BEFORE_START_EVENT.equals(event.getType())) {
                Tomcat.initWebappDefaults((Context) event.getLifecycle());
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/startup/Tomcat$ExistingStandardWrapper.class */
    public static class ExistingStandardWrapper extends StandardWrapper {
        private final Servlet existing;

        public ExistingStandardWrapper(Servlet existing) {
            this.existing = existing;
            if (existing instanceof SingleThreadModel) {
                this.singleThreadModel = true;
                this.instancePool = new Stack<>();
            }
            this.asyncSupported = hasAsync(existing);
        }

        private static boolean hasAsync(Servlet existing) {
            boolean result = false;
            Class<?> clazz = existing.getClass();
            WebServlet ws = (WebServlet) clazz.getAnnotation(WebServlet.class);
            if (ws != null) {
                result = ws.asyncSupported();
            }
            return result;
        }

        @Override // org.apache.catalina.core.StandardWrapper
        public synchronized Servlet loadServlet() throws ServletException {
            if (this.singleThreadModel) {
                try {
                    Servlet instance = (Servlet) this.existing.getClass().getConstructor(new Class[0]).newInstance(new Object[0]);
                    instance.init(this.facade);
                    return instance;
                } catch (ReflectiveOperationException e) {
                    throw new ServletException(e);
                }
            }
            if (!this.instanceInitialized) {
                this.existing.init(this.facade);
                this.instanceInitialized = true;
            }
            return this.existing;
        }

        @Override // org.apache.catalina.core.StandardWrapper, org.apache.catalina.Wrapper
        public long getAvailable() {
            return 0L;
        }

        @Override // org.apache.catalina.core.StandardWrapper, org.apache.catalina.Wrapper
        public boolean isUnavailable() {
            return false;
        }

        @Override // org.apache.catalina.core.StandardWrapper, org.apache.catalina.Wrapper
        public Servlet getServlet() {
            return this.existing;
        }

        @Override // org.apache.catalina.core.StandardWrapper, org.apache.catalina.Wrapper
        public String getServletClass() {
            return this.existing.getClass().getName();
        }
    }

    protected URL getWebappConfigFile(String path, String contextName) {
        File docBase = new File(path);
        if (docBase.isDirectory()) {
            return getWebappConfigFileFromDirectory(docBase, contextName);
        }
        return getWebappConfigFileFromWar(docBase, contextName);
    }

    private URL getWebappConfigFileFromDirectory(File docBase, String contextName) {
        URL result = null;
        File webAppContextXml = new File(docBase, Constants.ApplicationContextXml);
        if (webAppContextXml.exists()) {
            try {
                result = webAppContextXml.toURI().toURL();
            } catch (MalformedURLException e) {
                Logger.getLogger(getLoggerName(getHost(), contextName)).log(Level.WARNING, sm.getString("tomcat.noContextXml", docBase), (Throwable) e);
            }
        }
        return result;
    }

    private URL getWebappConfigFileFromWar(File docBase, String contextName) {
        URL result = null;
        try {
            JarFile jar = new JarFile(docBase);
            JarEntry entry = jar.getJarEntry(Constants.ApplicationContextXml);
            if (entry != null) {
                result = UriUtil.buildJarUrl(docBase, Constants.ApplicationContextXml);
            }
            if (jar != null) {
                if (0 != 0) {
                    jar.close();
                } else {
                    jar.close();
                }
            }
        } catch (IOException e) {
            Logger.getLogger(getLoggerName(getHost(), contextName)).log(Level.WARNING, sm.getString("tomcat.noContextXml", docBase), (Throwable) e);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        for (String arg : args) {
            if (arg.equals("--no-jmx")) {
                Registry.disableRegistry();
            }
        }
        SecurityClassLoad.securityClassLoad(Thread.currentThread().getContextClassLoader());
        Tomcat tomcat = new Tomcat();
        tomcat.init(null);
        boolean await = false;
        String path = "";
        int i = 0;
        while (i < args.length) {
            if (args[i].equals("--war")) {
                i++;
                if (i >= args.length) {
                    throw new IllegalArgumentException(sm.getString("tomcat.invalidCommandLine", args[i - 1]));
                }
                File war = new File(args[i]);
                tomcat.addWebapp(path, war.getAbsolutePath());
            } else if (args[i].equals("--path")) {
                i++;
                if (i >= args.length) {
                    throw new IllegalArgumentException(sm.getString("tomcat.invalidCommandLine", args[i - 1]));
                }
                path = args[i];
            } else if (args[i].equals("--await")) {
                await = true;
            } else if (!args[i].equals("--no-jmx")) {
                throw new IllegalArgumentException(sm.getString("tomcat.invalidCommandLine", args[i]));
            }
            i++;
        }
        tomcat.start();
        if (await) {
            tomcat.getServer().await();
        }
    }
}
