package org.springframework.web;

import java.util.List;
import org.springframework.http.MediaType;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/HttpMediaTypeNotAcceptableException.class */
public class HttpMediaTypeNotAcceptableException extends HttpMediaTypeException {
    public HttpMediaTypeNotAcceptableException(String message) {
        super(message);
    }

    public HttpMediaTypeNotAcceptableException(List<MediaType> supportedMediaTypes) {
        super("Could not find acceptable representation", supportedMediaTypes);
    }
}
