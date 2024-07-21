package org.springframework.web.accept;

import java.util.Collections;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/accept/ContentNegotiationStrategy.class */
public interface ContentNegotiationStrategy {
    public static final List<MediaType> MEDIA_TYPE_ALL_LIST = Collections.singletonList(MediaType.ALL);

    List<MediaType> resolveMediaTypes(NativeWebRequest nativeWebRequest) throws HttpMediaTypeNotAcceptableException;
}
