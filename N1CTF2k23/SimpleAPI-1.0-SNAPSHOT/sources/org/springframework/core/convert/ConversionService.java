package org.springframework.core.convert;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/ConversionService.class */
public interface ConversionService {
    boolean canConvert(@Nullable Class<?> cls, Class<?> cls2);

    boolean canConvert(@Nullable TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2);

    @Nullable
    <T> T convert(@Nullable Object obj, Class<T> cls);

    @Nullable
    Object convert(@Nullable Object obj, @Nullable TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2);
}
