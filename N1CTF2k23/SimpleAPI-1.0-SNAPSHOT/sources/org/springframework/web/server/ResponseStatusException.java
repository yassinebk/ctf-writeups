package org.springframework.web.server;

import java.util.Collections;
import java.util.Map;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/server/ResponseStatusException.class */
public class ResponseStatusException extends NestedRuntimeException {
    private final HttpStatus status;
    @Nullable
    private final String reason;

    public ResponseStatusException(HttpStatus status) {
        this(status, null, null);
    }

    public ResponseStatusException(HttpStatus status, @Nullable String reason) {
        this(status, reason, null);
    }

    public ResponseStatusException(HttpStatus status, @Nullable String reason, @Nullable Throwable cause) {
        super(null, cause);
        Assert.notNull(status, "HttpStatus is required");
        this.status = status;
        this.reason = reason;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    @Deprecated
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    public HttpHeaders getResponseHeaders() {
        Map<String, String> headers = getHeaders();
        if (headers.isEmpty()) {
            return HttpHeaders.EMPTY;
        }
        HttpHeaders result = new HttpHeaders();
        Map<String, String> headers2 = getHeaders();
        result.getClass();
        headers2.forEach(this::add);
        return result;
    }

    @Nullable
    public String getReason() {
        return this.reason;
    }

    @Override // org.springframework.core.NestedRuntimeException, java.lang.Throwable
    public String getMessage() {
        String msg = this.status + (this.reason != null ? " \"" + this.reason + "\"" : "");
        return NestedExceptionUtils.buildMessage(msg, getCause());
    }
}
