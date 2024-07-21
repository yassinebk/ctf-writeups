package org.springframework.boot.rsocket.netty;

import io.rsocket.transport.netty.server.CloseableChannel;
import java.net.InetSocketAddress;
import java.time.Duration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.rsocket.server.RSocketServer;
import org.springframework.boot.rsocket.server.RSocketServerException;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/rsocket/netty/NettyRSocketServer.class */
public class NettyRSocketServer implements RSocketServer {
    private static final Log logger = LogFactory.getLog(NettyRSocketServer.class);
    private final Mono<CloseableChannel> starter;
    private final Duration lifecycleTimeout;
    private CloseableChannel channel;

    public NettyRSocketServer(Mono<CloseableChannel> starter, Duration lifecycleTimeout) {
        Assert.notNull(starter, "starter must not be null");
        this.starter = starter;
        this.lifecycleTimeout = lifecycleTimeout;
    }

    @Override // org.springframework.boot.rsocket.server.RSocketServer
    public InetSocketAddress address() {
        if (this.channel != null) {
            return this.channel.address();
        }
        return null;
    }

    @Override // org.springframework.boot.rsocket.server.RSocketServer
    public void start() throws RSocketServerException {
        this.channel = (CloseableChannel) block(this.starter, this.lifecycleTimeout);
        logger.info("Netty RSocket started on port(s): " + address().getPort());
        startDaemonAwaitThread(this.channel);
    }

    private void startDaemonAwaitThread(CloseableChannel channel) {
        Thread awaitThread = new Thread(() -> {
            Void r0 = (Void) channel.onClose().block();
        }, "rsocket");
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override // org.springframework.boot.rsocket.server.RSocketServer
    public void stop() throws RSocketServerException {
        if (this.channel != null) {
            this.channel.dispose();
            this.channel = null;
        }
    }

    private <T> T block(Mono<T> mono, Duration timeout) {
        return timeout != null ? (T) mono.block(timeout) : (T) mono.block();
    }
}
