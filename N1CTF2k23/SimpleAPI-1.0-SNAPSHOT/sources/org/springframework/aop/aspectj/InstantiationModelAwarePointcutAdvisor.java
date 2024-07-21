package org.springframework.aop.aspectj;

import org.springframework.aop.PointcutAdvisor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/aspectj/InstantiationModelAwarePointcutAdvisor.class */
public interface InstantiationModelAwarePointcutAdvisor extends PointcutAdvisor {
    boolean isLazy();

    boolean isAdviceInstantiated();
}
