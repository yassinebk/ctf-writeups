package org.springframework.context;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/Lifecycle.class */
public interface Lifecycle {
    void start();

    void stop();

    boolean isRunning();
}
