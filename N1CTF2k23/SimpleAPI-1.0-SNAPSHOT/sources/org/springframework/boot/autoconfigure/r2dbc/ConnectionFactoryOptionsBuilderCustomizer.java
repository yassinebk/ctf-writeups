package org.springframework.boot.autoconfigure.r2dbc;

import io.r2dbc.spi.ConnectionFactoryOptions;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/r2dbc/ConnectionFactoryOptionsBuilderCustomizer.class */
public interface ConnectionFactoryOptionsBuilderCustomizer {
    void customize(ConnectionFactoryOptions.Builder builder);
}
