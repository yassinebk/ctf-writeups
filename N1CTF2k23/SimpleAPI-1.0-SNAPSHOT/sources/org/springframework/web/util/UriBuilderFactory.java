package org.springframework.web.util;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/util/UriBuilderFactory.class */
public interface UriBuilderFactory extends UriTemplateHandler {
    UriBuilder uriString(String str);

    UriBuilder builder();
}
