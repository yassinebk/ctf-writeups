package org.springframework.http.client.reactive;

import java.net.URI;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpMethod;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.util.MultiValueMap;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/client/reactive/ClientHttpRequest.class */
public interface ClientHttpRequest extends ReactiveHttpOutputMessage {
    HttpMethod getMethod();

    URI getURI();

    MultiValueMap<String, HttpCookie> getCookies();
}
