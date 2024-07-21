package org.springframework.http.converter;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/converter/HttpMessageNotWritableException.class */
public class HttpMessageNotWritableException extends HttpMessageConversionException {
    public HttpMessageNotWritableException(String msg) {
        super(msg);
    }

    public HttpMessageNotWritableException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
