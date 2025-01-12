package org.springframework.web.cors.reactive;

import org.springframework.lang.Nullable;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.server.ServerWebExchange;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/cors/reactive/CorsProcessor.class */
public interface CorsProcessor {
    boolean process(@Nullable CorsConfiguration corsConfiguration, ServerWebExchange serverWebExchange);
}
