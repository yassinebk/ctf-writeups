package org.springframework.boot.rsocket.context;

import org.springframework.boot.rsocket.server.RSocketServer;
import org.springframework.context.ApplicationEvent;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/rsocket/context/RSocketServerInitializedEvent.class */
public class RSocketServerInitializedEvent extends ApplicationEvent {
    public RSocketServerInitializedEvent(RSocketServer server) {
        super(server);
    }

    public RSocketServer getServer() {
        return getSource();
    }

    @Override // java.util.EventObject
    public RSocketServer getSource() {
        return (RSocketServer) super.getSource();
    }
}
