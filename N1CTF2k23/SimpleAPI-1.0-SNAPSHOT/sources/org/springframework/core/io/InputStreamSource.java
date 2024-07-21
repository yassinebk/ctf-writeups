package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/InputStreamSource.class */
public interface InputStreamSource {
    InputStream getInputStream() throws IOException;
}
