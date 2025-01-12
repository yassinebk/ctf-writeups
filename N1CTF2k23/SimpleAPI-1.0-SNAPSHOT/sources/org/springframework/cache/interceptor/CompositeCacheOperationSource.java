package org.springframework.cache.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/interceptor/CompositeCacheOperationSource.class */
public class CompositeCacheOperationSource implements CacheOperationSource, Serializable {
    private final CacheOperationSource[] cacheOperationSources;

    public CompositeCacheOperationSource(CacheOperationSource... cacheOperationSources) {
        Assert.notEmpty(cacheOperationSources, "CacheOperationSource array must not be empty");
        this.cacheOperationSources = cacheOperationSources;
    }

    public final CacheOperationSource[] getCacheOperationSources() {
        return this.cacheOperationSources;
    }

    @Override // org.springframework.cache.interceptor.CacheOperationSource
    public boolean isCandidateClass(Class<?> targetClass) {
        CacheOperationSource[] cacheOperationSourceArr;
        for (CacheOperationSource source : this.cacheOperationSources) {
            if (source.isCandidateClass(targetClass)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.springframework.cache.interceptor.CacheOperationSource
    @Nullable
    public Collection<CacheOperation> getCacheOperations(Method method, @Nullable Class<?> targetClass) {
        CacheOperationSource[] cacheOperationSourceArr;
        Collection<CacheOperation> ops = null;
        for (CacheOperationSource source : this.cacheOperationSources) {
            Collection<CacheOperation> cacheOperations = source.getCacheOperations(method, targetClass);
            if (cacheOperations != null) {
                if (ops == null) {
                    ops = new ArrayList<>();
                }
                ops.addAll(cacheOperations);
            }
        }
        return ops;
    }
}
