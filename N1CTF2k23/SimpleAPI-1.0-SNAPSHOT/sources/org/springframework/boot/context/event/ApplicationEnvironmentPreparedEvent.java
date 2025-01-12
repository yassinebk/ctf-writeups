package org.springframework.boot.context.event;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/event/ApplicationEnvironmentPreparedEvent.class */
public class ApplicationEnvironmentPreparedEvent extends SpringApplicationEvent {
    private final ConfigurableEnvironment environment;

    public ApplicationEnvironmentPreparedEvent(SpringApplication application, String[] args, ConfigurableEnvironment environment) {
        super(application, args);
        this.environment = environment;
    }

    public ConfigurableEnvironment getEnvironment() {
        return this.environment;
    }
}
