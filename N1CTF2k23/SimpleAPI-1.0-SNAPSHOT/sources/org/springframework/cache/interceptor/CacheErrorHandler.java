package org.springframework.cache.interceptor;

import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/interceptor/CacheErrorHandler.class */
public interface CacheErrorHandler {
    void handleCacheGetError(RuntimeException runtimeException, Cache cache, Object obj);

    void handleCachePutError(RuntimeException runtimeException, Cache cache, Object obj, @Nullable Object obj2);

    void handleCacheEvictError(RuntimeException runtimeException, Cache cache, Object obj);

    void handleCacheClearError(RuntimeException runtimeException, Cache cache);
}
