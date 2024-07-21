package org.springframework.boot.web.servlet.context;

import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.SmartLifecycle;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/servlet/context/WebServerStartStopLifecycle.class */
class WebServerStartStopLifecycle implements SmartLifecycle {
    private final ServletWebServerApplicationContext applicationContext;
    private final WebServer webServer;
    private volatile boolean running;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebServerStartStopLifecycle(ServletWebServerApplicationContext applicationContext, WebServer webServer) {
        this.applicationContext = applicationContext;
        this.webServer = webServer;
    }

    @Override // org.springframework.context.Lifecycle
    public void start() {
        this.webServer.start();
        this.running = true;
        this.applicationContext.publishEvent((ApplicationEvent) new ServletWebServerInitializedEvent(this.webServer, this.applicationContext));
    }

    @Override // org.springframework.context.Lifecycle
    public void stop() {
        this.webServer.stop();
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
