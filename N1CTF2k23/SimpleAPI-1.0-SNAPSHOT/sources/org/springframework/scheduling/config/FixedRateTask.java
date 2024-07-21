package org.springframework.scheduling.config;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scheduling/config/FixedRateTask.class */
public class FixedRateTask extends IntervalTask {
    public FixedRateTask(Runnable runnable, long interval, long initialDelay) {
        super(runnable, interval, initialDelay);
    }
}
