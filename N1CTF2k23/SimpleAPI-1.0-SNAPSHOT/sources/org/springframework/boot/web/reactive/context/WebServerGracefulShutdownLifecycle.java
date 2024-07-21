package org.springframework.boot.web.reactive.context;

import org.springframework.context.SmartLifecycle;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/reactive/context/WebServerGracefulShutdownLifecycle.class */
public class WebServerGracefulShutdownLifecycle implements SmartLifecycle {
    private final WebServerManager serverManager;
    private volatile boolean running;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebServerGracefulShutdownLifecycle(WebServerManager serverManager) {
        this.serverManager = serverManager;
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
        this.serverManager.shutDownGracefully(callback);
    }

    @Override // org.springframework.context.Lifecycle
    public boolean isRunning() {
        return this.running;
    }
}
