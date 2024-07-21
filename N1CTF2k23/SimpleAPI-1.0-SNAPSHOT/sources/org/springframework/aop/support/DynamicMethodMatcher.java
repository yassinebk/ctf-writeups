package org.springframework.aop.support;

import java.lang.reflect.Method;
import org.springframework.aop.MethodMatcher;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/support/DynamicMethodMatcher.class */
public abstract class DynamicMethodMatcher implements MethodMatcher {
    @Override // org.springframework.aop.MethodMatcher
    public final boolean isRuntime() {
        return true;
    }

    @Override // org.springframework.aop.MethodMatcher
    public boolean matches(Method method, Class<?> targetClass) {
        return true;
    }
}
