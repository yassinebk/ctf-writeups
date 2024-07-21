package org.springframework.boot.rsocket.messaging;

import org.springframework.messaging.rsocket.RSocketStrategies;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/rsocket/messaging/RSocketStrategiesCustomizer.class */
public interface RSocketStrategiesCustomizer {
    void customize(RSocketStrategies.Builder strategies);
}
