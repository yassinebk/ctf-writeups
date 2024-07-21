package org.springframework.boot.autoconfigure.cache;

import javax.cache.CacheManager;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cache/JCacheManagerCustomizer.class */
public interface JCacheManagerCustomizer {
    void customize(CacheManager cacheManager);
}
