package org.springframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/ParameterNameDiscoverer.class */
public interface ParameterNameDiscoverer {
    @Nullable
    String[] getParameterNames(Method method);

    @Nullable
    String[] getParameterNames(Constructor<?> constructor);
}
