package org.springframework.aop.framework;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/framework/AopProxyFactory.class */
public interface AopProxyFactory {
    AopProxy createAopProxy(AdvisedSupport advisedSupport) throws AopConfigException;
}
