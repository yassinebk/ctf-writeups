package org.springframework.scheduling;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scheduling/SchedulingAwareRunnable.class */
public interface SchedulingAwareRunnable extends Runnable {
    boolean isLongLived();
}
