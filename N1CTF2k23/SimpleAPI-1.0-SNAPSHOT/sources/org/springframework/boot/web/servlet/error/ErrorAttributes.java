package org.springframework.boot.web.servlet.error;

import java.util.Collections;
import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.web.context.request.WebRequest;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/servlet/error/ErrorAttributes.class */
public interface ErrorAttributes {
    Throwable getError(WebRequest webRequest);

    @Deprecated
    default Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        return Collections.emptyMap();
    }

    default Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        return getErrorAttributes(webRequest, options.isIncluded(ErrorAttributeOptions.Include.STACK_TRACE));
    }
}
