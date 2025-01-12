package org.springframework.http.server;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/ServerHttpResponse.class */
public interface ServerHttpResponse extends HttpOutputMessage, Flushable, Closeable {
    void setStatusCode(HttpStatus httpStatus);

    @Override // java.io.Flushable
    void flush() throws IOException;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();
}
