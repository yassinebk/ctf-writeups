package org.springframework.core.convert.converter;

import org.springframework.lang.Nullable;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/converter/Converter.class */
public interface Converter<S, T> {
    @Nullable
    T convert(S s);
}
