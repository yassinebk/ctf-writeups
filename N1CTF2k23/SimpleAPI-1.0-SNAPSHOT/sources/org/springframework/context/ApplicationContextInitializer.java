package org.springframework.context;

import org.springframework.context.ConfigurableApplicationContext;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/ApplicationContextInitializer.class */
public interface ApplicationContextInitializer<C extends ConfigurableApplicationContext> {
    void initialize(C c);
}
