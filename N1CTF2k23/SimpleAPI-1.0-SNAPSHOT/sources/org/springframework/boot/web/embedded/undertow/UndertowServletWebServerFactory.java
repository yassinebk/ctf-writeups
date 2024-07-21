package org.springframework.boot.web.embedded.undertow;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.Resource;
import io.undertow.server.handlers.resource.ResourceChangeListener;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.server.handlers.resource.URLResource;
import io.undertow.server.session.SessionManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.MimeMapping;
import io.undertow.servlet.api.ServletContainerInitializerInfo;
import io.undertow.servlet.api.ServletStackTraces;
import io.undertow.servlet.core.DeploymentImpl;
import io.undertow.servlet.handlers.DefaultServlet;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/undertow/UndertowServletWebServerFactory.class */
public class UndertowServletWebServerFactory extends AbstractServletWebServerFactory implements ConfigurableUndertowWebServerFactory, ResourceLoaderAware {
    private static final Pattern ENCODED_SLASH = Pattern.compile("%2F", 16);
    private static final Set<Class<?>> NO_CLASSES = Collections.emptySet();
    private UndertowWebServerFactoryDelegate delegate;
    private Set<UndertowDeploymentInfoCustomizer> deploymentInfoCustomizers;
    private ResourceLoader resourceLoader;
    private boolean eagerInitFilters;

    public UndertowServletWebServerFactory() {
        this.delegate = new UndertowWebServerFactoryDelegate();
        this.deploymentInfoCustomizers = new LinkedHashSet();
        this.eagerInitFilters = true;
        getJsp().setRegistered(false);
    }

    public UndertowServletWebServerFactory(int port) {
        super(port);
        this.delegate = new UndertowWebServerFactoryDelegate();
        this.deploymentInfoCustomizers = new LinkedHashSet();
        this.eagerInitFilters = true;
        getJsp().setRegistered(false);
    }

    public UndertowServletWebServerFactory(String contextPath, int port) {
        super(contextPath, port);
        this.delegate = new UndertowWebServerFactoryDelegate();
        this.deploymentInfoCustomizers = new LinkedHashSet();
        this.eagerInitFilters = true;
        getJsp().setRegistered(false);
    }

    @Override // org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
    public void setBuilderCustomizers(Collection<? extends UndertowBuilderCustomizer> customizers) {
        this.delegate.setBuilderCustomizers(customizers);
    }

    @Override // org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
    public void addBuilderCustomizers(UndertowBuilderCustomizer... customizers) {
        this.delegate.addBuilderCustomizers(customizers);
    }

    public Collection<UndertowBuilderCustomizer> getBuilderCustomizers() {
        return this.delegate.getBuilderCustomizers();
    }

    @Override // org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
    public void setBufferSize(Integer bufferSize) {
        this.delegate.setBufferSize(bufferSize);
    }

    @Override // org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
    public void setIoThreads(Integer ioThreads) {
        this.delegate.setIoThreads(ioThreads);
    }

    @Override // org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
    public void setWorkerThreads(Integer workerThreads) {
        this.delegate.setWorkerThreads(workerThreads);
    }

    @Override // org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
    public void setUseDirectBuffers(Boolean directBuffers) {
        this.delegate.setUseDirectBuffers(directBuffers);
    }

    @Override // org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
    public void setAccessLogDirectory(File accessLogDirectory) {
        this.delegate.setAccessLogDirectory(accessLogDirectory);
    }

    @Override // org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
    public void setAccessLogPattern(String accessLogPattern) {
        this.delegate.setAccessLogPattern(accessLogPattern);
    }

    @Override // org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
    public void setAccessLogPrefix(String accessLogPrefix) {
        this.delegate.setAccessLogPrefix(accessLogPrefix);
    }

    public String getAccessLogPrefix() {
        return this.delegate.getAccessLogPrefix();
    }

    @Override // org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
    public void setAccessLogSuffix(String accessLogSuffix) {
        this.delegate.setAccessLogSuffix(accessLogSuffix);
    }

    @Override // org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
    public void setAccessLogEnabled(boolean accessLogEnabled) {
        this.delegate.setAccessLogEnabled(accessLogEnabled);
    }

    public boolean isAccessLogEnabled() {
        return this.delegate.isAccessLogEnabled();
    }

    @Override // org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
    public void setAccessLogRotate(boolean accessLogRotate) {
        this.delegate.setAccessLogRotate(accessLogRotate);
    }

    @Override // org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
    public void setUseForwardHeaders(boolean useForwardHeaders) {
        this.delegate.setUseForwardHeaders(useForwardHeaders);
    }

    protected final boolean isUseForwardHeaders() {
        return this.delegate.isUseForwardHeaders();
    }

    public void setDeploymentInfoCustomizers(Collection<? extends UndertowDeploymentInfoCustomizer> customizers) {
        Assert.notNull(customizers, "Customizers must not be null");
        this.deploymentInfoCustomizers = new LinkedHashSet(customizers);
    }

    public void addDeploymentInfoCustomizers(UndertowDeploymentInfoCustomizer... customizers) {
        Assert.notNull(customizers, "UndertowDeploymentInfoCustomizers must not be null");
        this.deploymentInfoCustomizers.addAll(Arrays.asList(customizers));
    }

    public Collection<UndertowDeploymentInfoCustomizer> getDeploymentInfoCustomizers() {
        return this.deploymentInfoCustomizers;
    }

    @Override // org.springframework.context.ResourceLoaderAware
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public boolean isEagerInitFilters() {
        return this.eagerInitFilters;
    }

    public void setEagerInitFilters(boolean eagerInitFilters) {
        this.eagerInitFilters = eagerInitFilters;
    }

    @Override // org.springframework.boot.web.servlet.server.ServletWebServerFactory
    public WebServer getWebServer(ServletContextInitializer... initializers) {
        Undertow.Builder builder = this.delegate.createBuilder(this);
        DeploymentManager manager = createManager(initializers);
        return getUndertowWebServer(builder, manager, getPort());
    }

    private DeploymentManager createManager(ServletContextInitializer... initializers) {
        DeploymentInfo deployment = Servlets.deployment();
        registerServletContainerInitializerToDriveServletContextInitializers(deployment, initializers);
        deployment.setClassLoader(getServletClassLoader());
        deployment.setContextPath(getContextPath());
        deployment.setDisplayName(getDisplayName());
        deployment.setDeploymentName("spring-boot");
        if (isRegisterDefaultServlet()) {
            deployment.addServlet(Servlets.servlet("default", DefaultServlet.class));
        }
        configureErrorPages(deployment);
        deployment.setServletStackTraces(ServletStackTraces.NONE);
        deployment.setResourceManager(getDocumentRootResourceManager());
        deployment.setTempDir(createTempDir("undertow"));
        deployment.setEagerFilterInit(this.eagerInitFilters);
        configureMimeMappings(deployment);
        for (UndertowDeploymentInfoCustomizer customizer : this.deploymentInfoCustomizers) {
            customizer.customize(deployment);
        }
        if (getSession().isPersistent()) {
            File dir = getValidSessionStoreDir();
            deployment.setSessionPersistenceManager(new FileSessionPersistence(dir));
        }
        addLocaleMappings(deployment);
        DeploymentManager manager = Servlets.newContainer().addDeployment(deployment);
        manager.deploy();
        if (manager.getDeployment() instanceof DeploymentImpl) {
            removeSuperfluousMimeMappings((DeploymentImpl) manager.getDeployment(), deployment);
        }
        SessionManager sessionManager = manager.getDeployment().getSessionManager();
        Duration timeoutDuration = getSession().getTimeout();
        int sessionTimeout = isZeroOrLess(timeoutDuration) ? -1 : (int) timeoutDuration.getSeconds();
        sessionManager.setDefaultSessionTimeout(sessionTimeout);
        return manager;
    }

    private boolean isZeroOrLess(Duration timeoutDuration) {
        return timeoutDuration == null || timeoutDuration.isZero() || timeoutDuration.isNegative();
    }

    private void addLocaleMappings(DeploymentInfo deployment) {
        getLocaleCharsetMappings().forEach(locale, charset -> {
            deployment.addLocaleCharsetMapping(locale.toString(), charset.toString());
        });
    }

    private void registerServletContainerInitializerToDriveServletContextInitializers(DeploymentInfo deployment, ServletContextInitializer... initializers) {
        ServletContextInitializer[] mergedInitializers = mergeInitializers(initializers);
        Initializer initializer = new Initializer(mergedInitializers);
        deployment.addServletContainerInitializer(new ServletContainerInitializerInfo(Initializer.class, new ImmediateInstanceFactory(initializer), NO_CLASSES));
    }

    private ClassLoader getServletClassLoader() {
        if (this.resourceLoader != null) {
            return this.resourceLoader.getClassLoader();
        }
        return getClass().getClassLoader();
    }

    private ResourceManager getDocumentRootResourceManager() {
        File root = getValidDocumentRoot();
        File docBase = getCanonicalDocumentRoot(root);
        List<URL> metaInfResourceUrls = getUrlsOfJarsWithMetaInfResources();
        List<URL> resourceJarUrls = new ArrayList<>();
        List<ResourceManager> managers = new ArrayList<>();
        FileResourceManager fileResourceManager = docBase.isDirectory() ? new FileResourceManager(docBase, 0L) : new JarResourceManager(docBase);
        if (root != null) {
            fileResourceManager = new LoaderHidingResourceManager(fileResourceManager);
        }
        managers.add(fileResourceManager);
        for (URL url : metaInfResourceUrls) {
            if ("file".equals(url.getProtocol())) {
                try {
                    File file = new File(url.toURI());
                    if (file.isFile()) {
                        resourceJarUrls.add(new URL(ResourceUtils.JAR_URL_PREFIX + url + ResourceUtils.JAR_URL_SEPARATOR));
                    } else {
                        managers.add(new FileResourceManager(new File(file, "META-INF/resources"), 0L));
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                resourceJarUrls.add(url);
            }
        }
        managers.add(new MetaInfResourcesResourceManager(resourceJarUrls));
        return new CompositeResourceManager((ResourceManager[]) managers.toArray(new ResourceManager[0]));
    }

    private File getCanonicalDocumentRoot(File docBase) {
        File createTempDir;
        if (docBase != null) {
            createTempDir = docBase;
        } else {
            try {
                createTempDir = createTempDir("undertow-docbase");
            } catch (IOException ex) {
                throw new IllegalStateException("Cannot get canonical document root", ex);
            }
        }
        File root = createTempDir;
        return root.getCanonicalFile();
    }

    private void configureErrorPages(DeploymentInfo servletBuilder) {
        for (ErrorPage errorPage : getErrorPages()) {
            servletBuilder.addErrorPage(getUndertowErrorPage(errorPage));
        }
    }

    private io.undertow.servlet.api.ErrorPage getUndertowErrorPage(ErrorPage errorPage) {
        if (errorPage.getStatus() != null) {
            return new io.undertow.servlet.api.ErrorPage(errorPage.getPath(), errorPage.getStatusCode());
        }
        if (errorPage.getException() != null) {
            return new io.undertow.servlet.api.ErrorPage(errorPage.getPath(), errorPage.getException());
        }
        return new io.undertow.servlet.api.ErrorPage(errorPage.getPath());
    }

    private void configureMimeMappings(DeploymentInfo servletBuilder) {
        Iterator<MimeMappings.Mapping> it = getMimeMappings().iterator();
        while (it.hasNext()) {
            MimeMappings.Mapping mimeMapping = it.next();
            servletBuilder.addMimeMapping(new MimeMapping(mimeMapping.getExtension(), mimeMapping.getMimeType()));
        }
    }

    private void removeSuperfluousMimeMappings(DeploymentImpl deployment, DeploymentInfo deploymentInfo) {
        Map<String, String> mappings = new HashMap<>();
        for (MimeMapping mapping : deploymentInfo.getMimeMappings()) {
            mappings.put(mapping.getExtension().toLowerCase(Locale.ENGLISH), mapping.getMimeType());
        }
        deployment.setMimeExtensionMappings(mappings);
    }

    protected UndertowServletWebServer getUndertowWebServer(Undertow.Builder builder, DeploymentManager manager, int port) {
        List<HttpHandlerFactory> httpHandlerFactories = this.delegate.createHttpHandlerFactories(this, new DeploymentManagerHttpHandlerFactory(manager));
        return new UndertowServletWebServer(builder, httpHandlerFactories, getContextPath(), port >= 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/undertow/UndertowServletWebServerFactory$Initializer.class */
    public static class Initializer implements ServletContainerInitializer {
        private final ServletContextInitializer[] initializers;

        Initializer(ServletContextInitializer[] initializers) {
            this.initializers = initializers;
        }

        @Override // javax.servlet.ServletContainerInitializer
        public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
            ServletContextInitializer[] servletContextInitializerArr;
            for (ServletContextInitializer initializer : this.initializers) {
                initializer.onStartup(servletContext);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/undertow/UndertowServletWebServerFactory$MetaInfResourcesResourceManager.class */
    public static final class MetaInfResourcesResourceManager implements ResourceManager {
        private final List<URL> metaInfResourceJarUrls;

        private MetaInfResourcesResourceManager(List<URL> metaInfResourceJarUrls) {
            this.metaInfResourceJarUrls = metaInfResourceJarUrls;
        }

        public void close() throws IOException {
        }

        public Resource getResource(String path) {
            for (URL url : this.metaInfResourceJarUrls) {
                URLResource resource = getMetaInfResource(url, path);
                if (resource != null) {
                    return resource;
                }
            }
            return null;
        }

        public boolean isResourceChangeListenerSupported() {
            return false;
        }

        public void registerResourceChangeListener(ResourceChangeListener listener) {
        }

        public void removeResourceChangeListener(ResourceChangeListener listener) {
        }

        private URLResource getMetaInfResource(URL resourceJar, String path) {
            try {
                String urlPath = URLEncoder.encode(UndertowServletWebServerFactory.ENCODED_SLASH.matcher(path).replaceAll("/"), "UTF-8");
                URL resourceUrl = new URL(resourceJar + "META-INF/resources" + urlPath);
                URLResource resource = new URLResource(resourceUrl, path);
                if (resource.getContentLength().longValue() < 0) {
                    return null;
                }
                return resource;
            } catch (Exception e) {
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/undertow/UndertowServletWebServerFactory$LoaderHidingResourceManager.class */
    public static final class LoaderHidingResourceManager implements ResourceManager {
        private final ResourceManager delegate;

        private LoaderHidingResourceManager(ResourceManager delegate) {
            this.delegate = delegate;
        }

        public Resource getResource(String path) throws IOException {
            if (path.startsWith("/org/springframework/boot")) {
                return null;
            }
            return this.delegate.getResource(path);
        }

        public boolean isResourceChangeListenerSupported() {
            return this.delegate.isResourceChangeListenerSupported();
        }

        public void registerResourceChangeListener(ResourceChangeListener listener) {
            this.delegate.registerResourceChangeListener(listener);
        }

        public void removeResourceChangeListener(ResourceChangeListener listener) {
            this.delegate.removeResourceChangeListener(listener);
        }

        public void close() throws IOException {
            this.delegate.close();
        }
    }
}
