package org.apache.tomcat.websocket;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:org/apache/tomcat/websocket/WsContainerProvider.class */
public class WsContainerProvider extends ContainerProvider {
    @Override // javax.websocket.ContainerProvider
    protected WebSocketContainer getContainer() {
        return new WsWebSocketContainer();
    }
}
