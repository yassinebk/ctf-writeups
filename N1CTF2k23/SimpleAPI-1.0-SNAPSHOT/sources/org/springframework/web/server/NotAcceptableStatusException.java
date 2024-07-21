package org.springframework.web.server;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/server/NotAcceptableStatusException.class */
public class NotAcceptableStatusException extends ResponseStatusException {
    private final List<MediaType> supportedMediaTypes;

    public NotAcceptableStatusException(String reason) {
        super(HttpStatus.NOT_ACCEPTABLE, reason);
        this.supportedMediaTypes = Collections.emptyList();
    }

    public NotAcceptableStatusException(List<MediaType> supportedMediaTypes) {
        super(HttpStatus.NOT_ACCEPTABLE, "Could not find acceptable representation");
        this.supportedMediaTypes = Collections.unmodifiableList(supportedMediaTypes);
    }

    @Override // org.springframework.web.server.ResponseStatusException
    public Map<String, String> getHeaders() {
        return getResponseHeaders().toSingleValueMap();
    }

    @Override // org.springframework.web.server.ResponseStatusException
    public HttpHeaders getResponseHeaders() {
        if (CollectionUtils.isEmpty(this.supportedMediaTypes)) {
            return HttpHeaders.EMPTY;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(this.supportedMediaTypes);
        return headers;
    }

    public List<MediaType> getSupportedMediaTypes() {
        return this.supportedMediaTypes;
    }
}
