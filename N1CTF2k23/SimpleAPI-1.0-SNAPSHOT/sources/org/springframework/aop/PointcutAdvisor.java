package org.springframework.aop;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/PointcutAdvisor.class */
public interface PointcutAdvisor extends Advisor {
    Pointcut getPointcut();
}