package org.springframework.core.task;

import java.util.concurrent.Executor;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/task/TaskExecutor.class */
public interface TaskExecutor extends Executor {
    @Override // java.util.concurrent.Executor
    void execute(Runnable runnable);
}
