package org.springframework.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/interceptor/CacheOperationSource.class */
public interface CacheOperationSource {
    @Nullable
    Collection<CacheOperation> getCacheOperations(Method method, @Nullable Class<?> cls);

    default boolean isCandidateClass(Class<?> targetClass) {
        return true;
    }
}
