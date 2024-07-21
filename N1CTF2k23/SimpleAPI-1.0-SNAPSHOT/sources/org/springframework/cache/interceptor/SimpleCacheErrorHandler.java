package org.springframework.cache.interceptor;

import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/interceptor/SimpleCacheErrorHandler.class */
public class SimpleCacheErrorHandler implements CacheErrorHandler {
    @Override // org.springframework.cache.interceptor.CacheErrorHandler
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        throw exception;
    }

    @Override // org.springframework.cache.interceptor.CacheErrorHandler
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, @Nullable Object value) {
        throw exception;
    }

    @Override // org.springframework.cache.interceptor.CacheErrorHandler
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        throw exception;
    }

    @Override // org.springframework.cache.interceptor.CacheErrorHandler
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        throw exception;
    }
}
