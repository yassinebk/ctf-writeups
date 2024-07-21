package org.springframework.aop;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/TargetClassAware.class */
public interface TargetClassAware {
    @Nullable
    Class<?> getTargetClass();
}
