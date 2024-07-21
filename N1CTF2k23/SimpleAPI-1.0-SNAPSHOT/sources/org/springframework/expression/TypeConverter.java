package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/TypeConverter.class */
public interface TypeConverter {
    boolean canConvert(@Nullable TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2);

    @Nullable
    Object convertValue(@Nullable Object obj, @Nullable TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2);
}
