package org.springframework.boot.web.embedded.netty;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.boot.web.reactive.server.AbstractReactiveWebServerFactory;
import org.springframework.boot.web.server.Shutdown;
import org.springframework.boot.web.server.WebServer;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.util.Assert;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.server.HttpServer;
import reactor.netty.resources.LoopResources;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/netty/NettyReactiveWebServerFactory.class */
public class NettyReactiveWebServerFactory extends AbstractReactiveWebServerFactory {
    private Set<NettyServerCustomizer> serverCustomizers;
    private List<NettyRouteProvider> routeProviders;
    private Duration lifecycleTimeout;
    private boolean useForwardHeaders;
    private ReactorResourceFactory resourceFactory;
    private Shutdown shutdown;

    public NettyReactiveWebServerFactory() {
        this.serverCustomizers = new LinkedHashSet();
        this.routeProviders = new ArrayList();
    }

    public NettyReactiveWebServerFactory(int port) {
        super(port);
        this.serverCustomizers = new LinkedHashSet();
        this.routeProviders = new ArrayList();
    }

    @Override // org.springframework.boot.web.reactive.server.ReactiveWebServerFactory
    public WebServer getWebServer(HttpHandler httpHandler) {
        HttpServer httpServer = createHttpServer();
        ReactorHttpHandlerAdapter handlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);
        NettyWebServer webServer = new NettyWebServer(httpServer, handlerAdapter, this.lifecycleTimeout, getShutdown());
        webServer.setRouteProviders(this.routeProviders);
        return webServer;
    }

    public Collection<NettyServerCustomizer> getServerCustomizers() {
        return this.serverCustomizers;
    }

    public void setServerCustomizers(Collection<? extends NettyServerCustomizer> serverCustomizers) {
        Assert.notNull(serverCustomizers, "ServerCustomizers must not be null");
        this.serverCustomizers = new LinkedHashSet(serverCustomizers);
    }

    public void addServerCustomizers(NettyServerCustomizer... serverCustomizers) {
        Assert.notNull(serverCustomizers, "ServerCustomizer must not be null");
        this.serverCustomizers.addAll(Arrays.asList(serverCustomizers));
    }

    public void addRouteProviders(NettyRouteProvider... routeProviders) {
        Assert.notNull(routeProviders, "NettyRouteProvider must not be null");
        this.routeProviders.addAll(Arrays.asList(routeProviders));
    }

    public void setLifecycleTimeout(Duration lifecycleTimeout) {
        this.lifecycleTimeout = lifecycleTimeout;
    }

    public void setUseForwardHeaders(boolean useForwardHeaders) {
        this.useForwardHeaders = useForwardHeaders;
    }

    public void setResourceFactory(ReactorResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    @Override // org.springframework.boot.web.server.AbstractConfigurableWebServerFactory, org.springframework.boot.web.server.ConfigurableWebServerFactory
    public void setShutdown(Shutdown shutdown) {
        this.shutdown = shutdown;
    }

    @Override // org.springframework.boot.web.server.AbstractConfigurableWebServerFactory
    public Shutdown getShutdown() {
        return this.shutdown;
    }

    private HttpServer createHttpServer() {
        HttpServer server;
        HttpServer server2 = HttpServer.create();
        if (this.resourceFactory != null) {
            LoopResources resources = this.resourceFactory.getLoopResources();
            Assert.notNull(resources, "No LoopResources: is ReactorResourceFactory not initialized yet?");
            server = server2.tcpConfiguration(tcpServer -> {
                return tcpServer.runOn(resources).bindAddress(this::getListenAddress);
            });
        } else {
            server = server2.tcpConfiguration(tcpServer2 -> {
                return tcpServer2.bindAddress(this::getListenAddress);
            });
        }
        if (getSsl() != null && getSsl().isEnabled()) {
            SslServerCustomizer sslServerCustomizer = new SslServerCustomizer(getSsl(), getHttp2(), getSslStoreProvider());
            server = sslServerCustomizer.apply(server);
        }
        if (getCompression() != null && getCompression().getEnabled()) {
            CompressionCustomizer compressionCustomizer = new CompressionCustomizer(getCompression());
            server = compressionCustomizer.apply(server);
        }
        return applyCustomizers(server.protocol(listProtocols()).forwarded(this.useForwardHeaders));
    }

    private HttpProtocol[] listProtocols() {
        return (getHttp2() == null || !getHttp2().isEnabled() || getSsl() == null || !getSsl().isEnabled()) ? new HttpProtocol[]{HttpProtocol.HTTP11} : new HttpProtocol[]{HttpProtocol.H2, HttpProtocol.HTTP11};
    }

    private InetSocketAddress getListenAddress() {
        if (getAddress() != null) {
            return new InetSocketAddress(getAddress().getHostAddress(), getPort());
        }
        return new InetSocketAddress(getPort());
    }

    private HttpServer applyCustomizers(HttpServer server) {
        for (NettyServerCustomizer customizer : this.serverCustomizers) {
            server = customizer.apply(server);
        }
        return server;
    }
}
