package org.springframework.boot.context.properties.source;

import java.util.function.Predicate;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/source/ConfigurationPropertyState.class */
public enum ConfigurationPropertyState {
    PRESENT,
    ABSENT,
    UNKNOWN;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> ConfigurationPropertyState search(Iterable<T> source, Predicate<T> predicate) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(predicate, "Predicate must not be null");
        for (T item : source) {
            if (predicate.test(item)) {
                return PRESENT;
            }
        }
        return ABSENT;
    }
}
