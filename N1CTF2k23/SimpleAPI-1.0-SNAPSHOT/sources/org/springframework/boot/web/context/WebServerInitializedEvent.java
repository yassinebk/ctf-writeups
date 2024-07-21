package org.springframework.boot.web.context;

import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationEvent;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/context/WebServerInitializedEvent.class */
public abstract class WebServerInitializedEvent extends ApplicationEvent {
    public abstract WebServerApplicationContext getApplicationContext();

    /* JADX INFO: Access modifiers changed from: protected */
    public WebServerInitializedEvent(WebServer webServer) {
        super(webServer);
    }

    public WebServer getWebServer() {
        return getSource();
    }

    @Override // java.util.EventObject
    public WebServer getSource() {
        return (WebServer) super.getSource();
    }
}
