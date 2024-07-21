package org.springframework.boot.web.reactive.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.web.context.ConfigurableWebServerApplicationContext;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/reactive/context/ReactiveWebServerApplicationContext.class */
public class ReactiveWebServerApplicationContext extends GenericReactiveWebApplicationContext implements ConfigurableWebServerApplicationContext {
    private volatile WebServerManager serverManager;
    private String serverNamespace;

    public ReactiveWebServerApplicationContext() {
    }

    public ReactiveWebServerApplicationContext(DefaultListableBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override // org.springframework.context.support.AbstractApplicationContext, org.springframework.context.ConfigurableApplicationContext
    public final void refresh() throws BeansException, IllegalStateException {
        try {
            super.refresh();
        } catch (RuntimeException ex) {
            WebServerManager serverManager = this.serverManager;
            if (serverManager != null) {
                serverManager.getWebServer().stop();
            }
            throw ex;
        }
    }

    @Override // org.springframework.context.support.AbstractApplicationContext
    protected void onRefresh() {
        super.onRefresh();
        try {
            createWebServer();
        } catch (Throwable ex) {
            throw new ApplicationContextException("Unable to start reactive web server", ex);
        }
    }

    private void createWebServer() {
        WebServerManager serverManager = this.serverManager;
        if (serverManager == null) {
            String webServerFactoryBeanName = getWebServerFactoryBeanName();
            ReactiveWebServerFactory webServerFactory = getWebServerFactory(webServerFactoryBeanName);
            boolean lazyInit = getBeanFactory().getBeanDefinition(webServerFactoryBeanName).isLazyInit();
            this.serverManager = new WebServerManager(this, webServerFactory, this::getHttpHandler, lazyInit);
            getBeanFactory().registerSingleton("webServerGracefulShutdown", new WebServerGracefulShutdownLifecycle(this.serverManager));
            getBeanFactory().registerSingleton("webServerStartStop", new WebServerStartStopLifecycle(this.serverManager));
        }
        initPropertySources();
    }

    protected String getWebServerFactoryBeanName() {
        String[] beanNames = getBeanFactory().getBeanNamesForType(ReactiveWebServerFactory.class);
        if (beanNames.length == 0) {
            throw new ApplicationContextException("Unable to start ReactiveWebApplicationContext due to missing ReactiveWebServerFactory bean.");
        }
        if (beanNames.length > 1) {
            throw new ApplicationContextException("Unable to start ReactiveWebApplicationContext due to multiple ReactiveWebServerFactory beans : " + StringUtils.arrayToCommaDelimitedString(beanNames));
        }
        return beanNames[0];
    }

    protected ReactiveWebServerFactory getWebServerFactory(String factoryBeanName) {
        return (ReactiveWebServerFactory) getBeanFactory().getBean(factoryBeanName, ReactiveWebServerFactory.class);
    }

    protected HttpHandler getHttpHandler() {
        String[] beanNames = getBeanFactory().getBeanNamesForType(HttpHandler.class);
        if (beanNames.length == 0) {
            throw new ApplicationContextException("Unable to start ReactiveWebApplicationContext due to missing HttpHandler bean.");
        }
        if (beanNames.length > 1) {
            throw new ApplicationContextException("Unable to start ReactiveWebApplicationContext due to multiple HttpHandler beans : " + StringUtils.arrayToCommaDelimitedString(beanNames));
        }
        return (HttpHandler) getBeanFactory().getBean(beanNames[0], HttpHandler.class);
    }

    @Override // org.springframework.context.support.AbstractApplicationContext
    protected void doClose() {
        AvailabilityChangeEvent.publish(this, ReadinessState.REFUSING_TRAFFIC);
        super.doClose();
    }

    @Override // org.springframework.boot.web.context.WebServerApplicationContext
    public WebServer getWebServer() {
        WebServerManager serverManager = this.serverManager;
        if (serverManager != null) {
            return serverManager.getWebServer();
        }
        return null;
    }

    @Override // org.springframework.boot.web.context.WebServerApplicationContext
    public String getServerNamespace() {
        return this.serverNamespace;
    }

    @Override // org.springframework.boot.web.context.ConfigurableWebServerApplicationContext
    public void setServerNamespace(String serverNamespace) {
        this.serverNamespace = serverNamespace;
    }
}
