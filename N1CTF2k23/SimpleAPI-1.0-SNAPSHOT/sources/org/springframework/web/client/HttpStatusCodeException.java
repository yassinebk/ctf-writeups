package org.springframework.web.client;

import java.nio.charset.Charset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/client/HttpStatusCodeException.class */
public abstract class HttpStatusCodeException extends RestClientResponseException {
    private static final long serialVersionUID = 5696801857651587810L;
    private final HttpStatus statusCode;

    /* JADX INFO: Access modifiers changed from: protected */
    public HttpStatusCodeException(HttpStatus statusCode) {
        this(statusCode, statusCode.name(), null, null, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HttpStatusCodeException(HttpStatus statusCode, String statusText) {
        this(statusCode, statusText, null, null, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HttpStatusCodeException(HttpStatus statusCode, String statusText, @Nullable byte[] responseBody, @Nullable Charset responseCharset) {
        this(statusCode, statusText, null, responseBody, responseCharset);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HttpStatusCodeException(HttpStatus statusCode, String statusText, @Nullable HttpHeaders responseHeaders, @Nullable byte[] responseBody, @Nullable Charset responseCharset) {
        this(getMessage(statusCode, statusText), statusCode, statusText, responseHeaders, responseBody, responseCharset);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HttpStatusCodeException(String message, HttpStatus statusCode, String statusText, @Nullable HttpHeaders responseHeaders, @Nullable byte[] responseBody, @Nullable Charset responseCharset) {
        super(message, statusCode.value(), statusText, responseHeaders, responseBody, responseCharset);
        this.statusCode = statusCode;
    }

    private static String getMessage(HttpStatus statusCode, String statusText) {
        if (!StringUtils.hasLength(statusText)) {
            statusText = statusCode.getReasonPhrase();
        }
        return statusCode.value() + " " + statusText;
    }

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }
}
