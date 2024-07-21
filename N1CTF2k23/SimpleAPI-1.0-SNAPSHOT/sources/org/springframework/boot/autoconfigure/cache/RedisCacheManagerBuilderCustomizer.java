package org.springframework.boot.autoconfigure.cache;

import org.springframework.data.redis.cache.RedisCacheManager;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cache/RedisCacheManagerBuilderCustomizer.class */
public interface RedisCacheManagerBuilderCustomizer {
    void customize(RedisCacheManager.RedisCacheManagerBuilder builder);
}
