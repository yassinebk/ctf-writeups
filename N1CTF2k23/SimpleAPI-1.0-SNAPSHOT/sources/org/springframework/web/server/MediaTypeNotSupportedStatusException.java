package org.springframework.web.server;

import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/server/MediaTypeNotSupportedStatusException.class */
public class MediaTypeNotSupportedStatusException extends ResponseStatusException {
    private final List<MediaType> supportedMediaTypes;

    public MediaTypeNotSupportedStatusException(String reason) {
        super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, reason);
        this.supportedMediaTypes = Collections.emptyList();
    }

    public MediaTypeNotSupportedStatusException(List<MediaType> supportedMediaTypes) {
        super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type", null);
        this.supportedMediaTypes = Collections.unmodifiableList(supportedMediaTypes);
    }

    public List<MediaType> getSupportedMediaTypes() {
        return this.supportedMediaTypes;
    }
}
