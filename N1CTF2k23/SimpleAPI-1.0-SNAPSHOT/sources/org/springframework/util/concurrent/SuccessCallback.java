package org.springframework.util.concurrent;

import org.springframework.lang.Nullable;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/concurrent/SuccessCallback.class */
public interface SuccessCallback<T> {
    void onSuccess(@Nullable T t);
}
