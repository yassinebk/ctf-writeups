package org.springframework.web.method.support;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/method/support/AsyncHandlerMethodReturnValueHandler.class */
public interface AsyncHandlerMethodReturnValueHandler extends HandlerMethodReturnValueHandler {
    boolean isAsyncReturnValue(@Nullable Object obj, MethodParameter methodParameter);
}
