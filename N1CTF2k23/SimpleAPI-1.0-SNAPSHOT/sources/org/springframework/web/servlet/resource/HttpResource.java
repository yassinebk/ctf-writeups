package org.springframework.web.servlet.resource;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/resource/HttpResource.class */
public interface HttpResource extends Resource {
    HttpHeaders getResponseHeaders();
}
