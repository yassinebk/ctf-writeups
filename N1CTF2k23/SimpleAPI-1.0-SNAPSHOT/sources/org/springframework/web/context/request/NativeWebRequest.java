package org.springframework.web.context.request;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/context/request/NativeWebRequest.class */
public interface NativeWebRequest extends WebRequest {
    Object getNativeRequest();

    @Nullable
    Object getNativeResponse();

    @Nullable
    <T> T getNativeRequest(@Nullable Class<T> cls);

    @Nullable
    <T> T getNativeResponse(@Nullable Class<T> cls);
}
