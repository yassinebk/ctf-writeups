package org.springframework.boot.autoconfigure.cache;

import com.couchbase.client.java.Cluster;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.data.couchbase.cache.CouchbaseCacheManager;
import org.springframework.util.ObjectUtils;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Cluster.class, CouchbaseClientFactory.class, CouchbaseCacheManager.class})
@ConditionalOnSingleCandidate(CouchbaseClientFactory.class)
@ConditionalOnMissingBean({CacheManager.class})
@Conditional({CacheCondition.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cache/CouchbaseCacheConfiguration.class */
public class CouchbaseCacheConfiguration {
    @Bean
    public CouchbaseCacheManager cacheManager(CacheProperties cacheProperties, CacheManagerCustomizers customizers, CouchbaseClientFactory clientFactory) {
        List<String> cacheNames = cacheProperties.getCacheNames();
        CouchbaseCacheManager.CouchbaseCacheManagerBuilder builder = CouchbaseCacheManager.builder(clientFactory);
        CacheProperties.Couchbase couchbase = cacheProperties.getCouchbase();
        org.springframework.data.couchbase.cache.CouchbaseCacheConfiguration config = org.springframework.data.couchbase.cache.CouchbaseCacheConfiguration.defaultCacheConfig();
        if (couchbase.getExpiration() != null) {
            config = config.entryExpiry(couchbase.getExpiration());
        }
        builder.cacheDefaults(config);
        if (!ObjectUtils.isEmpty(cacheNames)) {
            builder.initialCacheNames(new LinkedHashSet(cacheNames));
        }
        CouchbaseCacheManager cacheManager = builder.build();
        return customizers.customize(cacheManager);
    }
}
