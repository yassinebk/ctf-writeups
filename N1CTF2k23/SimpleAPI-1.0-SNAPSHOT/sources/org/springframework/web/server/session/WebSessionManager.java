package org.springframework.web.server.session;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/server/session/WebSessionManager.class */
public interface WebSessionManager {
    Mono<WebSession> getSession(ServerWebExchange serverWebExchange);
}
