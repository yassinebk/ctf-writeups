package org.springframework.boot.rsocket.server;

import java.net.InetAddress;
import org.springframework.boot.rsocket.server.RSocketServer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/rsocket/server/ConfigurableRSocketServerFactory.class */
public interface ConfigurableRSocketServerFactory {
    void setPort(int port);

    void setAddress(InetAddress address);

    void setTransport(RSocketServer.Transport transport);
}
