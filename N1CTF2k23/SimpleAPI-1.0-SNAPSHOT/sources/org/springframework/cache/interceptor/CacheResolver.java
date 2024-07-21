package org.springframework.cache.interceptor;

import java.util.Collection;
import org.springframework.cache.Cache;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/interceptor/CacheResolver.class */
public interface CacheResolver {
    Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> cacheOperationInvocationContext);
}
