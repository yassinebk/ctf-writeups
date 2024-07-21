package org.springframework.boot.autoconfigure.cassandra;

import com.datastax.oss.driver.api.core.config.ProgrammaticDriverConfigLoaderBuilder;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/DriverConfigLoaderBuilderCustomizer.class */
public interface DriverConfigLoaderBuilderCustomizer {
    void customize(ProgrammaticDriverConfigLoaderBuilder builder);
}
