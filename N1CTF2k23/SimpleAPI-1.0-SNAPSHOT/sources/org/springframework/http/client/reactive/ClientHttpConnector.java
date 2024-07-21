package org.springframework.http.client.reactive;

import java.net.URI;
import java.util.function.Function;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/client/reactive/ClientHttpConnector.class */
public interface ClientHttpConnector {
    Mono<ClientHttpResponse> connect(HttpMethod httpMethod, URI uri, Function<? super ClientHttpRequest, Mono<Void>> function);
}
