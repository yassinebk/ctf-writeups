package org.springframework.aop;

import java.lang.reflect.Method;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/MethodMatcher.class */
public interface MethodMatcher {
    public static final MethodMatcher TRUE = TrueMethodMatcher.INSTANCE;

    boolean matches(Method method, Class<?> cls);

    boolean isRuntime();

    boolean matches(Method method, Class<?> cls, Object... objArr);
}
