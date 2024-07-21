package org.springframework.boot.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/task/TaskSchedulerCustomizer.class */
public interface TaskSchedulerCustomizer {
    void customize(ThreadPoolTaskScheduler taskScheduler);
}
