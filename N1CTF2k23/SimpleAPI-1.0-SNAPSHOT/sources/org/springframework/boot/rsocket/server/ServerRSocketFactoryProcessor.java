package org.springframework.boot.rsocket.server;

import io.rsocket.RSocketFactory;
@FunctionalInterface
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/rsocket/server/ServerRSocketFactoryProcessor.class */
public interface ServerRSocketFactoryProcessor {
    RSocketFactory.ServerRSocketFactory process(RSocketFactory.ServerRSocketFactory factory);
}
