package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpRequest;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/client/ClientHttpRequest.class */
public interface ClientHttpRequest extends HttpRequest, HttpOutputMessage {
    ClientHttpResponse execute() throws IOException;
}
