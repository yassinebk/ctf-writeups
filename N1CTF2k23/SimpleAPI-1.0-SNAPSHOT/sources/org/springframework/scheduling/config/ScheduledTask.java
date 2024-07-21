package org.springframework.scheduling.config;

import java.util.concurrent.ScheduledFuture;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scheduling/config/ScheduledTask.class */
public final class ScheduledTask {
    private final Task task;
    @Nullable
    volatile ScheduledFuture<?> future;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ScheduledTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return this.task;
    }

    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }

    public String toString() {
        return this.task.toString();
    }
}
