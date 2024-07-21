package org.springframework.web.accept;

import java.util.List;
import org.springframework.http.MediaType;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/accept/MediaTypeFileExtensionResolver.class */
public interface MediaTypeFileExtensionResolver {
    List<String> resolveFileExtensions(MediaType mediaType);

    List<String> getAllFileExtensions();
}
