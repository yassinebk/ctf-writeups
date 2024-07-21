package org.springframework.web.servlet.handler;

import org.springframework.web.method.HandlerMethod;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/handler/HandlerMethodMappingNamingStrategy.class */
public interface HandlerMethodMappingNamingStrategy<T> {
    String getName(HandlerMethod handlerMethod, T t);
}
