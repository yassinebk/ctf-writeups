package org.springframework.aop.support;

import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/support/NameMatchMethodPointcutAdvisor.class */
public class NameMatchMethodPointcutAdvisor extends AbstractGenericPointcutAdvisor {
    private final NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();

    public NameMatchMethodPointcutAdvisor() {
    }

    public NameMatchMethodPointcutAdvisor(Advice advice) {
        setAdvice(advice);
    }

    public void setClassFilter(ClassFilter classFilter) {
        this.pointcut.setClassFilter(classFilter);
    }

    public void setMappedName(String mappedName) {
        this.pointcut.setMappedName(mappedName);
    }

    public void setMappedNames(String... mappedNames) {
        this.pointcut.setMappedNames(mappedNames);
    }

    public NameMatchMethodPointcut addMethodName(String name) {
        return this.pointcut.addMethodName(name);
    }

    @Override // org.springframework.aop.PointcutAdvisor
    public Pointcut getPointcut() {
        return this.pointcut;
    }
}
