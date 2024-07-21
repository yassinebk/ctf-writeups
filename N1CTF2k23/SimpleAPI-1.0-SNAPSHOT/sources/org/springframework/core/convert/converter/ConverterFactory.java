package org.springframework.core.convert.converter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/convert/converter/ConverterFactory.class */
public interface ConverterFactory<S, R> {
    <T extends R> Converter<S, T> getConverter(Class<T> cls);
}
