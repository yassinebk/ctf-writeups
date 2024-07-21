package org.springframework.cglib.proxy;

import java.lang.reflect.Method;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/proxy/MethodInterceptor.class */
public interface MethodInterceptor extends Callback {
    Object intercept(Object obj, Method method, Object[] objArr, MethodProxy methodProxy) throws Throwable;
}
