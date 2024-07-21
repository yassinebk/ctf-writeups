package org.springframework.web.util;

import java.net.URI;
import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/util/UriTemplateHandler.class */
public interface UriTemplateHandler {
    URI expand(String str, Map<String, ?> map);

    URI expand(String str, Object... objArr);
}
