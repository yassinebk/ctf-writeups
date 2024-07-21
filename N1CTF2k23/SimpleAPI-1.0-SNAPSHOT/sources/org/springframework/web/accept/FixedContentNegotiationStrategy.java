package org.springframework.web.accept;

import java.util.Collections;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/accept/FixedContentNegotiationStrategy.class */
public class FixedContentNegotiationStrategy implements ContentNegotiationStrategy {
    private final List<MediaType> contentTypes;

    public FixedContentNegotiationStrategy(MediaType contentType) {
        this(Collections.singletonList(contentType));
    }

    public FixedContentNegotiationStrategy(List<MediaType> contentTypes) {
        Assert.notNull(contentTypes, "'contentTypes' must not be null");
        this.contentTypes = Collections.unmodifiableList(contentTypes);
    }

    public List<MediaType> getContentTypes() {
        return this.contentTypes;
    }

    @Override // org.springframework.web.accept.ContentNegotiationStrategy
    public List<MediaType> resolveMediaTypes(NativeWebRequest request) {
        return this.contentTypes;
    }
}
