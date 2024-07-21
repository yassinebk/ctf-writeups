package org.springframework.web.server.session;

import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/server/session/WebSessionStore.class */
public interface WebSessionStore {
    Mono<WebSession> createWebSession();

    Mono<WebSession> retrieveSession(String str);

    Mono<Void> removeSession(String str);

    Mono<WebSession> updateLastAccessTime(WebSession webSession);
}
