package org.springframework.boot.web.embedded.netty;

import java.util.function.Function;
import reactor.netty.http.server.HttpServerRoutes;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/netty/NettyRouteProvider.class */
public interface NettyRouteProvider extends Function<HttpServerRoutes, HttpServerRoutes> {
}
