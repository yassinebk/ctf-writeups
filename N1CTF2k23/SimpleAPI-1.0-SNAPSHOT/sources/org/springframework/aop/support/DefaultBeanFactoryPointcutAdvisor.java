package org.springframework.aop.support;

import org.springframework.aop.Pointcut;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/support/DefaultBeanFactoryPointcutAdvisor.class */
public class DefaultBeanFactoryPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    private Pointcut pointcut = Pointcut.TRUE;

    public void setPointcut(@Nullable Pointcut pointcut) {
        this.pointcut = pointcut != null ? pointcut : Pointcut.TRUE;
    }

    @Override // org.springframework.aop.PointcutAdvisor
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override // org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor
    public String toString() {
        return getClass().getName() + ": pointcut [" + getPointcut() + "]; advice bean '" + getAdviceBeanName() + "'";
    }
}
