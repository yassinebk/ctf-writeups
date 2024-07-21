package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpMethod;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/client/AsyncClientHttpRequestFactory.class */
public interface AsyncClientHttpRequestFactory {
    AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) throws IOException;
}
