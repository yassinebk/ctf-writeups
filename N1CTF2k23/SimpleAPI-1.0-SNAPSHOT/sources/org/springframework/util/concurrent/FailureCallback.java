package org.springframework.util.concurrent;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/concurrent/FailureCallback.class */
public interface FailureCallback {
    void onFailure(Throwable th);
}
