package org.springframework.boot.autoconfigure.flyway;

import org.flywaydb.core.api.configuration.FluentConfiguration;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayConfigurationCustomizer.class */
public interface FlywayConfigurationCustomizer {
    void customize(FluentConfiguration configuration);
}
