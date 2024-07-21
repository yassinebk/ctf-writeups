package org.springframework.boot;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/SpringApplicationRunListener.class */
public interface SpringApplicationRunListener {
    default void starting() {
    }

    default void environmentPrepared(ConfigurableEnvironment environment) {
    }

    default void contextPrepared(ConfigurableApplicationContext context) {
    }

    default void contextLoaded(ConfigurableApplicationContext context) {
    }

    default void started(ConfigurableApplicationContext context) {
    }

    default void running(ConfigurableApplicationContext context) {
    }

    default void failed(ConfigurableApplicationContext context, Throwable exception) {
    }
}
