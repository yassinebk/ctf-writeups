package org.springframework.boot.rsocket.server;

import io.rsocket.SocketAcceptor;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/rsocket/server/RSocketServerFactory.class */
public interface RSocketServerFactory {
    RSocketServer create(SocketAcceptor socketAcceptor);
}
