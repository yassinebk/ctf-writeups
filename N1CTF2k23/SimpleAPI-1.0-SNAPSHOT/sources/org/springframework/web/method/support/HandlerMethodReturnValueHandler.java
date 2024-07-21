package org.springframework.web.method.support;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/method/support/HandlerMethodReturnValueHandler.class */
public interface HandlerMethodReturnValueHandler {
    boolean supportsReturnType(MethodParameter methodParameter);

    void handleReturnValue(@Nullable Object obj, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception;
}
