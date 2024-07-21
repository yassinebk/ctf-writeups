package org.springframework.web.server.session;

import java.util.List;
import org.springframework.web.server.ServerWebExchange;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/server/session/WebSessionIdResolver.class */
public interface WebSessionIdResolver {
    List<String> resolveSessionIds(ServerWebExchange serverWebExchange);

    void setSessionId(ServerWebExchange serverWebExchange, String str);

    void expireSession(ServerWebExchange serverWebExchange);
}
