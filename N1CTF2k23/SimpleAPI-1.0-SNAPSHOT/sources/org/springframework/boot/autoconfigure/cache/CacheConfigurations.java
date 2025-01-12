package org.springframework.boot.autoconfigure.cache;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cache/CacheConfigurations.class */
final class CacheConfigurations {
    private static final Map<CacheType, Class<?>> MAPPINGS;

    static {
        Map<CacheType, Class<?>> mappings = new EnumMap<>(CacheType.class);
        mappings.put(CacheType.GENERIC, GenericCacheConfiguration.class);
        mappings.put(CacheType.EHCACHE, EhCacheCacheConfiguration.class);
        mappings.put(CacheType.HAZELCAST, HazelcastCacheConfiguration.class);
        mappings.put(CacheType.INFINISPAN, InfinispanCacheConfiguration.class);
        mappings.put(CacheType.JCACHE, JCacheCacheConfiguration.class);
        mappings.put(CacheType.COUCHBASE, CouchbaseCacheConfiguration.class);
        mappings.put(CacheType.REDIS, RedisCacheConfiguration.class);
        mappings.put(CacheType.CAFFEINE, CaffeineCacheConfiguration.class);
        mappings.put(CacheType.SIMPLE, SimpleCacheConfiguration.class);
        mappings.put(CacheType.NONE, NoOpCacheConfiguration.class);
        MAPPINGS = Collections.unmodifiableMap(mappings);
    }

    private CacheConfigurations() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getConfigurationClass(CacheType cacheType) {
        Class<?> configurationClass = MAPPINGS.get(cacheType);
        Assert.state(configurationClass != null, () -> {
            return "Unknown cache type " + cacheType;
        });
        return configurationClass.getName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CacheType getType(String configurationClassName) {
        for (Map.Entry<CacheType, Class<?>> entry : MAPPINGS.entrySet()) {
            if (entry.getValue().getName().equals(configurationClassName)) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Unknown configuration class " + configurationClassName);
    }
}
