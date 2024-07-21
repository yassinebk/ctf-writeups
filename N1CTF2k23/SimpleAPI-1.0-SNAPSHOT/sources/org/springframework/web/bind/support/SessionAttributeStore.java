package org.springframework.web.bind.support;

import org.springframework.lang.Nullable;
import org.springframework.web.context.request.WebRequest;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/bind/support/SessionAttributeStore.class */
public interface SessionAttributeStore {
    void storeAttribute(WebRequest webRequest, String str, Object obj);

    @Nullable
    Object retrieveAttribute(WebRequest webRequest, String str);

    void cleanupAttribute(WebRequest webRequest, String str);
}
