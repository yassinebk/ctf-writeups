package org.springframework.boot.rsocket.server;

import java.net.InetSocketAddress;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/rsocket/server/RSocketServer.class */
public interface RSocketServer {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/rsocket/server/RSocketServer$Transport.class */
    public enum Transport {
        TCP,
        WEBSOCKET
    }

    void start() throws RSocketServerException;

    void stop() throws RSocketServerException;

    InetSocketAddress address();
}
