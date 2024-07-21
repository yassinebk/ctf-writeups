package org.springframework.boot.autoconfigure.web.reactive.function.client;

import reactor.netty.http.client.HttpClient;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/function/client/ReactorNettyHttpClientMapper.class */
public interface ReactorNettyHttpClientMapper {
    HttpClient configure(HttpClient httpClient);
}
