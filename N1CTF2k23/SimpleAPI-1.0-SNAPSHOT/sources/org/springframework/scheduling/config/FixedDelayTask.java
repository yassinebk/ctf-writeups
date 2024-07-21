package org.springframework.scheduling.config;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scheduling/config/FixedDelayTask.class */
public class FixedDelayTask extends IntervalTask {
    public FixedDelayTask(Runnable runnable, long interval, long initialDelay) {
        super(runnable, interval, initialDelay);
    }
}
