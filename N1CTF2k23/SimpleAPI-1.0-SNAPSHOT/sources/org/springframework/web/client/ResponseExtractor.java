package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/client/ResponseExtractor.class */
public interface ResponseExtractor<T> {
    @Nullable
    T extractData(ClientHttpResponse clientHttpResponse) throws IOException;
}
