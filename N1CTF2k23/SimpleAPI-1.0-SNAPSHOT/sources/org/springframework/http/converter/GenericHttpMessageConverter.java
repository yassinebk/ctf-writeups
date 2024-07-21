package org.springframework.http.converter;

import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/converter/GenericHttpMessageConverter.class */
public interface GenericHttpMessageConverter<T> extends HttpMessageConverter<T> {
    boolean canRead(Type type, @Nullable Class<?> cls, @Nullable MediaType mediaType);

    T read(Type type, @Nullable Class<?> cls, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException;

    boolean canWrite(@Nullable Type type, Class<?> cls, @Nullable MediaType mediaType);

    void write(T t, @Nullable Type type, @Nullable MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException;
}
