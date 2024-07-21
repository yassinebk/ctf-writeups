package org.springframework.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.io.OutputStream;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/StreamingResponseBody.class */
public interface StreamingResponseBody {
    void writeTo(OutputStream outputStream) throws IOException;
}
