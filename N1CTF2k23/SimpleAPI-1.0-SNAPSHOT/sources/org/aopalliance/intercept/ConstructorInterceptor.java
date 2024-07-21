package org.aopalliance.intercept;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/aopalliance/intercept/ConstructorInterceptor.class */
public interface ConstructorInterceptor extends Interceptor {
    Object construct(ConstructorInvocation constructorInvocation) throws Throwable;
}
