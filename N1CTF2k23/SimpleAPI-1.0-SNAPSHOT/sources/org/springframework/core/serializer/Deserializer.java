package org.springframework.core.serializer;

import java.io.IOException;
import java.io.InputStream;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/serializer/Deserializer.class */
public interface Deserializer<T> {
    T deserialize(InputStream inputStream) throws IOException;
}
