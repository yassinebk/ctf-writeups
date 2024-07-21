package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.ClientHttpRequest;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/client/RequestCallback.class */
public interface RequestCallback {
    void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException;
}
