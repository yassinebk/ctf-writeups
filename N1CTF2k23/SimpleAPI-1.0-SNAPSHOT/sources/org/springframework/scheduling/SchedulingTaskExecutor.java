package org.springframework.scheduling;

import org.springframework.core.task.AsyncTaskExecutor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scheduling/SchedulingTaskExecutor.class */
public interface SchedulingTaskExecutor extends AsyncTaskExecutor {
    default boolean prefersShortLivedTasks() {
        return true;
    }
}
