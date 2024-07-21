package org.springframework.boot.web.embedded.netty;

import java.time.Duration;
import java.util.function.Supplier;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.web.server.GracefulShutdownCallback;
import org.springframework.boot.web.server.GracefulShutdownResult;
import reactor.netty.DisposableServer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/netty/GracefulShutdown.class */
final class GracefulShutdown {
    private static final Log logger = LogFactory.getLog(GracefulShutdown.class);
    private final Supplier<DisposableServer> disposableServer;
    private volatile Thread shutdownThread;
    private volatile boolean shuttingDown;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GracefulShutdown(Supplier<DisposableServer> disposableServer) {
        this.disposableServer = disposableServer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void shutDownGracefully(GracefulShutdownCallback callback) {
        DisposableServer server = this.disposableServer.get();
        if (server == null) {
            return;
        }
        logger.info("Commencing graceful shutdown. Waiting for active requests to complete");
        this.shutdownThread = new Thread(() -> {
            doShutdown(callback, server);
        }, "netty-shutdown");
        this.shutdownThread.start();
    }

    private void doShutdown(GracefulShutdownCallback callback, DisposableServer server) {
        this.shuttingDown = true;
        try {
            try {
                server.disposeNow(Duration.ofMillis(Long.MAX_VALUE));
                logger.info("Graceful shutdown complete");
                callback.shutdownComplete(GracefulShutdownResult.IDLE);
                this.shutdownThread = null;
                this.shuttingDown = false;
            } catch (Exception e) {
                logger.info("Graceful shutdown aborted with one or more active requests");
                callback.shutdownComplete(GracefulShutdownResult.REQUESTS_ACTIVE);
                this.shutdownThread = null;
                this.shuttingDown = false;
            }
        } catch (Throwable th) {
            this.shutdownThread = null;
            this.shuttingDown = false;
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void abort() {
        Thread shutdownThread = this.shutdownThread;
        if (shutdownThread != null) {
            while (!this.shuttingDown) {
                sleep(50L);
            }
            this.shutdownThread.interrupt();
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
