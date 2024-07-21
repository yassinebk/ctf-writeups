package org.springframework.boot.origin;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/origin/OriginLookup.class */
public interface OriginLookup<K> {
    Origin getOrigin(K key);

    default boolean isImmutable() {
        return false;
    }

    static <K> Origin getOrigin(Object source, K key) {
        if (!(source instanceof OriginLookup)) {
            return null;
        }
        try {
            return ((OriginLookup) source).getOrigin(key);
        } catch (Throwable th) {
            return null;
        }
    }
}
