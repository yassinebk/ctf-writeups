package org.springframework.boot.autoconfigure.cache;

import java.util.Properties;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cache/JCachePropertiesCustomizer.class */
public interface JCachePropertiesCustomizer {
    void customize(CacheProperties cacheProperties, Properties properties);
}
