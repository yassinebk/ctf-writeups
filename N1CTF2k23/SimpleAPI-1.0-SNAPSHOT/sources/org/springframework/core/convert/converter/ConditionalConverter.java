package org.springframework.core.convert.converter;

import org.springframework.core.convert.TypeDescriptor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/converter/ConditionalConverter.class */
public interface ConditionalConverter {
    boolean matches(TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2);
}
