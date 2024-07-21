package org.springframework.cache.annotation;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/annotation/CachingConfigurer.class */
public interface CachingConfigurer {
    @Nullable
    CacheManager cacheManager();

    @Nullable
    CacheResolver cacheResolver();

    @Nullable
    KeyGenerator keyGenerator();

    @Nullable
    CacheErrorHandler errorHandler();
}
