package org.springframework.boot.web.embedded.tomcat;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.ServletContainerInitializer;
import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Manager;
import org.apache.catalina.Valve;
import org.apache.catalina.WebResource;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.session.StandardManager;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.util.LifecycleBase;
import org.apache.catalina.webresources.AbstractResourceSet;
import org.apache.catalina.webresources.EmptyResource;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http2.Http2Protocol;
import org.apache.tomcat.util.modeler.Registry;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.boot.util.LambdaSafe;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/tomcat/TomcatServletWebServerFactory.class */
public class TomcatServletWebServerFactory extends AbstractServletWebServerFactory implements ConfigurableTomcatWebServerFactory, ResourceLoaderAware {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Set<Class<?>> NO_CLASSES = Collections.emptySet();
    public static final String DEFAULT_PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";
    private File baseDirectory;
    private List<Valve> engineValves;
    private List<Valve> contextValves;
    private List<LifecycleListener> contextLifecycleListeners;
    private Set<TomcatContextCustomizer> tomcatContextCustomizers;
    private Set<TomcatConnectorCustomizer> tomcatConnectorCustomizers;
    private Set<TomcatProtocolHandlerCustomizer<?>> tomcatProtocolHandlerCustomizers;
    private final List<Connector> additionalTomcatConnectors;
    private ResourceLoader resourceLoader;
    private String protocol;
    private Set<String> tldSkipPatterns;
    private Charset uriEncoding;
    private int backgroundProcessorDelay;
    private boolean disableMBeanRegistry;

    public TomcatServletWebServerFactory() {
        this.engineValves = new ArrayList();
        this.contextValves = new ArrayList();
        this.contextLifecycleListeners = getDefaultLifecycleListeners();
        this.tomcatContextCustomizers = new LinkedHashSet();
        this.tomcatConnectorCustomizers = new LinkedHashSet();
        this.tomcatProtocolHandlerCustomizers = new LinkedHashSet();
        this.additionalTomcatConnectors = new ArrayList();
        this.protocol = "org.apache.coyote.http11.Http11NioProtocol";
        this.tldSkipPatterns = new LinkedHashSet(TldSkipPatterns.DEFAULT);
        this.uriEncoding = DEFAULT_CHARSET;
        this.disableMBeanRegistry = true;
    }

    public TomcatServletWebServerFactory(int port) {
        super(port);
        this.engineValves = new ArrayList();
        this.contextValves = new ArrayList();
        this.contextLifecycleListeners = getDefaultLifecycleListeners();
        this.tomcatContextCustomizers = new LinkedHashSet();
        this.tomcatConnectorCustomizers = new LinkedHashSet();
        this.tomcatProtocolHandlerCustomizers = new LinkedHashSet();
        this.additionalTomcatConnectors = new ArrayList();
        this.protocol = "org.apache.coyote.http11.Http11NioProtocol";
        this.tldSkipPatterns = new LinkedHashSet(TldSkipPatterns.DEFAULT);
        this.uriEncoding = DEFAULT_CHARSET;
        this.disableMBeanRegistry = true;
    }

    public TomcatServletWebServerFactory(String contextPath, int port) {
        super(contextPath, port);
        this.engineValves = new ArrayList();
        this.contextValves = new ArrayList();
        this.contextLifecycleListeners = getDefaultLifecycleListeners();
        this.tomcatContextCustomizers = new LinkedHashSet();
        this.tomcatConnectorCustomizers = new LinkedHashSet();
        this.tomcatProtocolHandlerCustomizers = new LinkedHashSet();
        this.additionalTomcatConnectors = new ArrayList();
        this.protocol = "org.apache.coyote.http11.Http11NioProtocol";
        this.tldSkipPatterns = new LinkedHashSet(TldSkipPatterns.DEFAULT);
        this.uriEncoding = DEFAULT_CHARSET;
        this.disableMBeanRegistry = true;
    }

    private static List<LifecycleListener> getDefaultLifecycleListeners() {
        AprLifecycleListener aprLifecycleListener = new AprLifecycleListener();
        return AprLifecycleListener.isAprAvailable() ? new ArrayList(Arrays.asList(aprLifecycleListener)) : new ArrayList();
    }

    @Override // org.springframework.boot.web.servlet.server.ServletWebServerFactory
    public WebServer getWebServer(ServletContextInitializer... initializers) {
        if (this.disableMBeanRegistry) {
            Registry.disableRegistry();
        }
        Tomcat tomcat = new Tomcat();
        File baseDir = this.baseDirectory != null ? this.baseDirectory : createTempDir("tomcat");
        tomcat.setBaseDir(baseDir.getAbsolutePath());
        Connector connector = new Connector(this.protocol);
        connector.setThrowOnFailure(true);
        tomcat.getService().addConnector(connector);
        customizeConnector(connector);
        tomcat.setConnector(connector);
        tomcat.getHost().setAutoDeploy(false);
        configureEngine(tomcat.getEngine());
        for (Connector additionalConnector : this.additionalTomcatConnectors) {
            tomcat.getService().addConnector(additionalConnector);
        }
        prepareContext(tomcat.getHost(), initializers);
        return getTomcatWebServer(tomcat);
    }

    private void configureEngine(Engine engine) {
        engine.setBackgroundProcessorDelay(this.backgroundProcessorDelay);
        for (Valve valve : this.engineValves) {
            engine.getPipeline().addValve(valve);
        }
    }

    protected void prepareContext(Host host, ServletContextInitializer[] initializers) {
        File documentRoot = getValidDocumentRoot();
        TomcatEmbeddedContext context = new TomcatEmbeddedContext();
        if (documentRoot != null) {
            context.setResources(new LoaderHidingResourceRoot(context));
        }
        context.setName(getContextPath());
        context.setDisplayName(getDisplayName());
        context.setPath(getContextPath());
        File docBase = documentRoot != null ? documentRoot : createTempDir("tomcat-docbase");
        context.setDocBase(docBase.getAbsolutePath());
        context.addLifecycleListener(new Tomcat.FixContextListener());
        context.setParentClassLoader(this.resourceLoader != null ? this.resourceLoader.getClassLoader() : ClassUtils.getDefaultClassLoader());
        resetDefaultLocaleMapping(context);
        addLocaleMappings(context);
        try {
            context.setCreateUploadTargets(true);
        } catch (NoSuchMethodError e) {
        }
        configureTldSkipPatterns(context);
        WebappLoader loader = new WebappLoader();
        loader.setLoaderClass(TomcatEmbeddedWebappClassLoader.class.getName());
        loader.setDelegate(true);
        context.setLoader(loader);
        if (isRegisterDefaultServlet()) {
            addDefaultServlet(context);
        }
        if (shouldRegisterJspServlet()) {
            addJspServlet(context);
            addJasperInitializer(context);
        }
        context.addLifecycleListener(new StaticResourceConfigurer(context));
        ServletContextInitializer[] initializersToUse = mergeInitializers(initializers);
        host.addChild(context);
        configureContext(context, initializersToUse);
        postProcessContext(context);
    }

    private void resetDefaultLocaleMapping(TomcatEmbeddedContext context) {
        context.addLocaleEncodingMappingParameter(Locale.ENGLISH.toString(), DEFAULT_CHARSET.displayName());
        context.addLocaleEncodingMappingParameter(Locale.FRENCH.toString(), DEFAULT_CHARSET.displayName());
    }

    private void addLocaleMappings(TomcatEmbeddedContext context) {
        getLocaleCharsetMappings().forEach(locale, charset -> {
            context.addLocaleEncodingMappingParameter(locale.toString(), charset.toString());
        });
    }

    private void configureTldSkipPatterns(TomcatEmbeddedContext context) {
        StandardJarScanFilter filter = new StandardJarScanFilter();
        filter.setTldSkip(StringUtils.collectionToCommaDelimitedString(this.tldSkipPatterns));
        context.getJarScanner().setJarScanFilter(filter);
    }

    private void addDefaultServlet(Context context) {
        Wrapper defaultServlet = context.createWrapper();
        defaultServlet.setName("default");
        defaultServlet.setServletClass("org.apache.catalina.servlets.DefaultServlet");
        defaultServlet.addInitParameter("debug", CustomBooleanEditor.VALUE_0);
        defaultServlet.addInitParameter("listings", "false");
        defaultServlet.setLoadOnStartup(1);
        defaultServlet.setOverridable(true);
        context.addChild(defaultServlet);
        context.addServletMappingDecoded("/", "default");
    }

    private void addJspServlet(Context context) {
        Wrapper jspServlet = context.createWrapper();
        jspServlet.setName("jsp");
        jspServlet.setServletClass(getJsp().getClassName());
        jspServlet.addInitParameter("fork", "false");
        Map<String, String> initParameters = getJsp().getInitParameters();
        jspServlet.getClass();
        initParameters.forEach(this::addInitParameter);
        jspServlet.setLoadOnStartup(3);
        context.addChild(jspServlet);
        context.addServletMappingDecoded("*.jsp", "jsp");
        context.addServletMappingDecoded("*.jspx", "jsp");
    }

    private void addJasperInitializer(TomcatEmbeddedContext context) {
        try {
            ServletContainerInitializer initializer = (ServletContainerInitializer) ClassUtils.forName("org.apache.jasper.servlet.JasperInitializer", null).newInstance();
            context.addServletContainerInitializer(initializer, null);
        } catch (Exception e) {
        }
    }

    protected void customizeConnector(Connector connector) {
        int port = Math.max(getPort(), 0);
        connector.setPort(port);
        if (StringUtils.hasText(getServerHeader())) {
            connector.setProperty("server", getServerHeader());
        }
        if (connector.getProtocolHandler() instanceof AbstractProtocol) {
            customizeProtocol((AbstractProtocol) connector.getProtocolHandler());
        }
        invokeProtocolHandlerCustomizers(connector.getProtocolHandler());
        if (getUriEncoding() != null) {
            connector.setURIEncoding(getUriEncoding().name());
        }
        connector.setProperty("bindOnInit", "false");
        if (getSsl() != null && getSsl().isEnabled()) {
            customizeSsl(connector);
        }
        TomcatConnectorCustomizer compression = new CompressionConnectorCustomizer(getCompression());
        compression.customize(connector);
        for (TomcatConnectorCustomizer customizer : this.tomcatConnectorCustomizers) {
            customizer.customize(connector);
        }
    }

    private void customizeProtocol(AbstractProtocol<?> protocol) {
        if (getAddress() != null) {
            protocol.setAddress(getAddress());
        }
    }

    private void invokeProtocolHandlerCustomizers(ProtocolHandler protocolHandler) {
        LambdaSafe.callbacks(TomcatProtocolHandlerCustomizer.class, this.tomcatProtocolHandlerCustomizers, protocolHandler, new Object[0]).invoke(customizer -> {
            customizer.customize(protocolHandler);
        });
    }

    private void customizeSsl(Connector connector) {
        new SslConnectorCustomizer(getSsl(), getSslStoreProvider()).customize(connector);
        if (getHttp2() != null && getHttp2().isEnabled()) {
            connector.addUpgradeProtocol(new Http2Protocol());
        }
    }

    protected void configureContext(Context context, ServletContextInitializer[] initializers) {
        TomcatStarter starter = new TomcatStarter(initializers);
        if (context instanceof TomcatEmbeddedContext) {
            TomcatEmbeddedContext embeddedContext = (TomcatEmbeddedContext) context;
            embeddedContext.setStarter(starter);
            embeddedContext.setFailCtxIfServletStartFails(true);
        }
        context.addServletContainerInitializer(starter, NO_CLASSES);
        for (LifecycleListener lifecycleListener : this.contextLifecycleListeners) {
            context.addLifecycleListener(lifecycleListener);
        }
        for (Valve valve : this.contextValves) {
            context.getPipeline().addValve(valve);
        }
        for (ErrorPage errorPage : getErrorPages()) {
            org.apache.tomcat.util.descriptor.web.ErrorPage tomcatErrorPage = new org.apache.tomcat.util.descriptor.web.ErrorPage();
            tomcatErrorPage.setLocation(errorPage.getPath());
            tomcatErrorPage.setErrorCode(errorPage.getStatusCode());
            tomcatErrorPage.setExceptionType(errorPage.getExceptionName());
            context.addErrorPage(tomcatErrorPage);
        }
        Iterator<MimeMappings.Mapping> it = getMimeMappings().iterator();
        while (it.hasNext()) {
            MimeMappings.Mapping mapping = it.next();
            context.addMimeMapping(mapping.getExtension(), mapping.getMimeType());
        }
        configureSession(context);
        new DisableReferenceClearingContextCustomizer().customize(context);
        for (TomcatContextCustomizer customizer : this.tomcatContextCustomizers) {
            customizer.customize(context);
        }
    }

    private void configureSession(Context context) {
        long sessionTimeout = getSessionTimeoutInMinutes();
        context.setSessionTimeout((int) sessionTimeout);
        Boolean httpOnly = getSession().getCookie().getHttpOnly();
        if (httpOnly != null) {
            context.setUseHttpOnly(httpOnly.booleanValue());
        }
        if (getSession().isPersistent()) {
            Manager manager = context.getManager();
            if (manager == null) {
                manager = new StandardManager();
                context.setManager(manager);
            }
            configurePersistSession(manager);
            return;
        }
        context.addLifecycleListener(new DisablePersistSessionListener());
    }

    private void configurePersistSession(Manager manager) {
        Assert.state(manager instanceof StandardManager, () -> {
            return "Unable to persist HTTP session state using manager type " + manager.getClass().getName();
        });
        File dir = getValidSessionStoreDir();
        File file = new File(dir, "SESSIONS.ser");
        ((StandardManager) manager).setPathname(file.getAbsolutePath());
    }

    private long getSessionTimeoutInMinutes() {
        Duration sessionTimeout = getSession().getTimeout();
        if (isZeroOrLess(sessionTimeout)) {
            return 0L;
        }
        return Math.max(sessionTimeout.toMinutes(), 1L);
    }

    private boolean isZeroOrLess(Duration sessionTimeout) {
        return sessionTimeout == null || sessionTimeout.isNegative() || sessionTimeout.isZero();
    }

    protected void postProcessContext(Context context) {
    }

    protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
        return new TomcatWebServer(tomcat, getPort() >= 0, getShutdown());
    }

    @Override // org.springframework.context.ResourceLoaderAware
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override // org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory
    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public Set<String> getTldSkipPatterns() {
        return this.tldSkipPatterns;
    }

    public void setTldSkipPatterns(Collection<String> patterns) {
        Assert.notNull(patterns, "Patterns must not be null");
        this.tldSkipPatterns = new LinkedHashSet(patterns);
    }

    public void addTldSkipPatterns(String... patterns) {
        Assert.notNull(patterns, "Patterns must not be null");
        this.tldSkipPatterns.addAll(Arrays.asList(patterns));
    }

    public void setProtocol(String protocol) {
        Assert.hasLength(protocol, "Protocol must not be empty");
        this.protocol = protocol;
    }

    public void setEngineValves(Collection<? extends Valve> engineValves) {
        Assert.notNull(engineValves, "Valves must not be null");
        this.engineValves = new ArrayList(engineValves);
    }

    public Collection<Valve> getEngineValves() {
        return this.engineValves;
    }

    @Override // org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory
    public void addEngineValves(Valve... engineValves) {
        Assert.notNull(engineValves, "Valves must not be null");
        this.engineValves.addAll(Arrays.asList(engineValves));
    }

    public void setContextValves(Collection<? extends Valve> contextValves) {
        Assert.notNull(contextValves, "Valves must not be null");
        this.contextValves = new ArrayList(contextValves);
    }

    public Collection<Valve> getContextValves() {
        return this.contextValves;
    }

    public void addContextValves(Valve... contextValves) {
        Assert.notNull(contextValves, "Valves must not be null");
        this.contextValves.addAll(Arrays.asList(contextValves));
    }

    public void setContextLifecycleListeners(Collection<? extends LifecycleListener> contextLifecycleListeners) {
        Assert.notNull(contextLifecycleListeners, "ContextLifecycleListeners must not be null");
        this.contextLifecycleListeners = new ArrayList(contextLifecycleListeners);
    }

    public Collection<LifecycleListener> getContextLifecycleListeners() {
        return this.contextLifecycleListeners;
    }

    public void addContextLifecycleListeners(LifecycleListener... contextLifecycleListeners) {
        Assert.notNull(contextLifecycleListeners, "ContextLifecycleListeners must not be null");
        this.contextLifecycleListeners.addAll(Arrays.asList(contextLifecycleListeners));
    }

    public void setTomcatContextCustomizers(Collection<? extends TomcatContextCustomizer> tomcatContextCustomizers) {
        Assert.notNull(tomcatContextCustomizers, "TomcatContextCustomizers must not be null");
        this.tomcatContextCustomizers = new LinkedHashSet(tomcatContextCustomizers);
    }

    public Collection<TomcatContextCustomizer> getTomcatContextCustomizers() {
        return this.tomcatContextCustomizers;
    }

    @Override // org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory
    public void addContextCustomizers(TomcatContextCustomizer... tomcatContextCustomizers) {
        Assert.notNull(tomcatContextCustomizers, "TomcatContextCustomizers must not be null");
        this.tomcatContextCustomizers.addAll(Arrays.asList(tomcatContextCustomizers));
    }

    public void setTomcatConnectorCustomizers(Collection<? extends TomcatConnectorCustomizer> tomcatConnectorCustomizers) {
        Assert.notNull(tomcatConnectorCustomizers, "TomcatConnectorCustomizers must not be null");
        this.tomcatConnectorCustomizers = new LinkedHashSet(tomcatConnectorCustomizers);
    }

    @Override // org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory
    public void addConnectorCustomizers(TomcatConnectorCustomizer... tomcatConnectorCustomizers) {
        Assert.notNull(tomcatConnectorCustomizers, "TomcatConnectorCustomizers must not be null");
        this.tomcatConnectorCustomizers.addAll(Arrays.asList(tomcatConnectorCustomizers));
    }

    public Collection<TomcatConnectorCustomizer> getTomcatConnectorCustomizers() {
        return this.tomcatConnectorCustomizers;
    }

    public void setTomcatProtocolHandlerCustomizers(Collection<? extends TomcatProtocolHandlerCustomizer<?>> tomcatProtocolHandlerCustomizer) {
        Assert.notNull(tomcatProtocolHandlerCustomizer, "TomcatProtocolHandlerCustomizers must not be null");
        this.tomcatProtocolHandlerCustomizers = new LinkedHashSet(tomcatProtocolHandlerCustomizer);
    }

    @Override // org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory
    public void addProtocolHandlerCustomizers(TomcatProtocolHandlerCustomizer<?>... tomcatProtocolHandlerCustomizers) {
        Assert.notNull(tomcatProtocolHandlerCustomizers, "TomcatProtocolHandlerCustomizers must not be null");
        this.tomcatProtocolHandlerCustomizers.addAll(Arrays.asList(tomcatProtocolHandlerCustomizers));
    }

    public Collection<TomcatProtocolHandlerCustomizer<?>> getTomcatProtocolHandlerCustomizers() {
        return this.tomcatProtocolHandlerCustomizers;
    }

    public void addAdditionalTomcatConnectors(Connector... connectors) {
        Assert.notNull(connectors, "Connectors must not be null");
        this.additionalTomcatConnectors.addAll(Arrays.asList(connectors));
    }

    public List<Connector> getAdditionalTomcatConnectors() {
        return this.additionalTomcatConnectors;
    }

    @Override // org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory
    public void setUriEncoding(Charset uriEncoding) {
        this.uriEncoding = uriEncoding;
    }

    public Charset getUriEncoding() {
        return this.uriEncoding;
    }

    @Override // org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory
    public void setBackgroundProcessorDelay(int delay) {
        this.backgroundProcessorDelay = delay;
    }

    public void setDisableMBeanRegistry(boolean disableMBeanRegistry) {
        this.disableMBeanRegistry = disableMBeanRegistry;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/tomcat/TomcatServletWebServerFactory$DisablePersistSessionListener.class */
    public static class DisablePersistSessionListener implements LifecycleListener {
        private DisablePersistSessionListener() {
        }

        @Override // org.apache.catalina.LifecycleListener
        public void lifecycleEvent(LifecycleEvent event) {
            if (event.getType().equals(Lifecycle.START_EVENT)) {
                Context context = (Context) event.getLifecycle();
                Manager manager = context.getManager();
                if (manager instanceof StandardManager) {
                    ((StandardManager) manager).setPathname(null);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/tomcat/TomcatServletWebServerFactory$StaticResourceConfigurer.class */
    public final class StaticResourceConfigurer implements LifecycleListener {
        private final Context context;

        private StaticResourceConfigurer(Context context) {
            this.context = context;
        }

        @Override // org.apache.catalina.LifecycleListener
        public void lifecycleEvent(LifecycleEvent event) {
            if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
                addResourceJars(TomcatServletWebServerFactory.this.getUrlsOfJarsWithMetaInfResources());
            }
        }

        private void addResourceJars(List<URL> resourceJarUrls) {
            for (URL url : resourceJarUrls) {
                String path = url.getPath();
                if (path.endsWith(".jar") || path.endsWith(".jar!/")) {
                    String jar = url.toString();
                    if (!jar.startsWith(ResourceUtils.JAR_URL_PREFIX)) {
                        jar = ResourceUtils.JAR_URL_PREFIX + jar + ResourceUtils.JAR_URL_SEPARATOR;
                    }
                    addResourceSet(jar);
                } else {
                    addResourceSet(url.toString());
                }
            }
        }

        private void addResourceSet(String resource) {
            try {
                if (isInsideNestedJar(resource)) {
                    resource = resource.substring(0, resource.length() - 2);
                }
                URL url = new URL(resource);
                this.context.getResources().createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/", url, "/META-INF/resources");
            } catch (Exception e) {
            }
        }

        private boolean isInsideNestedJar(String dir) {
            return dir.indexOf(ResourceUtils.JAR_URL_SEPARATOR) < dir.lastIndexOf(ResourceUtils.JAR_URL_SEPARATOR);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/tomcat/TomcatServletWebServerFactory$LoaderHidingResourceRoot.class */
    public static final class LoaderHidingResourceRoot extends StandardRoot {
        private LoaderHidingResourceRoot(TomcatEmbeddedContext context) {
            super(context);
        }

        @Override // org.apache.catalina.webresources.StandardRoot
        protected WebResourceSet createMainResourceSet() {
            return new LoaderHidingWebResourceSet(super.createMainResourceSet());
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/tomcat/TomcatServletWebServerFactory$LoaderHidingWebResourceSet.class */
    private static final class LoaderHidingWebResourceSet extends AbstractResourceSet {
        private final WebResourceSet delegate;
        private final Method initInternal;

        private LoaderHidingWebResourceSet(WebResourceSet delegate) {
            this.delegate = delegate;
            try {
                this.initInternal = LifecycleBase.class.getDeclaredMethod("initInternal", new Class[0]);
                this.initInternal.setAccessible(true);
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override // org.apache.catalina.WebResourceSet
        public WebResource getResource(String path) {
            if (path.startsWith("/org/springframework/boot")) {
                return new EmptyResource(getRoot(), path);
            }
            return this.delegate.getResource(path);
        }

        @Override // org.apache.catalina.WebResourceSet
        public String[] list(String path) {
            return this.delegate.list(path);
        }

        @Override // org.apache.catalina.WebResourceSet
        public Set<String> listWebAppPaths(String path) {
            return (Set) this.delegate.listWebAppPaths(path).stream().filter(webAppPath -> {
                return !webAppPath.startsWith("/org/springframework/boot");
            }).collect(Collectors.toSet());
        }

        @Override // org.apache.catalina.WebResourceSet
        public boolean mkdir(String path) {
            return this.delegate.mkdir(path);
        }

        @Override // org.apache.catalina.WebResourceSet
        public boolean write(String path, InputStream is, boolean overwrite) {
            return this.delegate.write(path, is, overwrite);
        }

        @Override // org.apache.catalina.WebResourceSet
        public URL getBaseUrl() {
            return this.delegate.getBaseUrl();
        }

        @Override // org.apache.catalina.WebResourceSet
        public void setReadOnly(boolean readOnly) {
            this.delegate.setReadOnly(readOnly);
        }

        @Override // org.apache.catalina.WebResourceSet
        public boolean isReadOnly() {
            return this.delegate.isReadOnly();
        }

        @Override // org.apache.catalina.WebResourceSet
        public void gc() {
            this.delegate.gc();
        }

        @Override // org.apache.catalina.util.LifecycleBase
        protected void initInternal() throws LifecycleException {
            if (this.delegate instanceof LifecycleBase) {
                try {
                    ReflectionUtils.invokeMethod(this.initInternal, this.delegate);
                } catch (Exception ex) {
                    throw new LifecycleException(ex);
                }
            }
        }
    }
}
