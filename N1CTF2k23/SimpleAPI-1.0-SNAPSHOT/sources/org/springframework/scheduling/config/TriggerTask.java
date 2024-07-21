package org.springframework.scheduling.config;

import org.springframework.scheduling.Trigger;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scheduling/config/TriggerTask.class */
public class TriggerTask extends Task {
    private final Trigger trigger;

    public TriggerTask(Runnable runnable, Trigger trigger) {
        super(runnable);
        Assert.notNull(trigger, "Trigger must not be null");
        this.trigger = trigger;
    }

    public Trigger getTrigger() {
        return this.trigger;
    }
}
