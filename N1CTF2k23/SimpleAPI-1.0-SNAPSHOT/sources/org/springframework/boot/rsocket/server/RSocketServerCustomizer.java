package org.springframework.boot.rsocket.server;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/rsocket/server/RSocketServerCustomizer.class */
public interface RSocketServerCustomizer {
    void customize(io.rsocket.core.RSocketServer rSocketServer);
}
