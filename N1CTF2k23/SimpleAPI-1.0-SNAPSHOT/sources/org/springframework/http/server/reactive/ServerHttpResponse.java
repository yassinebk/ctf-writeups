package org.springframework.http.server.reactive;

import org.springframework.http.HttpStatus;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/reactive/ServerHttpResponse.class */
public interface ServerHttpResponse extends ReactiveHttpOutputMessage {
    boolean setStatusCode(@Nullable HttpStatus httpStatus);

    @Nullable
    HttpStatus getStatusCode();

    MultiValueMap<String, ResponseCookie> getCookies();

    void addCookie(ResponseCookie responseCookie);

    default boolean setRawStatusCode(@Nullable Integer value) {
        if (value == null) {
            return setStatusCode(null);
        }
        HttpStatus httpStatus = HttpStatus.resolve(value.intValue());
        if (httpStatus == null) {
            throw new IllegalStateException("Unresolvable HttpStatus for general ServerHttpResponse: " + value);
        }
        return setStatusCode(httpStatus);
    }

    @Nullable
    default Integer getRawStatusCode() {
        HttpStatus httpStatus = getStatusCode();
        if (httpStatus != null) {
            return Integer.valueOf(httpStatus.value());
        }
        return null;
    }
}
