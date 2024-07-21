package org.aopalliance.intercept;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/aopalliance/intercept/MethodInterceptor.class */
public interface MethodInterceptor extends Interceptor {
    Object invoke(MethodInvocation methodInvocation) throws Throwable;
}
