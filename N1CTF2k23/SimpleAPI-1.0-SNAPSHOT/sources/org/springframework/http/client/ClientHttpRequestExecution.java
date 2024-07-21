package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/client/ClientHttpRequestExecution.class */
public interface ClientHttpRequestExecution {
    ClientHttpResponse execute(HttpRequest httpRequest, byte[] bArr) throws IOException;
}
