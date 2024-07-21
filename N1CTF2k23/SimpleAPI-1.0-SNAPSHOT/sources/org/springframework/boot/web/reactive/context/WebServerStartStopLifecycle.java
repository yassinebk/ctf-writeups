package org.springframework.boot.web.reactive.context;

import org.springframework.context.SmartLifecycle;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/reactive/context/WebServerStartStopLifecycle.class */
public class WebServerStartStopLifecycle implements SmartLifecycle {
    private final WebServerManager weServerManager;
    private volatile boolean running;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebServerStartStopLifecycle(WebServerManager weServerManager) {
        this.weServerManager = weServerManager;
    }

    @Override // org.springframework.context.Lifecycle
    public void start() {
        this.weServerManager.start();
        this.running = true;
    }

    @Override // org.springframework.context.Lifecycle
    public void stop() {
        this.running = false;
        this.weServerManager.stop();
    }

    @Override // org.springframework.context.Lifecycle
    public boolean isRunning() {
        return this.running;
    }

    @Override // org.springframework.context.SmartLifecycle, org.springframework.context.Phased
    public int getPhase() {
        return 2147483646;
    }
}
