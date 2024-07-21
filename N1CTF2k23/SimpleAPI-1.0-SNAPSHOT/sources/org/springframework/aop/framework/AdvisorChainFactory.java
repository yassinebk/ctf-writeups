package org.springframework.aop.framework;

import java.lang.reflect.Method;
import java.util.List;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/framework/AdvisorChainFactory.class */
public interface AdvisorChainFactory {
    List<Object> getInterceptorsAndDynamicInterceptionAdvice(Advised advised, Method method, @Nullable Class<?> cls);
}
