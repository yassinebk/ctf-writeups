package org.springframework.boot.rsocket.netty;

import io.rsocket.RSocketFactory;
import io.rsocket.SocketAcceptor;
import io.rsocket.transport.ServerTransport;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.transport.netty.server.WebsocketServerTransport;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.boot.rsocket.server.ConfigurableRSocketServerFactory;
import org.springframework.boot.rsocket.server.RSocketServer;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.boot.rsocket.server.RSocketServerFactory;
import org.springframework.boot.rsocket.server.ServerRSocketFactoryProcessor;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;
import reactor.netty.tcp.TcpServer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/rsocket/netty/NettyRSocketServerFactory.class */
public class NettyRSocketServerFactory implements RSocketServerFactory, ConfigurableRSocketServerFactory {
    private InetAddress address;
    private ReactorResourceFactory resourceFactory;
    private Duration lifecycleTimeout;
    private int port = 9898;
    private RSocketServer.Transport transport = RSocketServer.Transport.TCP;
    private List<ServerRSocketFactoryProcessor> socketFactoryProcessors = new ArrayList();
    private List<RSocketServerCustomizer> rSocketServerCustomizers = new ArrayList();

    @Override // org.springframework.boot.rsocket.server.ConfigurableRSocketServerFactory
    public void setPort(int port) {
        this.port = port;
    }

    @Override // org.springframework.boot.rsocket.server.ConfigurableRSocketServerFactory
    public void setAddress(InetAddress address) {
        this.address = address;
    }

    @Override // org.springframework.boot.rsocket.server.ConfigurableRSocketServerFactory
    public void setTransport(RSocketServer.Transport transport) {
        this.transport = transport;
    }

    public void setResourceFactory(ReactorResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    @Deprecated
    public void setSocketFactoryProcessors(Collection<? extends ServerRSocketFactoryProcessor> socketFactoryProcessors) {
        Assert.notNull(socketFactoryProcessors, "SocketFactoryProcessors must not be null");
        this.socketFactoryProcessors = new ArrayList(socketFactoryProcessors);
    }

    @Deprecated
    public void addSocketFactoryProcessors(ServerRSocketFactoryProcessor... socketFactoryProcessors) {
        Assert.notNull(socketFactoryProcessors, "SocketFactoryProcessors must not be null");
        this.socketFactoryProcessors.addAll(Arrays.asList(socketFactoryProcessors));
    }

    public void setRSocketServerCustomizers(Collection<? extends RSocketServerCustomizer> rSocketServerCustomizers) {
        Assert.notNull(rSocketServerCustomizers, "RSocketServerCustomizers must not be null");
        this.rSocketServerCustomizers = new ArrayList(rSocketServerCustomizers);
    }

    public void addRSocketServerCustomizers(RSocketServerCustomizer... rSocketServerCustomizers) {
        Assert.notNull(rSocketServerCustomizers, "RSocketServerCustomizers must not be null");
        this.rSocketServerCustomizers.addAll(Arrays.asList(rSocketServerCustomizers));
    }

    public void setLifecycleTimeout(Duration lifecycleTimeout) {
        this.lifecycleTimeout = lifecycleTimeout;
    }

    @Override // org.springframework.boot.rsocket.server.RSocketServerFactory
    public NettyRSocketServer create(SocketAcceptor socketAcceptor) {
        ServerTransport<CloseableChannel> transport = createTransport();
        io.rsocket.core.RSocketServer server = io.rsocket.core.RSocketServer.create(socketAcceptor);
        RSocketFactory.ServerRSocketFactory factory = new RSocketFactory.ServerRSocketFactory(server);
        this.rSocketServerCustomizers.forEach(customizer -> {
            customizer.customize(server);
        });
        this.socketFactoryProcessors.forEach(processor -> {
            processor.process(factory);
        });
        Mono<CloseableChannel> starter = server.bind(transport);
        return new NettyRSocketServer(starter, this.lifecycleTimeout);
    }

    private ServerTransport<CloseableChannel> createTransport() {
        if (this.transport == RSocketServer.Transport.WEBSOCKET) {
            return createWebSocketTransport();
        }
        return createTcpTransport();
    }

    private ServerTransport<CloseableChannel> createWebSocketTransport() {
        if (this.resourceFactory != null) {
            HttpServer httpServer = HttpServer.create().tcpConfiguration(tcpServer -> {
                return tcpServer.runOn(this.resourceFactory.getLoopResources()).bindAddress(this::getListenAddress);
            });
            return WebsocketServerTransport.create(httpServer);
        }
        return WebsocketServerTransport.create(getListenAddress());
    }

    private ServerTransport<CloseableChannel> createTcpTransport() {
        if (this.resourceFactory != null) {
            TcpServer tcpServer = TcpServer.create().runOn(this.resourceFactory.getLoopResources()).bindAddress(this::getListenAddress);
            return TcpServerTransport.create(tcpServer);
        }
        return TcpServerTransport.create(getListenAddress());
    }

    private InetSocketAddress getListenAddress() {
        if (this.address != null) {
            return new InetSocketAddress(this.address.getHostAddress(), this.port);
        }
        return new InetSocketAddress(this.port);
    }
}
