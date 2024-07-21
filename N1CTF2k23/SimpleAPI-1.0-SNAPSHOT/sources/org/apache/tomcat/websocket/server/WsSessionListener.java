package org.apache.tomcat.websocket.server;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:org/apache/tomcat/websocket/server/WsSessionListener.class */
public class WsSessionListener implements HttpSessionListener {
    private final WsServerContainer wsServerContainer;

    public WsSessionListener(WsServerContainer wsServerContainer) {
        this.wsServerContainer = wsServerContainer;
    }

    @Override // javax.servlet.http.HttpSessionListener
    public void sessionDestroyed(HttpSessionEvent se) {
        this.wsServerContainer.closeAuthenticatedSession(se.getSession().getId());
    }
}
