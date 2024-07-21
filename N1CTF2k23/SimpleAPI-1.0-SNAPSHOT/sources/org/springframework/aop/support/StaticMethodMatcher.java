package org.springframework.aop.support;

import java.lang.reflect.Method;
import org.springframework.aop.MethodMatcher;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/support/StaticMethodMatcher.class */
public abstract class StaticMethodMatcher implements MethodMatcher {
    @Override // org.springframework.aop.MethodMatcher
    public final boolean isRuntime() {
        return false;
    }

    @Override // org.springframework.aop.MethodMatcher
    public final boolean matches(Method method, Class<?> targetClass, Object... args) {
        throw new UnsupportedOperationException("Illegal MethodMatcher usage");
    }
}
