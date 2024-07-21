package org.springframework.aop.interceptor;

import java.io.Serializable;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.ConcurrencyThrottleSupport;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/interceptor/ConcurrencyThrottleInterceptor.class */
public class ConcurrencyThrottleInterceptor extends ConcurrencyThrottleSupport implements MethodInterceptor, Serializable {
    public ConcurrencyThrottleInterceptor() {
        setConcurrencyLimit(1);
    }

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        beforeAccess();
        try {
            return methodInvocation.proceed();
        } finally {
            afterAccess();
        }
    }
}
