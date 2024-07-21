package org.springframework.web.servlet.mvc;

import javax.servlet.http.HttpServletRequest;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/mvc/LastModified.class */
public interface LastModified {
    long getLastModified(HttpServletRequest httpServletRequest);
}
