package org.springframework.web.servlet.function;

import org.springframework.web.servlet.function.ServerResponse;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/HandlerFunction.class */
public interface HandlerFunction<T extends ServerResponse> {
    T handle(ServerRequest serverRequest) throws Exception;
}
