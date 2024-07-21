package org.springframework.http;

import java.net.URI;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/HttpRequest.class */
public interface HttpRequest extends HttpMessage {
    String getMethodValue();

    URI getURI();

    @Nullable
    default HttpMethod getMethod() {
        return HttpMethod.resolve(getMethodValue());
    }
}
