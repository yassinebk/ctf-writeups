package org.springframework.web.client;

import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/client/ResponseErrorHandler.class */
public interface ResponseErrorHandler {
    boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException;

    void handleError(ClientHttpResponse clientHttpResponse) throws IOException;

    default void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        handleError(response);
    }
}
