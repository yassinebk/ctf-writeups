package org.springframework.boot.web.reactive.error;

import java.util.Collections;
import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/reactive/error/ErrorAttributes.class */
public interface ErrorAttributes {
    Throwable getError(ServerRequest request);

    void storeErrorInformation(Throwable error, ServerWebExchange exchange);

    @Deprecated
    default Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        return Collections.emptyMap();
    }

    default Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        return getErrorAttributes(request, options.isIncluded(ErrorAttributeOptions.Include.STACK_TRACE));
    }
}
