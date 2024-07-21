package org.springframework.core.env;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/env/PropertySources.class */
public interface PropertySources extends Iterable<PropertySource<?>> {
    boolean contains(String str);

    @Nullable
    PropertySource<?> get(String str);

    default Stream<PropertySource<?>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
