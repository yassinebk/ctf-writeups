package org.springframework.web.server;

import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/server/WebFilter.class */
public interface WebFilter {
    Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain);
}
