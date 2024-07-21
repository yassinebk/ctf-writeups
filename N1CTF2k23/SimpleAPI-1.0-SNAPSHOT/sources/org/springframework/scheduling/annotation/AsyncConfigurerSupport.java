package org.springframework.scheduling.annotation;

import java.util.concurrent.Executor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/scheduling/annotation/AsyncConfigurerSupport.class */
public class AsyncConfigurerSupport implements AsyncConfigurer {
    @Override // org.springframework.scheduling.annotation.AsyncConfigurer
    public Executor getAsyncExecutor() {
        return null;
    }

    @Override // org.springframework.scheduling.annotation.AsyncConfigurer
    @Nullable
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
