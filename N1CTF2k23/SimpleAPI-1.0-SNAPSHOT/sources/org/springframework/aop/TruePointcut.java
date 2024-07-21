package org.springframework.aop;

import java.io.Serializable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/TruePointcut.class */
final class TruePointcut implements Pointcut, Serializable {
    public static final TruePointcut INSTANCE = new TruePointcut();

    private TruePointcut() {
    }

    @Override // org.springframework.aop.Pointcut
    public ClassFilter getClassFilter() {
        return ClassFilter.TRUE;
    }

    @Override // org.springframework.aop.Pointcut
    public MethodMatcher getMethodMatcher() {
        return MethodMatcher.TRUE;
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public String toString() {
        return "Pointcut.TRUE";
    }
}
