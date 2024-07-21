package org.springframework.scheduling.config;

import org.springframework.scheduling.support.CronTrigger;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scheduling/config/CronTask.class */
public class CronTask extends TriggerTask {
    private final String expression;

    public CronTask(Runnable runnable, String expression) {
        this(runnable, new CronTrigger(expression));
    }

    public CronTask(Runnable runnable, CronTrigger cronTrigger) {
        super(runnable, cronTrigger);
        this.expression = cronTrigger.getExpression();
    }

    public String getExpression() {
        return this.expression;
    }
}
