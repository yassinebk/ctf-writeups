package org.springframework.boot.web.embedded.jetty;

import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.springframework.boot.web.server.GracefulShutdownCallback;
import org.springframework.boot.web.server.GracefulShutdownResult;
import org.springframework.core.log.LogMessage;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/jetty/GracefulShutdown.class */
final class GracefulShutdown {
    private static final Log logger = LogFactory.getLog(JettyWebServer.class);
    private final Server server;
    private final Supplier<Integer> activeRequests;
    private volatile boolean shuttingDown = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GracefulShutdown(Server server, Supplier<Integer> activeRequests) {
        this.server = server;
        this.activeRequests = activeRequests;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void shutDownGracefully(GracefulShutdownCallback callback) {
        Connector[] connectors;
        logger.info("Commencing graceful shutdown. Waiting for active requests to complete");
        for (Connector connector : this.server.getConnectors()) {
            shutdown(connector);
        }
        this.shuttingDown = true;
        new Thread(() -> {
            awaitShutdown(callback);
        }, "jetty-shutdown").start();
    }

    private void shutdown(Connector connector) {
        try {
            connector.shutdown().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e2) {
        }
    }

    private void awaitShutdown(GracefulShutdownCallback callback) {
        while (this.shuttingDown && this.activeRequests.get().intValue() > 0) {
            sleep(100L);
        }
        this.shuttingDown = false;
        long activeRequests = this.activeRequests.get().intValue();
        if (activeRequests == 0) {
            logger.info("Graceful shutdown complete");
            callback.shutdownComplete(GracefulShutdownResult.IDLE);
            return;
        }
        logger.info(LogMessage.format("Graceful shutdown aborted with %d request(s) still active", Long.valueOf(activeRequests)));
        callback.shutdownComplete(GracefulShutdownResult.REQUESTS_ACTIVE);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void abort() {
        this.shuttingDown = false;
    }
}
