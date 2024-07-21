package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpMethod;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/client/ClientHttpRequestFactory.class */
public interface ClientHttpRequestFactory {
    ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException;
}
