package org.springframework.http.server;

import java.net.URI;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/RequestPath.class */
public interface RequestPath extends PathContainer {
    PathContainer contextPath();

    PathContainer pathWithinApplication();

    RequestPath modifyContextPath(String str);

    static RequestPath parse(URI uri, @Nullable String contextPath) {
        return new DefaultRequestPath(uri, contextPath);
    }
}
