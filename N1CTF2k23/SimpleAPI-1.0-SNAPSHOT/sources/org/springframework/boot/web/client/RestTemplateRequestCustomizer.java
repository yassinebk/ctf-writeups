package org.springframework.boot.web.client;

import org.springframework.http.client.ClientHttpRequest;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/client/RestTemplateRequestCustomizer.class */
public interface RestTemplateRequestCustomizer<T extends ClientHttpRequest> {
    void customize(T request);
}
