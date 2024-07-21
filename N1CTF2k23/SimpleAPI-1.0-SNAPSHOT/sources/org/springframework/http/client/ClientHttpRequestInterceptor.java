package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/client/ClientHttpRequestInterceptor.class */
public interface ClientHttpRequestInterceptor {
    ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bArr, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException;
}
