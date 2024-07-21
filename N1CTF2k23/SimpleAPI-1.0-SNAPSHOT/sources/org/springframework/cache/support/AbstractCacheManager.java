package org.springframework.cache.support;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/support/AbstractCacheManager.class */
public abstract class AbstractCacheManager implements CacheManager, InitializingBean {
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap(16);
    private volatile Set<String> cacheNames = Collections.emptySet();

    protected abstract Collection<? extends Cache> loadCaches();

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        initializeCaches();
    }

    public void initializeCaches() {
        Collection<? extends Cache> caches = loadCaches();
        synchronized (this.cacheMap) {
            this.cacheNames = Collections.emptySet();
            this.cacheMap.clear();
            Set<String> cacheNames = new LinkedHashSet<>(caches.size());
            for (Cache cache : caches) {
                String name = cache.getName();
                this.cacheMap.put(name, decorateCache(cache));
                cacheNames.add(name);
            }
            this.cacheNames = Collections.unmodifiableSet(cacheNames);
        }
    }

    @Override // org.springframework.cache.CacheManager
    @Nullable
    public Cache getCache(String name) {
        Cache cache = this.cacheMap.get(name);
        if (cache != null) {
            return cache;
        }
        Cache missingCache = getMissingCache(name);
        if (missingCache != null) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    cache = decorateCache(missingCache);
                    this.cacheMap.put(name, cache);
                    updateCacheNames(name);
                }
            }
        }
        return cache;
    }

    @Override // org.springframework.cache.CacheManager
    public Collection<String> getCacheNames() {
        return this.cacheNames;
    }

    @Nullable
    protected final Cache lookupCache(String name) {
        return this.cacheMap.get(name);
    }

    @Deprecated
    protected final void addCache(Cache cache) {
        String name = cache.getName();
        synchronized (this.cacheMap) {
            if (this.cacheMap.put(name, decorateCache(cache)) == null) {
                updateCacheNames(name);
            }
        }
    }

    private void updateCacheNames(String name) {
        Set<String> cacheNames = new LinkedHashSet<>(this.cacheNames.size() + 1);
        cacheNames.addAll(this.cacheNames);
        cacheNames.add(name);
        this.cacheNames = Collections.unmodifiableSet(cacheNames);
    }

    protected Cache decorateCache(Cache cache) {
        return cache;
    }

    @Nullable
    protected Cache getMissingCache(String name) {
        return null;
    }
}
