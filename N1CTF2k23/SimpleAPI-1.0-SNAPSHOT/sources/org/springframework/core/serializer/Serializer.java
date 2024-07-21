package org.springframework.core.serializer;

import java.io.IOException;
import java.io.OutputStream;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/serializer/Serializer.class */
public interface Serializer<T> {
    void serialize(T t, OutputStream outputStream) throws IOException;
}