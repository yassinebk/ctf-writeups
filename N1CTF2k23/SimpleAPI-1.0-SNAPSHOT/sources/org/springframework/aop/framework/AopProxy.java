package org.springframework.aop.framework;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/framework/AopProxy.class */
public interface AopProxy {
    Object getProxy();

    Object getProxy(@Nullable ClassLoader classLoader);
}
