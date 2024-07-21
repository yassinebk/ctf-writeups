package org.springframework.boot.web.embedded.undertow;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.servlet.api.DeploymentManager;
import org.springframework.boot.web.server.Compression;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/undertow/UndertowServletWebServer.class */
public class UndertowServletWebServer extends UndertowWebServer {
    private final String contextPath;
    private final DeploymentManager manager;

    @Deprecated
    public UndertowServletWebServer(Undertow.Builder builder, DeploymentManager manager, String contextPath, boolean autoStart, Compression compression) {
        this(builder, manager, contextPath, false, autoStart, compression);
    }

    @Deprecated
    public UndertowServletWebServer(Undertow.Builder builder, DeploymentManager manager, String contextPath, boolean useForwardHeaders, boolean autoStart, Compression compression) {
        this(builder, manager, contextPath, useForwardHeaders, autoStart, compression, null);
    }

    @Deprecated
    public UndertowServletWebServer(Undertow.Builder builder, DeploymentManager manager, String contextPath, boolean useForwardHeaders, boolean autoStart, Compression compression, String serverHeader) {
        this(builder, UndertowWebServerFactoryDelegate.createHttpHandlerFactories(compression, useForwardHeaders, serverHeader, null, new DeploymentManagerHttpHandlerFactory(manager)), contextPath, autoStart);
    }

    public UndertowServletWebServer(Undertow.Builder builder, Iterable<HttpHandlerFactory> httpHandlerFactories, String contextPath, boolean autoStart) {
        super(builder, httpHandlerFactories, autoStart);
        this.contextPath = contextPath;
        this.manager = findManager(httpHandlerFactories);
    }

    private DeploymentManager findManager(Iterable<HttpHandlerFactory> httpHandlerFactories) {
        for (HttpHandlerFactory httpHandlerFactory : httpHandlerFactories) {
            if (httpHandlerFactory instanceof DeploymentManagerHttpHandlerFactory) {
                return ((DeploymentManagerHttpHandlerFactory) httpHandlerFactory).getDeploymentManager();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.web.embedded.undertow.UndertowWebServer
    public HttpHandler createHttpHandler() {
        HttpHandler handler = super.createHttpHandler();
        if (!StringUtils.isEmpty(this.contextPath)) {
            handler = Handlers.path().addPrefixPath(this.contextPath, handler);
        }
        return handler;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.web.embedded.undertow.UndertowWebServer
    public String getStartLogMessage() {
        String message = super.getStartLogMessage();
        if (StringUtils.hasText(this.contextPath)) {
            message = message + " with context path '" + this.contextPath + "'";
        }
        return message;
    }

    public DeploymentManager getDeploymentManager() {
        return this.manager;
    }
}
