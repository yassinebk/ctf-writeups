package org.springframework.boot.web.servlet.context;

import org.springframework.boot.web.server.WebServer;
import org.springframework.context.SmartLifecycle;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/servlet/context/WebServerGracefulShutdownLifecycle.class */
class WebServerGracefulShutdownLifecycle implements SmartLifecycle {
    private final WebServer webServer;
    private volatile boolean running;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebServerGracefulShutdownLifecycle(WebServer webServer) {
        this.webServer = webServer;
    }

    @Override // org.springframework.context.Lifecycle
    public void start() {
        this.running = true;
    }

    @Override // org.springframework.context.Lifecycle
    public void stop() {
        throw new UnsupportedOperationException("Stop must not be invoked directly");
    }

    @Override // org.springframework.context.SmartLifecycle
    public void stop(Runnable callback) {
        this.running = false;
        this.webServer.shutDownGracefully(result -> {
            callback.run();
        });
    }

    @Override // org.springframework.context.Lifecycle
    public boolean isRunning() {
        return this.running;
    }
}
