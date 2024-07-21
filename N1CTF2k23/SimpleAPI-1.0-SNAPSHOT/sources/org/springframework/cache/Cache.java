package org.springframework.cache;

import java.util.concurrent.Callable;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/Cache.class */
public interface Cache {

    @FunctionalInterface
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/Cache$ValueWrapper.class */
    public interface ValueWrapper {
        @Nullable
        Object get();
    }

    String getName();

    Object getNativeCache();

    @Nullable
    ValueWrapper get(Object obj);

    @Nullable
    <T> T get(Object obj, @Nullable Class<T> cls);

    @Nullable
    <T> T get(Object obj, Callable<T> callable);

    void put(Object obj, @Nullable Object obj2);

    void evict(Object obj);

    void clear();

    @Nullable
    default ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
        ValueWrapper existingValue = get(key);
        if (existingValue == null) {
            put(key, value);
        }
        return existingValue;
    }

    default boolean evictIfPresent(Object key) {
        evict(key);
        return false;
    }

    default boolean invalidate() {
        clear();
        return false;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/Cache$ValueRetrievalException.class */
    public static class ValueRetrievalException extends RuntimeException {
        @Nullable
        private final Object key;

        public ValueRetrievalException(@Nullable Object key, Callable<?> loader, Throwable ex) {
            super(String.format("Value for key '%s' could not be loaded using '%s'", key, loader), ex);
            this.key = key;
        }

        @Nullable
        public Object getKey() {
            return this.key;
        }
    }
}
