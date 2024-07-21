package org.springframework.scheduling.annotation;

import org.springframework.scheduling.config.ScheduledTaskRegistrar;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scheduling/annotation/SchedulingConfigurer.class */
public interface SchedulingConfigurer {
    void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar);
}
