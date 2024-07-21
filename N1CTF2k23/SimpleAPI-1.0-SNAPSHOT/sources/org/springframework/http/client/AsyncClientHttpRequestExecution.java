package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.util.concurrent.ListenableFuture;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/client/AsyncClientHttpRequestExecution.class */
public interface AsyncClientHttpRequestExecution {
    ListenableFuture<ClientHttpResponse> executeAsync(HttpRequest httpRequest, byte[] bArr) throws IOException;
}