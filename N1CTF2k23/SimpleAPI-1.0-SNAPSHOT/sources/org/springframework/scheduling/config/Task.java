package org.springframework.scheduling.config;

import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scheduling/config/Task.class */
public class Task {
    private final Runnable runnable;

    public Task(Runnable runnable) {
        Assert.notNull(runnable, "Runnable must not be null");
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return this.runnable;
    }

    public String toString() {
        return this.runnable.toString();
    }
}
