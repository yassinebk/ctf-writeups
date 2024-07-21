package org.springframework.cache.interceptor;

import java.lang.reflect.Method;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/interceptor/KeyGenerator.class */
public interface KeyGenerator {
    Object generate(Object obj, Method method, Object... objArr);
}
