package org.springframework.boot.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/task/TaskExecutorCustomizer.class */
public interface TaskExecutorCustomizer {
    void customize(ThreadPoolTaskExecutor taskExecutor);
}
