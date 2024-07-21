package org.springframework.aop.interceptor;

import java.lang.reflect.Method;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/interceptor/AsyncUncaughtExceptionHandler.class */
public interface AsyncUncaughtExceptionHandler {
    void handleUncaughtException(Throwable th, Method method, Object... objArr);
}
