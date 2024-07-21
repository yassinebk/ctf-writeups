package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.AsyncClientHttpRequest;
@FunctionalInterface
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/client/AsyncRequestCallback.class */
public interface AsyncRequestCallback {
    void doWithRequest(AsyncClientHttpRequest asyncClientHttpRequest) throws IOException;
}
