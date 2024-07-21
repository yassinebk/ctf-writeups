package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.MethodMatcher;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/framework/InterceptorAndDynamicMethodMatcher.class */
class InterceptorAndDynamicMethodMatcher {
    final MethodInterceptor interceptor;
    final MethodMatcher methodMatcher;

    public InterceptorAndDynamicMethodMatcher(MethodInterceptor interceptor, MethodMatcher methodMatcher) {
        this.interceptor = interceptor;
        this.methodMatcher = methodMatcher;
    }
}
