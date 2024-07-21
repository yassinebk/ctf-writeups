package org.springframework.http.server;

import java.net.InetSocketAddress;
import java.security.Principal;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpRequest;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/ServerHttpRequest.class */
public interface ServerHttpRequest extends HttpRequest, HttpInputMessage {
    @Nullable
    Principal getPrincipal();

    InetSocketAddress getLocalAddress();

    InetSocketAddress getRemoteAddress();

    ServerHttpAsyncRequestControl getAsyncRequestControl(ServerHttpResponse serverHttpResponse);
}
