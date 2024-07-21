package org.springframework.http.server.reactive;

import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/reactive/HttpHandler.class */
public interface HttpHandler {
    Mono<Void> handle(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse);
}
