package org.springframework.web.context.request;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/context/request/AsyncWebRequestInterceptor.class */
public interface AsyncWebRequestInterceptor extends WebRequestInterceptor {
    void afterConcurrentHandlingStarted(WebRequest webRequest);
}
