package org.springframework.boot.autoconfigure.rsocket;

import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/rsocket/RSocketMessageHandlerCustomizer.class */
public interface RSocketMessageHandlerCustomizer {
    void customize(RSocketMessageHandler messageHandler);
}
