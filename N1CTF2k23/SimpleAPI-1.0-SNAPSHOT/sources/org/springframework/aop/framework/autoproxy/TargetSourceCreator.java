package org.springframework.aop.framework.autoproxy;

import org.springframework.aop.TargetSource;
import org.springframework.lang.Nullable;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/framework/autoproxy/TargetSourceCreator.class */
public interface TargetSourceCreator {
    @Nullable
    TargetSource getTargetSource(Class<?> cls, String str);
}
