package org.springframework.http.server;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/ServerHttpAsyncRequestControl.class */
public interface ServerHttpAsyncRequestControl {
    void start();

    void start(long j);

    boolean isStarted();

    void complete();

    boolean isCompleted();
}
