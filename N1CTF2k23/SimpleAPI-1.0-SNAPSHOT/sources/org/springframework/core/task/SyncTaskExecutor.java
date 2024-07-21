package org.springframework.core.task;

import java.io.Serializable;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/task/SyncTaskExecutor.class */
public class SyncTaskExecutor implements TaskExecutor, Serializable {
    @Override // org.springframework.core.task.TaskExecutor, java.util.concurrent.Executor
    public void execute(Runnable task) {
        Assert.notNull(task, "Runnable must not be null");
        task.run();
    }
}
