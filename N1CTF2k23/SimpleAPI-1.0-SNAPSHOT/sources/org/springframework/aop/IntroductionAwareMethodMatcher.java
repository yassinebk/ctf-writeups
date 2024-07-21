package org.springframework.aop;

import java.lang.reflect.Method;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/IntroductionAwareMethodMatcher.class */
public interface IntroductionAwareMethodMatcher extends MethodMatcher {
    boolean matches(Method method, Class<?> cls, boolean z);
}
