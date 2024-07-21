package org.springframework.cache;

import java.util.Collection;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/CacheManager.class */
public interface CacheManager {
    @Nullable
    Cache getCache(String str);

    Collection<String> getCacheNames();
}
