package org.springframework.http.client;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/client/ClientHttpRequestInitializer.class */
public interface ClientHttpRequestInitializer {
    void initialize(ClientHttpRequest clientHttpRequest);
}
