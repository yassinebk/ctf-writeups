package org.springframework.boot.logging;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/logging/LoggingInitializationContext.class */
public class LoggingInitializationContext {
    private final ConfigurableEnvironment environment;

    public LoggingInitializationContext(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return this.environment;
    }
}
