package org.springframework.aop.support;

import java.io.Serializable;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/support/DefaultPointcutAdvisor.class */
public class DefaultPointcutAdvisor extends AbstractGenericPointcutAdvisor implements Serializable {
    private Pointcut pointcut;

    public DefaultPointcutAdvisor() {
        this.pointcut = Pointcut.TRUE;
    }

    public DefaultPointcutAdvisor(Advice advice) {
        this(Pointcut.TRUE, advice);
    }

    public DefaultPointcutAdvisor(Pointcut pointcut, Advice advice) {
        this.pointcut = Pointcut.TRUE;
        this.pointcut = pointcut;
        setAdvice(advice);
    }

    public void setPointcut(@Nullable Pointcut pointcut) {
        this.pointcut = pointcut != null ? pointcut : Pointcut.TRUE;
    }

    @Override // org.springframework.aop.PointcutAdvisor
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override // org.springframework.aop.support.AbstractGenericPointcutAdvisor
    public String toString() {
        return getClass().getName() + ": pointcut [" + getPointcut() + "]; advice [" + getAdvice() + "]";
    }
}
